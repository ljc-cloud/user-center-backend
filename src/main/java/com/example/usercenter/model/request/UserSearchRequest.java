package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author _LJC
 */
@Data
public class UserSearchRequest implements Serializable {

    private static final long serialVersionUID = 5488573884523L;
    private String username;
    private String userAccount;
    private Integer gender;
    private String phone;
    private String email;
    private String planetCode;
    private Integer userStatus;
    private Integer userRole;
}
