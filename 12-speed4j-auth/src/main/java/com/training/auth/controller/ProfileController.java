package com.training.auth.controller;

import com.training.auth.config.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 受保护接口示例
 */
@RestController
@RequestMapping("/api")
public class ProfileController {

    /**
     * 需要 Token 才能访问（白名单外）
     * GET /api/profile
     */
    @GetMapping("/profile")
    public Map<String, Object> profile() {
        return Map.of(
            "userId", UserContext.getCurrentUserId(),
            "name", UserContext.getCurrentUserName()
        );
    }

    /**
     * 健康检查（白名单内，无需 Token）
     * GET /api/health
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "UP");
    }
}
