package com.example.usercenter.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 使用JWT 拦截器和Redis做登录校验
 * @author _LJC
 */
@Configuration
public class CommonConfig implements WebMvcConfigurer {

//    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final String[] excludeSwaggerUri = new String[] { "/**/doc.html", "/**/webjars/**", "/**/favicon.ico",
            "/**/swagger-resources", "/**/v2/**"};

//    /**
//     * 添加拦截器
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor(stringRedisTemplate)).addPathPatterns("/**").
//                excludePathPatterns("/**/login", "/**/register","/**/user/search/tags").excludePathPatterns(excludeSwaggerUri).order(1);
//    }


    /**
     * 处理跨域请求
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET","POST","PUT","DELETE")
                .maxAge(3600);
    }
}
