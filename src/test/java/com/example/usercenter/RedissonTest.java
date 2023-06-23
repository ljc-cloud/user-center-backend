package com.example.usercenter;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        RList<Object> rList = redissonClient.getList("test-list");
        rList.add("zhangsan");
        System.out.println("rList[0] -> " + rList.get(0));
//        rList.remove("zhangsan");
    }
}
