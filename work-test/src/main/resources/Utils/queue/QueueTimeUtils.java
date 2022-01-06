package com.wuyiling.worktest.Utils.queue;

/**
 * @author: lingjun.jlj
 * @date: 2019/8/22 11:19
 * @description:
 */
public class QueueTimeUtils {

    /**
     * 排队时间格式化
     *
     * @return 返回 00:00:00 格式时间
     */
    public static String transferQueueTime(Long computeTime) {
        if (computeTime == null) {
            return "00:00:00";
        }
        Long hour = computeTime / 3600L;
        computeTime = computeTime % 3600L;
        Long minute = computeTime / 60L;
        Long second = computeTime % 60L;
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02d", hour))
                .append(":")
                .append(String.format("%02d", minute))
                .append(":")
                .append(String.format("%02d", second));
        return builder.toString();
    }

    /**
     * 计算时间
     *
     * @param startTime 开始时间，毫秒
     * @return 返回 00:00，分钟:秒，格式时间
     */
    public static String computeQueueTime(Long startTime) {
        if (startTime == null) {
            return "00:00";
        }
        long duration = System.currentTimeMillis() - startTime;
        duration = duration / 1000;
        Long hour = duration / 3600L;
        duration = duration % 3600L;
        Long minute = duration / 60L;
        Long second = duration % 60L;
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02d", minute))
                .append(":")
                .append(String.format("%02d", second));
        return builder.toString();
    }

    /**
     * 时间格式化，返回中文格式
     *
     * @param duration 时间差，毫秒
     * @return 返回，格式 1小时20分钟
     */
    public static String computeQueueTimeZh(Long duration) {
        if (duration == null) {
            return "0分钟";
        }
        duration = duration / 1000;
        Long hour = duration / 3600L;
        duration = duration % 3600L;
        Long minute = duration / 60L;
        StringBuilder builder = new StringBuilder();
        if (hour > 0) {
            builder.append(hour).append("小时");
        }
        if (minute > 0) {
            builder.append(minute).append("分钟");
        }
        return builder.length() == 0 ? "0分钟" : builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(computeQueueTimeZh(208100L));
    }
}
