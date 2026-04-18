package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@Slf4j
public class RedisConfiguration {
    /**
     * 创建RedisTemplate对象，并完成相关配置,只会在spring启动时创建一次，后续只负责被注入
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建RedisTemplate对象");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置Redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置key的序列化器，防止存储对象时出现乱码
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置value的序列化器
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
