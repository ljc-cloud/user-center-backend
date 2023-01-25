package com.example.usercenter.service.impl;
import java.nio.charset.StandardCharsets;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.common.ResultCode;
import com.example.usercenter.constant.UserConstant;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.service.UserService;
import com.example.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
* @author _LJC
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-01-07 16:35:01
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "ljc";

    @Override
    public long userRegister(String userAccount, String password, String checkPassword, String planetCode) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword, planetCode)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "请求参数为空");
        }
        // 账户长度不能小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户账户过短");
        }
        // 密码长度不能小于8
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\[\\\\]_ \\\\n]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        //密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码和校验密码相同");
        }
        // 星球编号过长
        if (planetCode.length() > 5) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "星球编号过长");
        }

        // 账户不能相同
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "已存在账户:" + userAccount);
        }

        // 星球编号不能相同
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planet_code", planetCode);
        count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "已存在星球编号:" + planetCode);
        }

        // 加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        log.info("加密密码为:{}", encryptPassword);

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        user.setAvatarUrl("https://yupi.icu/logo.png");
        String username = DateUtil.date().toString("yyyyMMddHHmmss") + "-" + userAccount;
        user.setUsername(username);

        // 保存到数据库
        boolean saveRes = this.save(user);
        if (!saveRes) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "保存数据库错误");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, password)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "请求参数为空");
        }
        // 账户长度不能小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账户长度过短");
        }
        // 密码长度不能小于8
        if (password.length() < 8) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\[\\\\]_ \\\\n]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }

        // 加密密码，与数据库匹配
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount).eq("user_password",encryptPassword);

        User user = this.getOne(queryWrapper);
        if (user == null) {
            log.info("用户登录失败,账户或密码不匹配...");
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "账户或密码错误");
        }

        User safetyUser = getSafetyUser(user);

        // 记录用户登录态
        request.getSession().setAttribute(UserConstant.LOGIN_USER_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        // 脱敏用户信息
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "系统错误");
        }
        request.getSession().removeAttribute(UserConstant.LOGIN_USER_STATE);
        return 1;
    }
}




