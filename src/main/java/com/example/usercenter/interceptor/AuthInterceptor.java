package com.example.usercenter.interceptor;

import com.example.usercenter.common.UserHolder;
import com.example.usercenter.constant.UserConstant;
import com.example.usercenter.util.AuthUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.usercenter.constant.UserConstant.EXPIRE_TIME;


/**
 * 拦截器，登录校验
 * @author _LJC
 */
public class AuthInterceptor implements HandlerInterceptor {


    private final StringRedisTemplate stringRedisTemplate;

    public AuthInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        Long userId = AuthUtils.getUserId(token);
        UserHolder.setUserId(userId);
        if (StringUtils.isBlank(token)) {
            return false;
        }

        String expireTimeStr = stringRedisTemplate.opsForValue().get(UserConstant.EXPIRE_KEY + token);
        long expireTime = Long.parseLong(expireTimeStr);
        if (System.currentTimeMillis() > expireTime) {
            return false;
        }
        String expire2 = String.valueOf(System.currentTimeMillis() + EXPIRE_TIME);
        stringRedisTemplate.opsForValue().set(UserConstant.EXPIRE_KEY + token, expire2);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserHolder.removeUserId();
    }
}
