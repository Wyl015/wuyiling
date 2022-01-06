package com.wuyiling.worktest.Utils;

import cn.hutool.core.lang.Snowflake;
import com.yuuwei.faceview.cache.RedisDao;
import com.yuuwei.faceview.constant.CachePrefixConstants;
import com.yuuwei.faceview.constant.SystemConsts;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 系统各种id生成策略
 *
 * @author 陶海峰
 * @date 2017-11-02
 */
@Component
public class IDUtils {

    private static RedisDao redisDao;

    @Autowired
    public IDUtils(RedisDao redisDao) {
        com.yuuwei.faceview.util.IDUtils.redisDao = redisDao;
    }

    static final long workerId = 0;
    static final long datacenterId = 1;

    static Snowflake snowflake = new Snowflake(workerId, datacenterId);

    public IDUtils() {
    }

    /**
     * 生成文件名称
     *
     * @return
     */
    public static String generateFileName() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis() / 1000L;
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        return millis + String.format("%03d", end3);
    }

    /**
     * 生成订单编号:YYYYMMDDHHMM+5位用户id+3位流水号（001-999），共20位
     *
     * @param uid 用户id
     * @return
     */
    public static String generateOrderNumber(String uid) {
        String curDate = new DateTime().toString("yyyyMMddHHmm");
        String uidNo = PropertyUtils.getRedisProperty("uidNo");
        int value = redisDao.getAndIncrement(uidNo + uid, 1);
        return curDate + uid + String.format("%03d", value % 1000);
    }

    /**
     * 生成订单编号：YYYYMMDDHHMM+5位随机数+3位流水号（001-999），共20位
     *
     * @return 预生成订单号
     */
    public static String generatePreOrderNumber() {
        String curDate = new DateTime().toString("yyyyMMddHHmm");
        Random random = new Random();
        String randomNumber = String.format("%05d", random.nextInt(99999));
        int value = redisDao.getAndIncrement(CachePrefixConstants.ORDER_NUMBER_COMSEQ, 1);
        String valueString = String.format("%03d", value % 1000);
        return curDate + randomNumber + valueString;
    }

    /**
     * 生成面签号流水号:YYYYMMDDHH+{0/1/2/3}+4位流水号
     *
     * @param type     面签类别1：免预约面签，2：预约面签
     * @param loanType 0:只有借款人，1：借款人和共还人，2：借款人，3：共还人
     * @return
     */
    public static String generateFacaviewNumber(Integer type, Integer loanType) {
        String curDate = new DateTime().toString("yyyyMMddHH");
        String serialName = PropertyUtils.getRedisProperty("serialName");
        int serial = redisDao.getAndIncrement(serialName, 1);
        return curDate + type + loanType + String.format("%04d", serial % 10000);
    }

    /**
     * 生成对接平台请求流水号：YYYYMMDDHHMMSSFFF+6位流水号
     *
     * @param
     * @return
     */
    public static String generateComseqno() {
        String curDate = new DateTime().toString("yyyyMMddHHmmssSSS");
        String comseqNo = PropertyUtils.getRedisProperty("comseqNo");
        int serial = redisDao.getAndIncrement(comseqNo, 1);
        return curDate + String.format("%06d", serial % 1000000);
    }

    /**
     * 获取房间号
     */
    public static String generateRoomName() {
        return "ROOM_" + getSnowflakeId();
    }

    /**
     * 获取呼叫流程流程号
     *
     * @return 唯一的流程号
     */
    public static String generateCallNo() {
        return "CALL_NO_" + getSnowflakeId();
    }

    /**
     * 获取临时用户账号
     */
    public static String getTempUserAccount() {
        return SystemConsts.TEMP_USER_POOL_ACCOUNT_PREFIX + getSnowflakeId();
    }

    public static String generatePicNo() {
        return generateId();
    }


    /**
     * 根据身份证号获取性别
     *
     * @param card
     * @return
     */
    public static String getCardGender(String card) {
        if (card.length() == 15) {
            if (Integer.parseInt(card.substring(14, 15)) % 2 != 0) {
                return "M";
            } else {
                return "F";
            }
        } else if (card.length() == 18) {
            if (Integer.parseInt(card.substring(16, 17)) % 2 != 0) {
                return "M";
            } else {
                return "F";
            }
        } else {
            return "";
        }
    }


    /**
     * 生成6位数的随机码
     *
     * @return
     */
    public static String generateRandomCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(899999) + 100000);
    }

    /**
     * 获取唯一ID（基于雪花算法）
     *
     * @return 字符串
     */
    public static String generateId() {
        return com.yuuwei.faceview.util.IDUtils.getSnowflakeId() + "";
    }

    /**
     * 获取雪花算法
     *
     * @return Long
     */
    public static long getSnowflakeId() {
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(getSnowflakeId());
        System.out.println(System.currentTimeMillis());
    }

}
