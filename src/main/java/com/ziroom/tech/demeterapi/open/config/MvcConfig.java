package com.ziroom.tech.demeterapi.open.config;

import com.ziroom.tech.demeterapi.open.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author xuzeyu
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource(name = "loginHandlerInterceptor")
    private HandlerInterceptor loginHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns("/**");
        //registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/**");
    }
}
