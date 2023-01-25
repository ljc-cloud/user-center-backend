package com.example.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @author _LJC
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code,data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code,data, "", "");
    }

    public BaseResponse(T data, ResultCode resultCode) {
        this(resultCode.getCode(), data, resultCode.getMessage(), resultCode.getDescription());
    }

    public BaseResponse(ResultCode resultCode) {
        this(resultCode.getCode(), null, resultCode.getMessage(), resultCode.getDescription());
    }
    public BaseResponse(ResultCode resultCode, String message, String description) {
        this(resultCode.getCode(), null, message, description);
    }
}
