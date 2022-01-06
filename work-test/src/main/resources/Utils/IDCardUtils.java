package com.wuyiling.worktest.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证验证的工具（支持15位或18位省份证）
 *
 * @author mhw
 * @version v1.0
 * @date 2019-06-17
 */
public final class IDCardUtils {
    private final static Map<Integer, String> ZONE_NUM = new HashMap<>();

    static {
        ZONE_NUM.put(11, "北京");
        ZONE_NUM.put(12, "天津");
        ZONE_NUM.put(13, "河北");
        ZONE_NUM.put(14, "山西");
        ZONE_NUM.put(15, "内蒙古");
        ZONE_NUM.put(21, "辽宁");
        ZONE_NUM.put(22, "吉林");
        ZONE_NUM.put(23, "黑龙江");
        ZONE_NUM.put(31, "上海");
        ZONE_NUM.put(32, "江苏");
        ZONE_NUM.put(33, "浙江");
        ZONE_NUM.put(34, "安徽");
        ZONE_NUM.put(35, "福建");
        ZONE_NUM.put(36, "江西");
        ZONE_NUM.put(37, "山东");
        ZONE_NUM.put(41, "河南");
        ZONE_NUM.put(42, "湖北");
        ZONE_NUM.put(43, "湖南");
        ZONE_NUM.put(44, "广东");
        ZONE_NUM.put(45, "广西");
        ZONE_NUM.put(46, "海南");
        ZONE_NUM.put(50, "重庆");
        ZONE_NUM.put(51, "四川");
        ZONE_NUM.put(52, "贵州");
        ZONE_NUM.put(53, "云南");
        ZONE_NUM.put(54, "西藏");
        ZONE_NUM.put(61, "陕西");
        ZONE_NUM.put(62, "甘肃");
        ZONE_NUM.put(63, "青海");
        ZONE_NUM.put(64, "青海");
        ZONE_NUM.put(65, "新疆");
        ZONE_NUM.put(71, "台湾");
        ZONE_NUM.put(81, "香港");
        ZONE_NUM.put(82, "澳门");
        ZONE_NUM.put(91, "外国");
    }

    private final static Integer CARD_LENGTH_15 = 15;
    private final static Integer CARD_LENGTH_18 = 18;
    private final static int[] PARITY_BIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final static int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    public static boolean isIDCard(String certNo) {
        if (StringUtils.isBlank(certNo)) {
            return false;
        }
        if (certNo.length() != CARD_LENGTH_15 && certNo.length() != CARD_LENGTH_18) {
            return false;
        }

        final char[] cs = certNo.toUpperCase().toCharArray();
        // 校验位数
        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            if (i == cs.length - 1 && cs[i] == 'X') {
                break;
            }
            if (cs[i] < '0' || cs[i] > '9') {
                return false;
            }
            if (i < cs.length - 1) {
                power += (cs[i] - '0') * POWER_LIST[i];
            }
        }

        // 校验区位码
        if (!ZONE_NUM.containsKey(Integer.valueOf(certNo.substring(0, 2)))) {
            return false;
        }

        // 校验年份
        String year = certNo.length() == CARD_LENGTH_15 ?
                getIdcardCalendar() + certNo.substring(6, 8) : certNo.substring(6, 10);

        final int iyear = Integer.parseInt(year);
        if (iyear < 1900 || iyear > Calendar.getInstance().get(Calendar.YEAR)) {
            return false;
        }

        // 校验月份
        String month = certNo.length() == CARD_LENGTH_15 ?
                certNo.substring(8, 10) : certNo.substring(10, 12);
        final int imonth = Integer.parseInt(month);
        if (imonth < 1 || imonth > 12) {
            return false;
        }

        // 校验天数
        String day = certNo.length() == CARD_LENGTH_15 ?
                certNo.substring(10, 12) : certNo.substring(12, 14);
        final int iday = Integer.parseInt(day);
        if (iday < 1 || iday > 31) {
            return false;
        }

        // 校验"校验码"
        if (certNo.length() == CARD_LENGTH_15) {
            return true;
        }

        return cs[cs.length - 1] == PARITY_BIT[power % 11];
    }

    private static int getIdcardCalendar() {
        GregorianCalendar curDay = new GregorianCalendar();
        int curYear = curDay.get(Calendar.YEAR);
        return Integer.parseInt(String.valueOf(curYear).substring(2));
    }

    public static void main(String[] args) {
        boolean mark = isIDCard("450981198802261753");
        System.out.println(mark);
    }

}