package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求类
 * @author _LJC
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 3555464884523L;
    private String userAccount;
    private String userPassword;

}
