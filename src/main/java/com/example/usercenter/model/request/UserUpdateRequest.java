package com.example.usercenter.model.request;

import lombok.Data;

/**
 * @author _LJC
 */
@Data
public class UserUpdateRequest {
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 0-正常,1-异常
     */
    private Integer userStatus;

    /**
     * 权限
     * 0 - 普通用户
     * 1 - 管理员
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;

//    /**
//     * 创建时间
//     */
//    private Date createTime;
//    /**
//     * 更新时间
//     */
//    private Date updateTime;
}
