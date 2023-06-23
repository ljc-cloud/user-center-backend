package com.example.usercenter.constant;

/**
 * 用户常量
 * @author _LJC
 */
public interface UserConstant {


    String LOGIN_USER_STATE = "loginUser:";

    long PAGE_SIZE = 40;

    // ------ 权限 ------

    /**
     * 普通用户
     */
    int DEFAULT_ROLE = 0;
    /**
     * 管理员
     */
    int ADMIN_ROLE = 1;

    String EXPIRE_KEY = "expire:";
    long EXPIRE_TIME = 1000 * 60 * 60 * 10;

    String VALIDATE_PATTERN = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\[\\\\] \\\\]";

}
