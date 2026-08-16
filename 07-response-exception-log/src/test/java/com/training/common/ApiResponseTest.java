package com.training.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 统一响应与全局异常测试（MockMvc）
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("统一响应与全局异常测试")
class ApiResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("成功响应：code=0，data 正常返回")
    void shouldReturnSuccessFormat() throws Exception {
        mockMvc.perform(get("/api/demo/success"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.message").value("success"))
            .andExpect(jsonPath("$.data").value("hello world"))
            .andExpect(jsonPath("$.timestamp").isNumber());
    }

    @Test
    @DisplayName("业务异常：返回业务错误码，HTTP 200")
    void shouldReturnBusinessError() throws Exception {
        mockMvc.perform(get("/api/demo/biz-error"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(20001))
            .andExpect(jsonPath("$.message").value("待办不存在"))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("资源不存在：返回 10005")
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/demo/not-found"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(10005))
            .andExpect(jsonPath("$.message").value(containsString("用户 不存在")));
    }

    @Test
    @DisplayName("参数校验失败：HTTP 400 + 具体字段错误")
    void shouldReturnValidationError() throws Exception {
        mockMvc.perform(get("/api/demo/validate").param("name", "").param("age", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(10002))
            .andExpect(jsonPath("$.message").value(containsString("name 不能为空")));
    }

    @Test
    @DisplayName("未知异常：HTTP 500 + 通用提示（不泄露内部信息）")
    void shouldReturnGenericError() throws Exception {
        mockMvc.perform(get("/api/demo/unknown"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value(10001))
            .andExpect(jsonPath("$.message").value("系统繁忙，请稍后重试"))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("响应头携带 traceId")
    void shouldReturnTraceId() throws Exception {
        mockMvc.perform(get("/api/demo/success"))
            .andExpect(header().exists("X-Trace-Id"));
    }
}
