package com.example.usercenter.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ResultCode;
import com.example.usercenter.common.ResultUtils;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.model.request.UserLoginRequest;
import com.example.usercenter.model.request.UserRegisterRequest;
import com.example.usercenter.model.request.UserSearchRequest;
import com.example.usercenter.model.request.UserUpdateRequest;
import com.example.usercenter.service.UserService;
import com.example.usercenter.util.UploadUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.usercenter.constant.UserConstant.PAGE_SIZE;

/**
 * 用户接口
 *
 * @author _LJC
 */
@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:5173")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User safeUser = userService.getCurrentUser(request);
        return ResultUtils.success(safeUser);
    }

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "请求参数为空");
        }
        long res = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(res);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "请求参数为空");
        }
        User safeUser = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(safeUser);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        return ResultUtils.success(userService.userLogout(request));
    }

    @GetMapping("/admin/search")
    public BaseResponse<List<User>> adminSearchUsers(UserSearchRequest searchRequest, HttpServletRequest request) {
        if (!userService.isAdminUser(request)) {
            throw new BusinessException(ResultCode.NO_AUTH, "该用户不是管理员");
        }
        String username = searchRequest.getUsername();
        String userAccount = searchRequest.getUserAccount();
        String phone = searchRequest.getPhone();
        String email = searchRequest.getEmail();
        Integer gender = searchRequest.getGender();
        String planetCode = searchRequest.getPlanetCode();
        Integer userStatus = searchRequest.getUserStatus();
        Integer userRole = searchRequest.getUserRole();
        if (StringUtils.isAllBlank(username, userAccount, phone, email, planetCode) && gender == null && userStatus == null && userRole == null) {
            List<User> userList = userService.list();
            List<User> userCollect = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
            return ResultUtils.success(userCollect);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username).like(StringUtils.isNotBlank(userAccount), "user_account", userAccount)
                .eq(gender != null, "gender", gender).like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(email), "email", email).eq(StringUtils.isNotBlank(planetCode), "planet_code", planetCode)
                .eq(userStatus != null, "user_status", userStatus).eq(userRole != null, "user_role", userRole);

        List<User> userList = userService.list(queryWrapper);
        List<User> userCollect = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(userCollect);
    }

    @GetMapping("/search/tags")
    public BaseResponse<Page<User>> searchUserByTags(long pageNum, @RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.searchUserByTags(pageNum, tagNameList));
    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageNum, HttpServletRequest request) {
        Page<User> userPage = userService.recommendUser(pageNum, request);
        return ResultUtils.success(userPage);
    }
    @PostMapping("/admin/delete")
    public BaseResponse<Boolean> adminDeleteUser(@RequestBody String id, HttpServletRequest request) {
        if (!userService.isAdminUser(request)) {
            throw new BusinessException(ResultCode.NO_AUTH, "该用户不是管理员");
        }
        boolean res = userService.removeById(id);
        return ResultUtils.success(res);
    }

    @PutMapping("/admin/update")
    public BaseResponse<Boolean> adminUpdateUser(@RequestBody UserUpdateRequest updateRequest, HttpServletRequest request) {
        if (!userService.isAdminUser(request)) {
            throw new BusinessException(ResultCode.NO_AUTH, "该用户不是管理员");
        }
        User user = BeanUtil.copyProperties(updateRequest, User.class);
        boolean updateRes = userService.updateById(user);
        return ResultUtils.success(updateRes);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        if (user == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        int result = userService.updateUser(user, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/updateAvatar")
    public BaseResponse<Boolean> updateAvatar(@RequestBody MultipartFile file, HttpServletRequest request) {
        boolean res = userService.updateUserAvatar(file, request);
        return ResultUtils.success(res);
    }

}
