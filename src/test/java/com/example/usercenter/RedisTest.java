package com.example.usercenter;
import java.util.Date;

import com.example.usercenter.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void testSet() {
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name", "xiancai");
//        valueOperations.set("age", 10);
//        valueOperations.set("money", 200.50);
        User user = new User();
        user.setId(0L);
        user.setUsername("xiancai");
        user.setUserAccount("xiancai");
        user.setAvatarUrl("https://yupidog.org");
        user.setGender(0);
        user.setUserPassword("12345678");
        user.setPhone("123456");
        user.setEmail("dwadwadwa");
        user.setUserStatus(0);
        user.setUserRole(0);
        user.setPlanetCode("");
        user.setTags("");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDeleted(0);
        valueOperations.set("user", user);

        Object xiancai = valueOperations.get("name");
        Assertions.assertTrue("xiancai".equals(xiancai));
        xiancai = valueOperations.get("age");
//        Assertions.assertTrue(10 == (Integer)xiancai);
        xiancai = valueOperations.get("money");
//        Assertions.assertTrue(200.50 == (Double)xiancai);
        System.out.println(valueOperations.get("user"));
    }
}
