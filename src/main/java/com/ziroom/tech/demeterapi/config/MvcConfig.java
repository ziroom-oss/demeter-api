package com.ziroom.tech.demeterapi.config;

import com.ziroom.tech.demeterapi.interceptor.UserParamInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xuzeyu
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserParamInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new DemeterInterceptor()).addPathPatterns("/**");
    }
}
