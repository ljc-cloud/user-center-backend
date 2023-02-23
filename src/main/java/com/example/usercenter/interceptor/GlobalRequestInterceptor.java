package com.example.usercenter.interceptor;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局请求拦截器
 */
@Slf4j
@Aspect
@Component
public class GlobalRequestInterceptor {

    @Around("execution(* com.example.usercenter.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 生成请求唯一id
        String id = UUID.randomUUID().toString();
        // 获取请求路径
        String requestURI = httpServletRequest.getRequestURI();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParams = "[" + StringUtils.join(args, ",") + "]";

        // 记录请求日志
        log.info("request start, id:{}, ip:{}, uri:{}, params:{}", id, httpServletRequest.getRemoteHost(),requestURI, reqParams);

        Object result = point.proceed();

        // 输入响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id:{}, cost:{}", id, totalTimeMillis);

        return result;
    }
}
