package com.training.common.response;

import lombok.Data;

/**
 * 统一响应包装
 */
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private String traceId;
    private long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(0);
        resp.setMessage("success");
        resp.setData(data);
        resp.setTimestamp(System.currentTimeMillis());
        return resp;
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(code);
        resp.setMessage(message);
        resp.setTimestamp(System.currentTimeMillis());
        return resp;
    }
}
