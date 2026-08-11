package com.training.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 健康检查（K8s 探针使用）
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "app", "training-service",
            "time", LocalDateTime.now().toString()
        );
    }

    @GetMapping("/version")
    public Map<String, Object> version() {
        return Map.of("version", "1.0.0");
    }
}
