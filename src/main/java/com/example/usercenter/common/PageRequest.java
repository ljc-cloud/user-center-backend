package com.example.usercenter.common;

import lombok.Data;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest {
    /**
     * 页面条数
     */
    protected int pageSize;

    /**
     * 当前页数
     */
    protected int pageNum;
}
