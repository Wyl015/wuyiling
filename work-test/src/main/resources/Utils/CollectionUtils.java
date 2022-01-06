package com.wuyiling.worktest.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 集合处理工具
 * @author thf
 * @date 2018-01-27
 * @version v1.2
 */
public class CollectionUtils {

    /**
     * 从list中抽取count个元素，将这几个元素组成新的数组返回
     * @param  list 被抽取list
     * @param count 抽取元素的个数
     * @return  由抽取元素组成的新list
     */
    public static List getRandomList(List list, int count) {
        if(list.size() <= count || count == 0){
            return list;
        }
        Random random = new Random();
        List<Object> newList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int temp = random.nextInt(list.size());
            newList.add(list.get(temp));
            list.remove(list.get(temp));
        }
        return newList;
    }

    /**
     * 从list中随机抽取一个元素
     *
     * @param
     * @return
     */
    public static <T> T getRandomElement(List<T> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        Random random = new Random();
        int temp = random.nextInt(list.size());
        return list.get(temp);
    }

}
