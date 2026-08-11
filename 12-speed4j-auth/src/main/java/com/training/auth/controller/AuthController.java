package com.training.auth.controller;

import com.training.auth.config.UserContext;
import com.training.auth.config.UserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${auth.token.secret:my-secret-key-for-training-at-least-32-bytes}")
    private String secret;

    @Value("${auth.token.expire-minutes:120}")
    private int expireMinutes;

    /**
     * 模拟登录（实际应对接 SSO）
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // 模拟验证（实际查数据库/调 SSO）
        if (!"admin".equals(username) || !"123456".equals(password)) {
            return Map.of("code", 401, "message", "用户名或密码错误");
        }

        // 生成 Token
        UserInfo user = new UserInfo(1L, "管理员", List.of("ADMIN", "USER"));
        String token = generateToken(user);

        return Map.of(
            "code", 0,
            "message", "登录成功",
            "data", Map.of(
                "token", token,
                "userInfo", Map.of("userId", 1, "name", "管理员", "roles", List.of("ADMIN", "USER"))
            )
        );
    }

    /**
     * SSO 回调（模拟）
     */
    @GetMapping("/callback")
    public Map<String, Object> callback(@RequestParam String code) {
        // 实际：用 code 换 access_token，再获取用户信息
        UserInfo user = new UserInfo(100L, "SSO用户", List.of("USER"));
        String token = generateToken(user);
        return Map.of("code", 0, "data", Map.of("token", token));
    }

    /**
     * 刷新 Token
     */
    @PostMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader) {
        // 简化：直接生成新 Token（实际应验证旧 Token）
        UserInfo user = UserContext.get();
        if (user == null) {
            user = new UserInfo(1L, "管理员", List.of("ADMIN"));
        }
        String newToken = generateToken(user);
        return Map.of("code", 0, "data", Map.of("token", newToken));
    }

    private String generateToken(UserInfo user) {
        return Jwts.builder()
            .setSubject(String.valueOf(user.getUserId()))
            .claim("name", user.getName())
            .claim("roles", user.getRoles())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expireMinutes * 60_000L))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
            .compact();
    }
}
