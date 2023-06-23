package com.example.usercenter.dto;

import com.example.usercenter.common.PageRequest;

import java.io.Serializable;

public class TeamQuery extends PageRequest implements Serializable {
    private static final long serialVersionUID = 2148932841211123L;

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
}
