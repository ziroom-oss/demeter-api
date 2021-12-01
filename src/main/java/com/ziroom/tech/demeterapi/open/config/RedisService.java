package com.ziroom.tech.demeterapi.open.config;

import com.ziroom.tech.demeterapi.open.redis.RedisConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuzeyu
 */
@Configuration
@ConditionalOnExpression("!'${spring.profiles.active}'.equals('test')")
public class RedisService extends RedisConfig {
}
