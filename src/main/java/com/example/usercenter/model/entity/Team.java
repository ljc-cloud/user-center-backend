package com.example.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍表
 * @TableName team
 */
@TableName(value ="team")
@Data
public class Team implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 队伍名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 队伍最大人数
     */
    @TableField(value = "max_num")
    private Integer maxNum;

    /**
     * 队伍过期时间
     */
    @TableField(value = "expire_time")
    private Date expireTime;

    /**
     * 0-公开 1-私密 2-加密
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

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