package com.example.usercenter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserCenterApplicationTests {


//    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;


    @Test
    void testUUID() {
        System.out.println(cn.hutool.core.lang.UUID.randomUUID());
    }
    @Test
    void testDigest() {
        String encryptPassword = DigestUtils.md5DigestAsHex(("ljc" + "12345678").getBytes(StandardCharsets.UTF_8));
        System.out.println(encryptPassword);
    }

    @Test

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
            userService.save(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
    @Test
    public void testAsyncInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            List<User> userList = Collections.synchronizedList(new ArrayList<>());
            while (true) {
                j++;
                User user = new User();
                user.setUsername("咸菜_" + random.nextInt() + UUID.randomUUID());
                user.setUserAccount("xiancai" + random.nextInt() + UUID.randomUUID());
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

    @Test
    void testTime() {
        String s = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        System.out.println(s);
    }

    @Test
    void testRedis() {
//        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        String k1 = stringRedisTemplate.opsForValue().get("k");
        System.out.println(k1 == null);
    }

    @Test
    void testJWT() {
        String KEY = "token$*&23";
        String id = "rs256";
        JWTSigner signer = JWTSignerUtil.createSigner(id, KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(id)));
        String token = JWT.create().setPayload("userId", "12").setSigner(signer).setKey(KEY.getBytes()).sign();
        System.out.println("生成密钥为:" + token);

        JWT jwt = JWT.of(token);
        Object userId = jwt.getPayload("userId");
        System.out.println("payload:" + userId);
    }

    @Test
    void contextLoads() {
        List<String> stringList1 = Arrays.asList("a","b","c");
        List<String> stringList2 = Arrays.asList("a","b");
        System.out.println(stringList2.containsAll(stringList1));
    }

}
