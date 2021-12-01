package com.ziroom.tech.demeterapi.open.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * redis连接、连接池、部署相关等配置中心
 * @author xuzeyu
 */
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Resource
    private RedisProperties properties;

    /**
     *
     * @Description: JedisPoolConfig 连接池配置
     * @author xuzeyu
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(properties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(properties.getMaxIdle());
        jedisPoolConfig.setMinIdle(properties.getMinIdle());
        jedisPoolConfig.setBlockWhenExhausted(properties.isBlockWhenExhausted());
        jedisPoolConfig.setMaxWaitMillis(properties.getMaxWaitMillis());
        jedisPoolConfig.setTestOnBorrow(properties.isTestOnBorrow());
        jedisPoolConfig.setTestOnReturn(properties.isTestOnReturn());
        jedisPoolConfig.setTestWhileIdle(properties.isTestWhileIdle());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        jedisPoolConfig.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        jedisPoolConfig.setNumTestsPerEvictionRun(properties.getNumTestsPerEvictionRun());
        return jedisPoolConfig;
    }

    /**
     *
     * @Description: 单点部署连接配置
     * @author xuzeyu
     */
    @ConditionalOnProperty(name = RedisProperties.DeployMode.REDIS_MODE, havingValue = RedisProperties.DeployMode.STANDALONE, matchIfMissing = true)
    @Bean
    public RedisConnectionFactory standAloneJedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {

        String[] hostAndPort = StringUtils.split(properties.getHostName(), ":");
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostAndPort[0], Integer.valueOf(hostAndPort[1]));
        redisStandaloneConfiguration.setPassword(RedisPassword.of(properties.getPassword()));
        redisStandaloneConfiguration.setDatabase(properties.getDatabase());

        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(properties.getTimeout()))
                .readTimeout(Duration.ofMillis(properties.getTimeout()))
                .usePooling()
                .poolConfig(jedisPoolConfig).build();

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    /**
     *
     * @Description: 哨兵部署连接配置
     * @author xuzeyu
     */
    @Bean
    @ConditionalOnProperty(name = RedisProperties.DeployMode.REDIS_MODE, havingValue = RedisProperties.DeployMode.SENTINEL)
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(properties.getSentinelMasterName(), org.springframework.util.StringUtils.commaDelimitedListToSet(properties.getHostName()));
        redisSentinelConfiguration.setPassword(RedisPassword.of(properties.getPassword()));
        redisSentinelConfiguration.setDatabase(properties.getDatabase());

        return new JedisConnectionFactory(redisSentinelConfiguration, jedisPoolConfig);
    }

    /**
     *
     * @Description: 分片部署连接配置
     * @author xuzeyu
     */
    @ConditionalOnProperty(name = RedisProperties.DeployMode.REDIS_MODE, havingValue = RedisProperties.DeployMode.CLUSTER)
    @Bean
    public RedisConnectionFactory clusterRedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(org.springframework.util.StringUtils.commaDelimitedListToSet(properties.getHostName()));
        redisClusterConfiguration.setPassword(RedisPassword.of(properties.getPassword()));

        return new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
    }


    /**
     *
     * @Description: 实例化 RedisTemplate对象
     * @author xuzeyu
     */
    @Bean
    public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        /** 配置连接工厂 **/
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        /** 配置序列化方式 **/
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }


    /**
     *
     * @Description: 注入Util工具类
     * @author xuzeyu
     */
    @Bean
    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil redisUtil = new RedisUtil(redisTemplate);
        return redisUtil;
    }
}