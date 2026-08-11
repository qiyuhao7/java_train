package com.training.common.exception;

import com.training.common.response.ResultCode;

/**
 * 资源不存在异常
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String resource, Object id) {
        super(ResultCode.NOT_FOUND.getCode(),
            String.format("%s 不存在: %s", resource, id));
    }

    public NotFoundException(ResultCode resultCode, Object id) {
        super(resultCode.getCode(),
            String.format("%s: %s", resultCode.getMessage(), id));
    }
}
