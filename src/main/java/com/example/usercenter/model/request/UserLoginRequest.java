package com.example.usercenter.model.request;

import lombok.Data;

/**
 * 用户登录请求类
 * @author _LJC
 */
@Data
public class UserLoginRequest {
    private String userAccount;
    private String userPassword;
}
