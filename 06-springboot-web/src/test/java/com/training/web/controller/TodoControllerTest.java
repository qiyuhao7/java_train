package com.training.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Todo 接口测试（MockMvc）
 * 不启动服务器，直接测试 Controller 层
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("待办事项接口测试")
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("查询列表：返回 3 条初始数据")
    void shouldListTodos() throws Exception {
        mockMvc.perform(get("/api/todos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    @DisplayName("按状态过滤列表")
    void shouldFilterByStatus() throws Exception {
        // 用不存在的状态验证过滤逻辑（不依赖测试执行顺序）
        mockMvc.perform(get("/api/todos").param("status", "NON_EXISTENT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("查询详情：返回指定待办")
    void shouldGetById() throws Exception {
        mockMvc.perform(get("/api/todos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").isNotEmpty());
    }

    @Test
    @DisplayName("创建待办：返回 201 和创建的数据")
    void shouldCreateTodo() throws Exception {
        String json = "{\"title\":\"写单元测试\",\"priority\":1}";

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("写单元测试"))
            .andExpect(jsonPath("$.priority").value(1))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("创建待办：标题为空返回 400")
    void shouldFailWhenTitleBlank() throws Exception {
        String json = "{\"title\":\"\",\"priority\":1}";

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("创建待办：优先级超出范围返回 400")
    void shouldFailWhenPriorityInvalid() throws Exception {
        String json = "{\"title\":\"test\",\"priority\":99}";

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("更新待办")
    void shouldUpdateTodo() throws Exception {
        String json = "{\"title\":\"更新后的标题\"}";

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("更新后的标题"));
    }

    @Test
    @DisplayName("标记完成")
    void shouldMarkDone() throws Exception {
        mockMvc.perform(patch("/api/todos/1/done"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    @DisplayName("删除待办：返回 204")
    void shouldDeleteTodo() throws Exception {
        mockMvc.perform(delete("/api/todos/2"))
            .andExpect(status().isNoContent());

        // 删除后查询应为 404
        mockMvc.perform(get("/api/todos/2"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("查询不存在的待办：404")
    void should404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/todos/999"))
            .andExpect(status().isNotFound());
    }
}
