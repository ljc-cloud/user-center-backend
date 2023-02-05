package com.example.usercenter.service;

import com.example.usercenter.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author _LJC
* @description 针对表【user】的数据库操作Service
* @createDate 2023-01-07 16:35:01
*/
@Service
public interface UserService extends IService<User> {

    /**
     * 获取当前用户信息
     * @return 脱敏用户
     */
    User getCurrentUser(String token);

    /**
     * 是否为管理员
     * @return
     */
    boolean isAdminUser();

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param password 用户密码
     * @param checkPassword 检查密码
     * @return 用户id
     */
    long userRegister(String userAccount, String password, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount 用户账号
     * @param password    用户密码
     * @param request
     * @return token
     */
    String userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 脱敏用户信息
     * @param originUser 初始用户
     * @return
     */
    User getSafetyUser(User originUser);


    /**
     * 用户注销
     * @param request 获取Session
     * @return
     */
    int userLogout(HttpServletRequest request);
}
