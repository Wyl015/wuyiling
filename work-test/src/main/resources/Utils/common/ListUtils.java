package com.wuyiling.worktest.Utils.common;

import java.util.List;

/**
 * @author: lingjun.jlj
 * @date: 2019-03-19 16:08
 * @description:
 */
public class ListUtils {

    /**
     * 判断List是否为空
     *
     * @param list
     */
    public static boolean isEmpty(List list) {

        return list == null || list.isEmpty();
    }

    /**
     * 判断List不为空
     *
     * @param list
     */
    public static boolean isNotEmpty(List list) {

        return !isEmpty(list);
    }

}
