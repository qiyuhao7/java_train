package com.training.common.response;

import lombok.Getter;

/**
 * 业务状态码枚举
 */
@Getter
public enum ResultCode {
    SUCCESS(0, "成功"),
    SYSTEM_ERROR(10001, "系统内部错误"),
    PARAM_ERROR(10002, "参数校验失败"),
    NOT_FOUND(10005, "资源不存在"),

    // 待办模块 2xxxx
    TODO_NOT_FOUND(20001, "待办不存在"),
    TODO_ALREADY_DONE(20002, "待办已完成，不可重复操作");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
