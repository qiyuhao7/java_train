package com.training.common;

import com.training.common.exception.BusinessException;
import com.training.common.exception.NotFoundException;
import com.training.common.response.ApiResponse;
import com.training.common.response.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 演示接口：触发各类响应和异常
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    /**
     * 成功响应
     * GET /api/demo/success
     */
    @GetMapping("/success")
    public ApiResponse<String> success() {
        return ApiResponse.success("hello world");
    }

    /**
     * 业务异常
     * GET /api/demo/biz-error
     */
    @GetMapping("/biz-error")
    public ApiResponse<Void> bizError() {
        throw new BusinessException(ResultCode.TODO_NOT_FOUND);
    }

    /**
     * 资源不存在
     * GET /api/demo/not-found
     */
    @GetMapping("/not-found")
    public ApiResponse<Void> notFound() {
        throw new NotFoundException("用户", 999L);
    }

    /**
     * 参数校验异常
     * GET /api/demo/validate?name=
     */
    @GetMapping("/validate")
    public ApiResponse<String> validate(
            @RequestParam @NotBlank(message = "name 不能为空") String name,
            @RequestParam @Min(value = 1, message = "age 必须大于0") int age) {
        return ApiResponse.success(name + ":" + age);
    }

    /**
     * 未捕获异常（500）
     * GET /api/demo/unknown
     */
    @GetMapping("/unknown")
    public ApiResponse<Void> unknown() {
        throw new RuntimeException("模拟未知异常");
    }
}
