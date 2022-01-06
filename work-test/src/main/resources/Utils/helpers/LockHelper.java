package com.wuyiling.worktest.Utils.helpers;

import com.yuuwei.faceview.cache.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具类
 *
 * @author mhw
 * @version v1.0
 * @date 2019-11-21
 */
@Slf4j
@Component
public class LockHelper {

    /**
     * 缓存过期时间，单位毫秒
     */
    private static final long DEFAULT_TIMEOUT = 1000;
    /**
     * 尝试次数
     */
    private static final int DEFAULT_RETRIES = 1;
    /**
     * 重试等待时间，单位毫秒
     */
    private static final long DEFAULT_WAITING_TIME = 200;

    private final RedisDao redisDao;

    @Autowired
    public LockHelper(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    /**
     * 获取锁
     *
     * @param key 锁的键
     * @return 获取锁是否成功
     */
    public boolean getLock(String key) {
        return this.getLock(key, "", DEFAULT_TIMEOUT, DEFAULT_RETRIES, DEFAULT_WAITING_TIME);
    }

    /**
     * 获取锁
     *
     * @param key   锁的键
     * @param value 锁对应的值
     * @return 获取锁是否成功
     */
    public boolean getLock(String key, String value) {
        return this.getLock(key, value, DEFAULT_TIMEOUT, DEFAULT_RETRIES, DEFAULT_WAITING_TIME);
    }

    /**
     * 获取锁，同时配置锁过期时间
     *
     * @param key     锁的键
     * @param value   锁对应的值
     * @param timeout 锁失效时间，单位毫秒
     * @return 获取锁是否成功
     */
    public boolean getLock(String key, String value, long timeout) {
        return this.getLock(key, value, timeout, DEFAULT_RETRIES, DEFAULT_WAITING_TIME);
    }


    /**
     * 获取锁，可配置锁过期时间和获取锁的次数
     *
     * @param key         锁的键
     * @param value       锁对应的值
     * @param timeout     锁失效时间，单位毫秒
     * @param retries     尝试获取锁的次数
     * @param waitingTime 重试等待时间，单位毫秒
     * @return 获取锁是否成功
     */
    public boolean getLock(String key, String value, long timeout, int retries, long waitingTime) {
        try {
            while (retries >= NumberUtils.INTEGER_ZERO) {
                log.debug("尝试获取锁【{}，{}】", key, value);
                boolean result = redisDao.tryLock(key, value, timeout, TimeUnit.MILLISECONDS);
                if (result) {
                    log.debug("获取锁【{}，{}】成功", key, value);
                    return true;
                }
                if (--retries > NumberUtils.INTEGER_ZERO) {
                    log.info("获取锁【{}，{}】失败，等待【{}】ms后重试，还剩【{}】次", key, value, waitingTime, retries);
                    TimeUnit.MILLISECONDS.sleep(waitingTime);
                }
            }
            log.info("获取锁【{}，{}】失败，不再重试", key, value);
            return false;
        } catch (InterruptedException e) {
            log.error("获取锁异常", e);
            return false;
        }
    }

    /**
     * 解锁
     *
     * @param key           锁的键
     * @param expectedValue 待释放的锁的值
     */
    public void unlock(String key, String expectedValue) {
        if (!redisDao.hasKey(key)) {
            log.info("key【{}】已经失效，不做删除操作", key);
            return;
        }
        String currentValue = redisDao.getValue(key);
        if (StringUtils.equals(currentValue, expectedValue)) {
            log.info("成功删除key：【{}，{}】", key, expectedValue);
            redisDao.delete(key);
        } else {
            log.info("key【{}】对应的value为【{}】，而不是【{}】，不予删除", key, currentValue, expectedValue);
        }
    }
}
