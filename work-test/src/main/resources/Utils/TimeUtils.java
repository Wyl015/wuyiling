package com.wuyiling.worktest.Utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 时间操作工具类
 *
 * @author 陶海峰
 * @date 2017-11-08
 */
public class TimeUtils {

    public final static String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String PATTERN_YYYYMMDD = "yyyyMMdd";
    public final static String PATTERN_HHMMSS = "HHMMSS";
    public final static String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String PATTERN_YYYY_MM = "yyyy-MM";
    public final static String PATTERN_HH_MM_SS = "HH:mm:ss";
    public final static String PATTERN_HH_MM = "HH:mm";
    public final static String PATTERN_HH = "HH";

    /**
     * 转换时间
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM);
        Date time = null;
        try {
            time = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 将date格式转为String
     *
     * @param date
     * @return
     */
    public static String parseDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM);
        return format.format(date);
    }

    /**
     * 将date格式转为String
     *
     * @param date
     * @return
     */
    public static String parseDateToString2(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
        return format.format(date);
    }


    public static String parseDateToString3(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD);
        return format.format(date);
    }


    /**
     * 将date格式转为String
     *
     * @param date
     * @return
     */
    public static String parseDateToStringBlank(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
        return format.format(date);
    }

    /**
     * 将date格式转为String
     *
     * @param date
     * @return
     */
    public static String parseDateToMinutes(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_HH_MM);
        return format.format(date);
    }

    /**
     * 根据dateformat将date格式转为String
     *
     * @param date
     * @return
     */
    public static String parseDateToString(Date date , String dateFormat) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    /**
     * 根据预约时间计算预约单的过期时间
     * 现在时间+默认过期时间 < 预约时间，则过期时间为默认过期时间
     * 现在时间+默认过期时间 > 预约时间，则过期时间为预约时间与现在时间的差值
     *
     * @param reserveTime 预约时间
     * @param type        面签单所属银行
     * @return 返回过期时间，单位：分钟
     */
    public static int getExpireTime(String reserveTime, String type) {
        // 默认过期时间
        String reserveExpireTime = PropertyUtils.getAppProperty("reserveExpireTime");
        int expire = Integer.parseInt(reserveExpireTime);

        DateTime reserve = new DateTime(parseDate(reserveTime));
        DateTime now = new DateTime(new Date());

        int minutes = Minutes.minutesBetween(now, reserve).getMinutes();
        return minutes > expire ? expire : minutes;
    }

    /**
     * 获取两个时间的时间差
     *
     * @param d1
     * @param d2
     * @return 分钟(d1 - d2)
     */
    public static int getMinutesBetweenDate(Date d1, Date d2) {
        DateTime reserve = new DateTime(d1);
        DateTime now = new DateTime(d2);
        return Minutes.minutesBetween(now, reserve).getMinutes();
    }

    /**
     * 获取两个时间的时间差
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDaysBetweenDate(String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD);
        Date s = null;
        Date e = null;
        try {
            s = format.parse(start);
            e = format.parse(end);
            DateTime d1 = new DateTime(s);
            DateTime d2 = new DateTime(e);
            return Days.daysBetween(d1, d2).getDays();
        } catch (ParseException e1) {
            e1.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取两个时间的相差天数
     *
     * @param d1
     * @param d2
     * @return d2距离的d1的时间
     */
    public static int getDaysBetweenDate(Date d1, Date d2) {
        DateTime reserve = new DateTime(d1);
        DateTime now = new DateTime(d2);
        return Days.daysBetween(reserve, now).getDays();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurTime() {
        DateTime dateTime = new DateTime();
        return dateTime.toString(PATTERN_YYYY_MM_DD_HH_MM);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurTime(String pattern) {
        DateTime dateTime = new DateTime();
        return dateTime.toString(pattern);
    }


    /**
     * 获取Date格式当前时间
     *
     * @return
     */
    public static Date getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
        Date time = null;
        try {
            time = format.parse(format.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 返回联系时间
     *
     * @param time
     * @return 今天的只显示HH:mm:ss,昨天的显示:昨天 HH:mm:ss,其他的显示完整时间
     */
    public static String getFormatDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_YYYY_MM_DD);
        int days = 0;
        try {
            DateTime dateTime = new DateTime(simpleDateFormat.parse(simpleDateFormat.format(time)));
            DateTime now = new DateTime(new DateTime(simpleDateFormat.parse(simpleDateFormat.format(new Date()))));
            days = Days.daysBetween(now, dateTime).getDays();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (days == 0) {
            SimpleDateFormat format = new SimpleDateFormat(PATTERN_HH_MM_SS);
            return format.format(time);
        } else if (days == -1) {
            SimpleDateFormat format = new SimpleDateFormat(PATTERN_HH_MM_SS);
            return "昨天：" + format.format(time);
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
        return format.format(time);
    }

    /**
     * 获取当前时间
     *
     * @return 2018年03月09日 星期五
     */
    public static String getTime() {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        String month = String.format("%02d", dateTime.getMonthOfYear());
        String day = String.format("%02d", dateTime.getDayOfMonth());
        int dayOfWeek = dateTime.getDayOfWeek();
//        String hour = String.format("%02d", dateTime.getHourOfDay());
//        String minute = String.format("%02d", dateTime.getMinuteOfHour());
        return year + "年" + month + "月" + day + "日 " + getWeek(dayOfWeek);
    }

    /**
     * 获取当前是周几
     *
     * @return
     */
    private static String getWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static String getStringOfTimestamp(Long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
        return format.format(timestamp);
    }

    public static String getStringOfTimestamp(Long timestamp, String patter) {
        SimpleDateFormat format = new SimpleDateFormat(patter);
        return format.format(timestamp);
    }

    public static String getStringOfLocalDateTime(LocalDateTime localDateTime, String patter) {
        return getStringOfTimestamp(getTimestampOfDateTime(localDateTime), patter);
    }

    public static String getStringOfLocalDateTime(LocalDateTime localDateTime) {
        return getStringOfTimestamp(getTimestampOfDateTime(localDateTime));
    }

    public static Date parseDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date time = null;
        try {
            time = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void main(String[] args) {
        System.out.println("当前时间：" + getStringOfLocalDateTime(LocalDateTime.now(), PATTERN_YYYYMMDDHHMMSS));
    }


}
