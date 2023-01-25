package com.example.usercenter.exception;

import com.example.usercenter.common.ResultCode;

/**
 * 自定义业务异常类
 * @author _LJC
 */
public class BusinessException extends RuntimeException{
    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ResultCode resultCode) {
        this(resultCode.getMessage(), resultCode.getCode(), resultCode.getDescription());
    }

    public BusinessException(ResultCode resultCode, String description) {
        this(resultCode.getMessage(), resultCode.getCode(), description);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
