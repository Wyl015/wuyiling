package com.wuyiling.worktest.Utils;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author kanx
 * @date 2021/11/18 22:28
 * @descrip 1.0
 */
public class ReflectClassUtils {

    /**
     * 获取单个对象的所有键值对
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> getKeyAndValue(T t) {
        Map<String, String> map = Maps.newHashMap();
        Class clazz = (Class) t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        map = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, field -> {
            String resultObj = null;
            field.setAccessible(true);
            try {
                resultObj = field.get(t).toString();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return Optional.ofNullable(resultObj).orElse("");
        }, (k1, k2) -> k2));
        return map;
    }

    public static void printInnerParamValue(Class<?> clasz){
        Class innerClazz[] = clasz.getDeclaredClasses();
        for(Class claszInner : innerClazz){
            Field[] fields = claszInner.getDeclaredFields();
            for(Field field : fields){
                try {
                    Object object = field.get(claszInner);
                    System.out.println("获取到的feild, name=" + field.getName()+",   value="+ object.toString());
                    //打印内容
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
