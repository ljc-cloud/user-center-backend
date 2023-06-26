package com.example.usercenter.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TeamVO {
    /**
     * id
     */
    private Long id;

    /**
     * 队伍名
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 队伍最大人数
     */
    private Integer maxNum;

    /**
     * 队伍已加入人数
     */
    private Integer joinNum;

    /**
     * 队伍过期时间
     */
    private Date expireTime;

    /**
     * 0-公开 1-私密 2-加密
     */
    private Integer status;

    /**
     * 用户id（队长id）
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人用户
     */
//    private UserVO createUser;

}
