package com.example.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.usercenter.constant.UserConstant.PAGE_SIZE;

/**
 * 缓存定时任务
 */
@Slf4j
@Component
public class CacheJob {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserService userService;

    private List<Integer> recommendUserPageList = Arrays.asList(1, 2, 3, 4);

    /**
     * 缓存预热
     * 定时缓存热门用户
     */
    @Scheduled(cron = "0 50 21 * * ?")
    public void doCache() {
        RLock rLock = redissonClient.getLock("user:cachejob:docache:lock");
        try {
            if (rLock.tryLock(0, 1000, TimeUnit.MILLISECONDS)) {
                for (Integer page : recommendUserPageList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(page, PAGE_SIZE), queryWrapper);
                    String redisKey = "user:recommend" + page;
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    try {
                        valueOperations.set(redisKey, userPage, 10, TimeUnit.HOURS);
                    } catch (Exception e) {
                        log.info("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }
}
