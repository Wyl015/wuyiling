package com.wuyiling.worktest.Utils;

import java.math.BigDecimal;

/**
 * 字符串工具类
 * @author 陶海峰
 * @date 2017-11-24
 */
public class StringOperateUtils {

    /**
     * 将字符串转成数字
     * @param number 输入字符串
     * @param value 默认值
     * @return
     */
    public static int stringToInteger(String number, int value) {
        try {
            int i = Integer.parseInt(number);
            if (i > 0) {
                return i;
            }
            return value;
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * 将BigDecimal转成字符串
     *
     * @param number 金额
     * @return 金额字符串
     */
    public static String bigDecimalToString(BigDecimal number) {
        if (number != null) {
            return number.toPlainString();
        }
        return null;
    }

    /**
     * 将BigDecimal转成字符串
     *
     * @param number 金额
     * @param divisor 除数
     * @return 金额字符串
     */
    public static String bigDecimalToString(BigDecimal number, BigDecimal divisor) {
        if (number != null) {
            BigDecimal result = number.divide(divisor, 10, BigDecimal.ROUND_DOWN);
            return result.stripTrailingZeros().toPlainString();
        }
        return null;
    }



}
