package com.example.usercenter.model.request;

import lombok.Data;

/**
 * @author _LJC
 */
@Data
public class UserSearchRequest {
    private String username;
    private String userAccount;
    private Integer gender;
    private String phone;
    private String email;
    private String planetCode;
    private Integer userStatus;
    private Integer userRole;
}
