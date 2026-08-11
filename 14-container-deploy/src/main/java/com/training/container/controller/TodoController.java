package com.training.container.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 待办事项接口（内存实现）
 */
@RestController
@RequestMapping("/api")
public class TodoController {

    private final Map<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    public TodoController() {
        // 初始化示例数据（对应 sql/init.sql）
        create(new TodoRequest("学习 Docker 基础", 1));
        create(new TodoRequest("编写 Dockerfile", 2));
        create(new TodoRequest("Docker Compose 编排", 2));
    }

    /**
     * 健康检查（Docker HEALTHCHECK 使用）
     * GET /api/health
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "app", "container-app",
            "time", LocalDateTime.now().toString()
        );
    }

    /**
     * 待办列表
     * GET /api/todos
     */
    @GetMapping("/todos")
    public List<Todo> list() {
        return new ArrayList<>(store.values());
    }

    /**
     * 创建待办
     * POST /api/todos
     */
    @PostMapping("/todos")
    public Todo create(@RequestBody TodoRequest req) {
        return create0(req);
    }

    /**
     * 标记完成
     * PATCH /api/todos/{id}/done
     */
    @PatchMapping("/todos/{id}/done")
    public Todo markDone(@PathVariable Long id) {
        Todo todo = store.get(id);
        if (todo == null) {
            throw new NoSuchElementException("待办不存在: " + id);
        }
        todo.setStatus("DONE");
        return todo;
    }

    private Todo create0(TodoRequest req) {
        Long id = idGen.incrementAndGet();
        Todo todo = new Todo(id, req.getTitle(), req.getPriority(),
            "PENDING", LocalDateTime.now());
        store.put(id, todo);
        return todo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Todo {
        private Long id;
        private String title;
        private Integer priority;
        private String status;
        private LocalDateTime createTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TodoRequest {
        private String title;
        private Integer priority;
    }
}
