package com.wuyiling.worktest.Utils.common.json;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: lingjun.jlj
 * @date: 2019/7/23 10:42
 * @version：1.0.0
 * @description:
 */
public class JsonUtils {

    /**
     * 判断字符串是否可以转化为json对象
     *
     * @param content
     * @return
     */
    public static boolean isJsonObject(String content) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        try {
            return JsonParser.parseString(content).isJsonObject();
        } catch (JsonParseException e) {
            return false;
        }
    }
}
