package com.wuyiling.worktest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisTemplate配置
 *
 * @author thf
 * @version v1.0.0
 * @date 2019-09-27
 */
@Configuration
public class RedisConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return stringRedisTemplate;
    }
}
