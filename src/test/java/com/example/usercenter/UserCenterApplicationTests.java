package com.example.usercenter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserCenterApplicationTests {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testDigest() {
        String encryptPassword = DigestUtils.md5DigestAsHex("abcd1234".getBytes(StandardCharsets.UTF_8));
        System.out.println(encryptPassword);
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
    }

}
