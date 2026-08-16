package com.training.web.service;

import com.training.web.dto.CreateTodoDTO;
import com.training.web.dto.UpdateTodoDTO;
import com.training.web.vo.TodoVO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 待办事项服务（内存实现）
 */
@Service
public class TodoService {

    private final Map<Long, TodoVO> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    public TodoService() {
        // 初始化示例数据
        create(new CreateTodoDTO("学习 Spring Boot", 1));
        create(new CreateTodoDTO("编写单元测试", 2));
        create(new CreateTodoDTO("部署到测试环境", 3));
    }

    public List<TodoVO> list(String status) {
        return store.values().stream()
            .filter(todo -> status == null || todo.getStatus().equals(status))
            .sorted(Comparator.comparing(TodoVO::getCreateTime).reversed())
            .collect(Collectors.toList());
    }

    public TodoVO getById(Long id) {
        TodoVO todo = store.get(id);
        if (todo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "待办不存在: " + id);
        }
        return todo;
    }

    public TodoVO create(CreateTodoDTO dto) {
        Long id = idGen.incrementAndGet();
        TodoVO todo = new TodoVO();
        todo.setId(id);
        todo.setTitle(dto.getTitle());
        todo.setPriority(dto.getPriority());
        todo.setStatus("PENDING");
        todo.setCreateTime(LocalDateTime.now());
        store.put(id, todo);
        return todo;
    }

    public TodoVO update(Long id, UpdateTodoDTO dto) {
        TodoVO todo = getById(id);
        if (dto.getTitle() != null) {
            todo.setTitle(dto.getTitle());
        }
        if (dto.getPriority() != null) {
            todo.setPriority(dto.getPriority());
        }
        todo.setUpdateTime(LocalDateTime.now());
        return todo;
    }

    public void delete(Long id) {
        if (store.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "待办不存在: " + id);
        }
    }

    public TodoVO markDone(Long id) {
        TodoVO todo = getById(id);
        todo.setStatus("DONE");
        todo.setUpdateTime(LocalDateTime.now());
        return todo;
    }
}
