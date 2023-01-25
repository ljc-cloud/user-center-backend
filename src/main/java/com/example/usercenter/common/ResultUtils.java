package com.example.usercenter.common;

/**
 * 返回工具类
 * @author _LJC
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data, ResultCode.SUCCESS);
    }

    public static <T> BaseResponse<T> fail(ResultCode resultCode) {
        return new BaseResponse<>(resultCode);
    }

    public static <T> BaseResponse<T> fail(ResultCode resultCode, String message, String description) {
        return new BaseResponse<>(resultCode, message, description);
    }

    public static <T> BaseResponse<T> fail(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }
}
