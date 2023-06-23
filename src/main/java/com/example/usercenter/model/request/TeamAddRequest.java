package com.example.usercenter.model.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TeamAddRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 队伍名
     */
    private String name;

    /**
     *
     */
    private String description;

    /**
     * 队伍最大人数
     */
    private Integer maxNum;

    /**
     * 队伍过期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 0-公开 1-私密 2-加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
