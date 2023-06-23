package com.example.usercenter.service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.ArrayUtil;
import com.example.usercenter.model.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 * @author _LJC
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testInsertUser() {
        User user = new User();
        user.setUsername("test_ljc");
        user.setUserAccount("123");
        user.setAvatarUrl("https://alifei05.cfp.cn/creative/vcg/800/new/VCG41N1227618771.jpg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123456");
        user.setEmail("123456@qq.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDeleted(0);

        boolean save = userService.save(user);
        Assertions.assertTrue(save);
        System.err.println(user.getId());
    }

    @Test
    void userRegister() {
        String account = "abcd";
        String password = "";
        String checkPassword = "12345678";
        long res = userService.userRegister(account, password, checkPassword,"1234");
        Assertions.assertEquals(-1, res);
        account = "abc";
        password = "12345678";
        res = userService.userRegister(account, password, checkPassword,"1234");
        Assertions.assertEquals(-1, res);
        account = "abcd";
        password = "123456";
        checkPassword = "123456";
        res = userService.userRegister(account, password, checkPassword,"1234");
        Assertions.assertEquals(-1, res);
        account = "ljc deq";
        password = "12345678";
        checkPassword = "12345678";
        res = userService.userRegister(account, password, checkPassword,"1234");
        Assertions.assertEquals(-1, res);
        account = "abcde";
        password = "123456789";
        checkPassword = "12345678";
        res = userService.userRegister(account, password, checkPassword,"1234");
        Assertions.assertEquals(-1, res);
        account = "123";
        password = "12345678";
        checkPassword = "12345678";
        res = userService.userRegister(account, password, checkPassword,"1234");
        Assertions.assertEquals(-1, res);

        account = "ljc1234";
        password = "12345678";
        checkPassword = "12345678";
        res = userService.userRegister(account, password, checkPassword,"1234");
        Assertions.assertTrue(res > 0);
    }

    @Test
    public void testSearchByTags() {
//        List<User> userList = userService.searchUserByTags(Arrays.asList("java", "C#"));
//        Assertions.assertNotNull(userList);
    }
}