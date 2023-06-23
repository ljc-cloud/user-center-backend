package com.example.usercenter.once;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cn.hutool.core.date.StopWatch;
import com.example.usercenter.mapper.UserMapper;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InsertTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;


//    @Scheduled(initialDelay = 3000, fixedRate = Long.MAX_VALUE)
    public void testInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final long NUM = 1000;
        for (long i = 0; i < NUM; i++) {
            User user = new User();
            user.setUsername("咸菜_" + i);
            user.setUserAccount("xiancai" + i);
            user.setAvatarUrl("https://yupi.icu/logo.png");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("12345678");
            user.setEmail("2066765685@163.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("");
            user.setTags("");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

//    @Scheduled(initialDelay = 3000, fixedRate = Long.MAX_VALUE)
    public void testAsyncInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUsername("咸菜_" + j + i);
                user.setUserAccount("xiancai" + j + i);
                user.setAvatarUrl("https://yupi.icu/logo.png");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("12345678");
                user.setEmail("2066765685@163.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("");
                user.setTags("");
                userList.add(user);
                if (j % 10000 == 0) {
                    break;
                }
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                userService.saveBatch(userList);
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
