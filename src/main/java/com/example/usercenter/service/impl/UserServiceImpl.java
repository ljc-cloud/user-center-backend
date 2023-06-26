package com.example.usercenter.service.impl;
import java.nio.charset.StandardCharsets;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ResultCode;
import com.example.usercenter.constant.UserConstant;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.service.UserService;
import com.example.usercenter.mapper.UserMapper;
import com.example.usercenter.util.UploadUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.usercenter.constant.UserConstant.*;

/**
 * 用户服务实现类
* @author _LJC
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-01-07 16:35:01
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "ljc";

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        // JWT登录校验
//        Long userId = UserHolder.getUserId();
//        String userJson = stringRedisTemplate.opsForValue().get(UserConstant.LOGIN_USER_STATE + userId);
        // Session登录校验
        User user = (User)request.getSession().getAttribute(LOGIN_USER_STATE);
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ResultCode.NOT_LOGIN);
        }
        return getSafetyUser(user);
    }

    @Override
    public boolean isAdminUser(HttpServletRequest request) {
//        String userJson = stringRedisTemplate.opsForValue().get(UserConstant.LOGIN_USER_STATE + USER_THREAD.get());
        User safeUser = (User)request.getSession().getAttribute(LOGIN_USER_STATE);
        if (BeanUtil.isEmpty(safeUser)) {
            return false;
        }
        return safeUser.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    @Override
    public long userRegister(String userAccount, String password, String checkPassword, String planetCode) {
        validateUser(userAccount, password);
        //密码和校验密码是否相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码和校验密码不相同");
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
        validateUser(userAccount, password);
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
//        // 记录用户登录态
//        String token = AuthUtils.createToken(user.getId());
//        String expire = String.valueOf(System.currentTimeMillis() + EXPIRE_TIME);
//        // 设置token过期时间
//        stringRedisTemplate.opsForValue().set(UserConstant.EXPIRE_KEY + token, expire);
//        // 将用户信息存入redis
//        stringRedisTemplate.opsForValue().set(UserConstant.LOGIN_USER_STATE + user.getId(), JSONUtil.toJsonStr(safetyUser));

        request.getSession().setAttribute(LOGIN_USER_STATE, safetyUser);
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
        safetyUser.setTags(originUser.getTags());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "系统错误");
        }
        request.getSession().removeAttribute(UserConstant.LOGIN_USER_STATE);
//        stringRedisTemplate.delete(UserConstant.LOGIN_USER_STATE + USER_THREAD.get());
//        stringRedisTemplate.delete(UserConstant.EXPIRE_KEY + request.getHeader("Authorization"));
        return 1;
    }

    /**
     * 根据标签查询用户  （内存查询）
     * @param tagNameList
     * @return
     */
    @Override
    public Page<User> searchUserByTags(long pageNum, List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "未选择标签");
        }
        // SQL查询方法
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags" ,tagName);
        }
        Page<User> page = new Page<>(((pageNum - 1) * PAGE_SIZE), PAGE_SIZE);
        Page<User> userPage = this.page(page, queryWrapper);
//        List<User> userList = this.list(queryWrapper);

//        // 内存计算
//        List<User> userList = this.list();
//        Gson gson = new Gson();
//        List<User> records = userList.stream().filter(user -> {
//            // 得到json字符串
//            String tags = user.getTags();
//            if (StringUtils.isBlank(tags)) {
//                return false;
//            }
////            tags = Optional.ofNullable(tags).orElse("");
//            // 解析json
//            Set<String> userTagNameSet = gson.fromJson(tags, new TypeToken<Set<String>>() {
//            }.getType());
//            for (String tagName : tagNameList) {
//                if (!userTagNameSet.contains(tagName)) {
//                    return false;
//                }
//            }
//            return true;
//        }).map(this::getSafetyUser).collect(Collectors.toList());
        return userPage;
    }

    @Override
    public int updateUser(User user, HttpServletRequest request) {
        User currentUser = getCurrentUser(request);
        Long userId = user.getId();
        User unUpdatedUser = this.getById(userId);
        if (unUpdatedUser == null) {
            throw new BusinessException(ResultCode.NULL_ERROR);
        }
        if (currentUser.getId().longValue() != userId.longValue()) {
            throw new BusinessException(ResultCode.NO_AUTH);
        }
        // 验证用户更新信息是否符合规定
        if (StringUtils.isNotBlank(user.getUserAccount())) {
            user.setUserPassword(unUpdatedUser.getUserPassword());
        }else if (StringUtils.isNotBlank(user.getUserPassword())) {
            user.setUserAccount(unUpdatedUser.getUserAccount());
        }
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        if (!StringUtils.isAllBlank(userAccount, userPassword)) {
            validateUser(userAccount, userPassword);
        }
        int res = userMapper.updateById(user);
        User userForUpdateSession = this.getById(userId);
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_USER_STATE, userForUpdateSession);
        return res;
    }

    @Override
    public boolean validateUser(String userAccount, String userPassword) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "请求参数为空");
        }
        // 账户长度不能小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账户长度不能小于4位");
        }
        // 密码长度不能小于8
        if (userPassword.length() < 8) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码长度不能小于8");
        }
        // 账户不能包含特殊字符
        Matcher matcher = Pattern.compile(VALIDATE_PATTERN).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        return true;
    }

    @Override
    public Page<User> recommendUser(long pageNum, HttpServletRequest request) {
        String key = "user:recommend:" + pageNum;
        Page<User> userPage = (Page<User>) redisTemplate.opsForValue().get(key);
        if (userPage != null) {
            return userPage;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = this.page(new Page<>(((pageNum - 1) * PAGE_SIZE), PAGE_SIZE), queryWrapper);
        try {
            redisTemplate.opsForValue().set(key, userPage, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.info("redis set key error", e);
        }
        return userPage;
    }

    @Override
    public boolean updateUserAvatar(MultipartFile file, HttpServletRequest request) {
        User currentUser = this.getCurrentUser(request);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "-" + file.getOriginalFilename();
        String url = UploadUtil.uploadImage(file, fileName);
        currentUser.setAvatarUrl(url);
        boolean update = this.updateById(currentUser);
        if (!update) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "用户头像更新失败");
        }
        User userForSession = this.getById(currentUser.getId());
        request.getSession().setAttribute(LOGIN_USER_STATE, userForSession);
        return true;
    }
}




