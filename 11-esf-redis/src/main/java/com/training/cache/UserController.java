package com.training.cache;

import org.springframework.web.bind.annotation.*;

/**
 * 用户缓存接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserCacheService userCacheService;
    private final RateLimiter rateLimiter;

    public UserController(UserCacheService userCacheService, RateLimiter rateLimiter) {
        this.userCacheService = userCacheService;
        this.rateLimiter = rateLimiter;
    }

    /**
     * 查询用户（走缓存）
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public UserVO getById(@PathVariable Long id) {
        return userCacheService.getById(id);
    }

    /**
     * 限流示例：同一 userId 每分钟最多 10 次
     * GET /api/users/{id}/limited
     */
    @GetMapping("/{id}/limited")
    public String limited(@PathVariable Long id) {
        if (rateLimiter.isAllowed("user:" + id, 10, 60)) {
            return "允许访问";
        }
        return "被限流了，请稍后再试";
    }
}
