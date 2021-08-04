package com.ziroom.tech.demeterapi.config;

import com.github.dozermapper.core.Mapper;
import com.ziroom.tech.demeterapi.common.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author libingsi
 * @date 2021/6/22 19:22
 */
@Configuration
public class BeanMapperConfig {

    @Autowired
    private Mapper mapper;

    @Bean
    public BeanMapper beanMapper(Mapper mapper)
    {
        return new BeanMapper(mapper);
    }
}
