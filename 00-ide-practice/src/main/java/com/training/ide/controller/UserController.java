package com.training.ide.controller;

import com.training.ide.model.Order;
import com.training.ide.model.User;
import com.training.ide.service.OrderService;
import com.training.ide.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 * 练习：Ctrl+B 跳转、Alt+F7 查找引用、全局搜 @GetMapping 查路由
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users — 列表
     */
    @GetMapping
    public List<User> list() {
        return userService.listAll();
    }

    /**
     * GET /api/users/{id} — 详情
     */
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * POST /api/users — 创建
     */
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    /**
     * POST /api/users/batch?count=5 — 批量创建
     */
    @PostMapping("/batch")
    public List<User> batch(@RequestParam int count) {
        return userService.batchCreate(count);
    }
}
