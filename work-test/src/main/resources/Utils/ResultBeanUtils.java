package com.wuyiling.worktest.Utils;

import com.yuuwei.faceview.domain.vo.order.client.NewZgOrderInfoVO;
import com.yuuwei.faceview.util.common.json.FormatUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Bean处理
 * @author thf
 * @date 2017-12-28
 * @version v1.2
 */
public class ResultBeanUtils {

    /**
     * 将Bean中的Null字段转换为空
     * @param obj
     * @return
     */
    public static Object transferNullToBlank(Object obj) {
        // 获取实体类的所有属性
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取属性的名称
            String fieldName = field.getName();
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            // 获取属性类型
            String type = field.getGenericType().toString();
            if (StringUtils.equals(type, "class java.lang.String")) {
                try {
                    Method method = obj.getClass().getMethod("get" + fieldName);
                    String value = (String) method.invoke(obj);
                    if (null == value) {
                        method = obj.getClass().getMethod("set" + fieldName, String.class);
                        method.invoke(obj, "");
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    public static void main(String[] args) {
        NewZgOrderInfoVO zgOrderInfoVO = new NewZgOrderInfoVO();
        System.out.println(zgOrderInfoVO);
        Object o = transferNullToBlank(zgOrderInfoVO);
        System.out.println(o);
        System.out.println(FormatUtils.obj2str(o));
    }

}
