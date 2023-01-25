package com.example.usercenter.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(0, "成功",""),
    PARAMS_ERROR(4000, "请求参数错误",""),
    NULL_ERROR(4001, "请求数据为空",""),
    NO_AUTH(4010,"无权限",""),
    NOT_LOGIN(4011, "未登录",""),
    USER_NOT_EXISTS(4100, "用户不存在",""),
    SYSTEM_ERROR(5000, "系统内部异常","");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 信息
     */
    private final String message;
    /**
     * 描述
     */
    private final String description;

    ResultCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
