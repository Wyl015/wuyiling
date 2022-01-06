package com.wuyiling.worktest.Utils.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: lingjun.jlj
 * @date: 2021/1/4 14:21
 * @description: base64 工具
 */
@Slf4j
public class Base64CodecUtil {

    /**
     * base64 加密
     *
     * @param str 原字符串
     * @return 加密后的字符串
     */
    public static byte[] encode(String str) {
        if (StringUtils.isBlank(str)) {
            log.error("base64加密原字符串不能为空");
            throw new RuntimeException("Base64原字符串不能为空");
        }
        Base64 base64 = new Base64();
        return base64.encode(str.getBytes());
    }

    /**
     * base64 加密
     *
     * @param str 原字符串
     * @return 加密后的字符串
     */
    public static String encodeAsString(String str) {
        if (StringUtils.isBlank(str)) {
            log.error("base64加密原字符串不能为空");
            throw new RuntimeException("Base64原字符串不能为空");
        }
        Base64 base64 = new Base64();
        return base64.encodeAsString(str.getBytes());
    }

    /**
     * base64 解密
     *
     * @param str 原字符串
     * @return 加密后的字符串
     */
    public static String decode(String str) {
        if (StringUtils.isBlank(str)) {
            log.error("base64解密字符串不能为空");
            throw new RuntimeException("base64解密字符串不能为空");
        }
        Base64 base64 = new Base64();
        return new String(base64.decode(str));
    }

    public static void main(String[] args) {
        String str = "测试，今天天气真好";
        String sec = encodeAsString(str);
        System.out.println(sec);
        System.out.println(decode(sec));
    }
}
