package com.example.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 账号
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 头像url
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 密码
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * 手机号码
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 0-正常,1-异常
     */
    @TableField(value = "user_status")
    private Integer userStatus;

    /**
     * 权限
     * 0 - 普通用户
     * 1 - 管理员
     */
    @TableField(value = "user_role")
    private Integer userRole;

    /**
     * 星球编号
     */
    @TableField(value = "planet_code")
    private String planetCode;

    /**
     * 标签
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}