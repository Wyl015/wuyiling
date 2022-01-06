package com.wuyiling.worktest.Utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串编解码工具类
 * @author
 * @date 2017-11-28
 */
public class CharacterCodeUtils {

    /**
     * unicode转字符串
     * @param unicode
     * @return
     */
    public static String unicodeToString(String unicode) {
        if (StringUtils.isBlank(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i=unicode.indexOf("\\u", pos)) != -1){
            sb.append(unicode.substring(pos, i));
            if(i+5 < unicode.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(unicode.substring(i+2, i+6), 16));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String msg = "\\u4e3bapp\\u7528\\u6237\\u672a\\u767b\\u5f55";
        System.out.println(unicodeToString(msg));
    }

}
