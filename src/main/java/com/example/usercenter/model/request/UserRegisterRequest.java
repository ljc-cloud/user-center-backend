package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求类
 * @author _LJC
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUid = 125616516514676L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;

    private String planetCode;
}
