package com.training.web.controller;

import com.training.web.dto.CreateTodoDTO;
import com.training.web.dto.UpdateTodoDTO;
import com.training.web.service.TodoService;
import com.training.web.vo.TodoVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 待办事项 RESTful 接口
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * 查询列表（支持状态过滤）
     * GET /api/todos?status=PENDING
     */
    @GetMapping
    public List<TodoVO> list(@RequestParam(required = false) String status) {
        return todoService.list(status);
    }

    /**
     * 查询详情
     * GET /api/todos/{id}
     */
    @GetMapping("/{id}")
    public TodoVO getById(@PathVariable Long id) {
        return todoService.getById(id);
    }

    /**
     * 创建待办
     * POST /api/todos
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoVO create(@Valid @RequestBody CreateTodoDTO dto) {
        return todoService.create(dto);
    }

    /**
     * 更新待办
     * PUT /api/todos/{id}
     */
    @PutMapping("/{id}")
    public TodoVO update(@PathVariable Long id, @Valid @RequestBody UpdateTodoDTO dto) {
        return todoService.update(id, dto);
    }

    /**
     * 删除待办
     * DELETE /api/todos/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }

    /**
     * 标记完成
     * PATCH /api/todos/{id}/done
     */
    @PatchMapping("/{id}/done")
    public TodoVO markDone(@PathVariable Long id) {
        return todoService.markDone(id);
    }
}
