package com.wuyiling.worktest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;


@SpringBootTest
public class RedisZSetTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return stringRedisTemplate;
    }

    @Test
    public void mainTest() {
        System.out.println("1");
        this.addZSet("key", "value1", 9.12);
//        this.addZSet("key", "value2", 9.13);
//        this.addZSet("key", "value3", 9.14);
//        System.out.println(reverseRangeWithScoresZSet("key", 0, -1));

    }



    /**
     * zset 添加元素 K-V-S
     */
    public void addZSet(String key, String value, double score) {
        ZSetOperations<String, String> operation = stringRedisTemplate.opsForZSet();
        operation.add(key, value, score);
    }
    /**
     * zset 删除元素 K-V
     */
    public void removeZSet(String key, String value) {
        ZSetOperations<String, String> operation = stringRedisTemplate.opsForZSet();
        operation.remove(key, value);
    }
    /**
     * zset 添加元素 K-ALL
     */
    public Set<String> getZSet(String key) {
        ZSetOperations<String, String> operation = stringRedisTemplate.opsForZSet();
        long count = operation.size(key);
        if (count < 1) {
            return new HashSet();
        }
        return operation.range(key, 0, count - 1);
    }


    /**
     * zset 删除元素 K-V
     */
    public void incrementScoreZSet(String key, String value, double score) {
        ZSetOperations<String, String> operation = stringRedisTemplate.opsForZSet();
        operation.incrementScore(key, value, score);
    }

    /**
     * zset 获取元素-按排名
     */
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScoresZSet(String key, Integer Start, Integer End) {
        ZSetOperations<String, String> operation = stringRedisTemplate.opsForZSet();
        return operation.reverseRangeWithScores(key, Start, End);
    }



}
