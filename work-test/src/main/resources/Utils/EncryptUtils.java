package com.wuyiling.worktest.Utils;

import com.yuuwei.faceview.constant.NeteaseConsts;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类,目前支持SHA1和MD5加密
 * @author 陶海峰
 * @date 2017-10-09
 */
public class EncryptUtils {

    private static final char[] HEX_DIGITS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    /**
     * SHA1加密
     * @param input 要加密的内容
     * @return
     */
    public static String sha1(String input) {
        return encrypt("SHA1", input);
    }

    /**
     * MD5加密
     * @param input 要加密的内容
     * @return
     */
    public static String md5(String input) {
        return encrypt("MD5", input);
    }

    private static String encrypt(String algorithm, String input) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("待加密的内容不能为空！！");
        }
        if (StringUtils.isBlank(algorithm)) {
            algorithm = "MD5";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(input.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFormattedText(byte[] bytes) {
        int length = bytes.length;
        StringBuilder buf = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            buf.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[(bytes[i] & 0x0f)]);
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        String curTime = String.valueOf(System.currentTimeMillis() / 1000L);
        String checkSum = com.yuuwei.faceview.util.EncryptUtils.sha1(NeteaseConsts.APP_SECRET.concat(NeteaseConsts.NONCE).concat(curTime));
        System.out.println(curTime);
        System.out.println(checkSum);
    }

}
