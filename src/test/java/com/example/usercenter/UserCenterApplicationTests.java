package com.example.usercenter;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserCenterApplicationTests {

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
    void contextLoads() {
    }

}
