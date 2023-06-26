package com.example.usercenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    User getCurrentUser(HttpServletRequest request);

    /**
     * 是否为管理员
     * @return
     */
    boolean isAdminUser(HttpServletRequest request);

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
    User userLogin(String userAccount, String password, HttpServletRequest request);

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

    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    Page<User> searchUserByTags(long pageNum, List<String> tagNameList);


    /**
     * 更新用户
     * @param user 传来的用户信息
     * @param request 获取当前登录用户信息
     * @return 结果
     */
    int updateUser(User user, HttpServletRequest request);

    /**
     * 验证用户的账户和密码是否符合规定
     * @return
     */
    boolean validateUser(String userAccount, String userPassword);

    Page<User> recommendUser(long pageNum, HttpServletRequest request);

    boolean updateUserAvatar(MultipartFile file, HttpServletRequest request);
}
