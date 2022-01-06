package com.wuyiling.worktest.Utils.cache;

import com.yuuwei.faceview.util.cache.serializer.ISerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @author: lingjun.jlj
 * @date: 2019/10/18 10:27
 * @description: spring mvc 整合注释类缓存
 */
@Slf4j
@Component
public class RedisCache implements Cache {

    @Autowired
    private ISerializer serializer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
//    private RedisTemplate redisTemplate;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.redisTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        log.info("get cache, key:{}", key);
        final String keyf = key.toString();
        Object object = null;
        object = redisTemplate.execute((RedisCallback<Object>) connection -> {
            byte[] key1 = keyf.getBytes();
            byte[] value = connection.get(key1);
            if (value == null) {
                return null;
            }
            return serializer.deserialize(value);
        });
        return (object != null ? new SimpleValueWrapper(object) : null);
    }

    @Override
    public void put(Object key, Object value) {
        log.info("put cache, key:{}", key);
        final String keyf = key.toString();
        final Object valuef = value;
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            byte[] keyb = keyf.getBytes();
            byte[] valueb = serializer.serialize(valuef);
            connection.set(keyb, valueb);
            return 1L;
        });
    }

    @Override
    public void evict(Object key) {
        log.info("del cache, key:{}", key);
        final String keyf = key.toString();
        redisTemplate.execute((RedisCallback<Long>) connection -> connection.del(keyf.getBytes()));
    }

    @Override
    public void clear() {
        log.info("clear cache");
        redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }
}
