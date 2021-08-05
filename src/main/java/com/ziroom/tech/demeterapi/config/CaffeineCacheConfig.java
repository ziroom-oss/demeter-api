package com.ziroom.tech.demeterapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author lipp3
 * @date 2021/6/28 10:33
 * @Description
 */
@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager(){
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(100_0000)
                .maximumSize(100_0000)
                .expireAfterWrite(10, TimeUnit.MINUTES);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);

        return cacheManager;
    }
}
