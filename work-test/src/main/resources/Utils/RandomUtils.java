package com.wuyiling.worktest.Utils;


import java.util.Random;

/**
 * 随机数工具类
 *
 * @author mhw
 * @version v1.0
 * @date 2019-11-20
 */
public class RandomUtils {

    public static String getRandomNumberString() {
        return String.valueOf(getRandomNumber());
    }

    /**
     * 返回一个随机数
     * 随机数介于0-99999
     */
    public static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(99999);
    }

    public static long getRandomNumber(long min, long max) {
        return min + (int)(Math.random() * (max-min+1));
    }

}
