package com.wuyiling.worktest.Utils.common.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.yuuwei.faceview.util.common.json.serializer.JacksonLocalDateTimeDeserializer;
import com.yuuwei.faceview.util.common.json.serializer.JacksonLocalDateTimeSerializer;
import com.yuuwei.faceview.util.common.json.serializer.JacksonLongDeserializer;
import com.yuuwei.faceview.util.common.json.serializer.JacksonLongSerializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: lingjun.jlj
 * @date: 2021/6/1 16:51
 * @description: 基于 jackson 的json 解析工具
 */
public class FormatUtils {

    private static Logger log = LoggerFactory.getLogger(FormatUtils.class);

    /**
     * 自定义序列化程序
     */
    private static final SimpleModule module = (new SimpleModule())
            .addSerializer(Long.class, new JacksonLongSerializer())
            .addDeserializer(Long.class, new JacksonLongDeserializer())
            .addSerializer(LocalDateTime.class, new JacksonLocalDateTimeSerializer())
            .addDeserializer(LocalDateTime.class, new JacksonLocalDateTimeDeserializer());

    private static final ObjectMapper xmlMapper = new XmlMapper()
            .setDefaultUseWrapper(false)
            // 未知参数
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 解决java8时间冲突问题
     */
    private static final ObjectMapper objectMapper = (new ObjectMapper())
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            //序列化结果中不包含null值
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            //允许：默认false_不解析含有结束语控制字符
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
            //主要是这行配置，意思是在遇到未知字段时是否失败，默认为true，也就是遇到未知字段时会报错
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(module);

    /**
     * 对象转化成json
     *
     * @param obj 对象
     * @return json
     */
    public static <T> String obj2str(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("jackson obj2str error, obj={}, e={}", obj, e.getMessage());
            return null;
        }
    }

    /**
     * json 字符串转换成对象
     *
     * @param str   json 字符串
     * @param clazz 转换类型
     * @return
     */
    public static <T> T str2obj(String str, Class<T> clazz) {
        try {
            return StringUtils.isEmpty(str) ? null : objectMapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            log.error("jackson str2obj error, str={}, e={}", str, e.getMessage());
            return null;
        }
    }

    /**
     * 反序列化json 为 集合
     *
     * @param src   json
     * @param clazz 对象
     * @param <T>   对象类型
     * @return 反序列化后的对象
     */
    public static <T> List<T> toArray(String src, Class<T> clazz) {
        if (src == null || clazz == null) {
            return null;
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(src, javaType);
        } catch (JsonProcessingException e) {
            log.error("parse json[{}] to array error", src, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * json 字符串转换成对象
     *
     * @param str           json 字符串
     * @param typeReference 转换类型 TypeReference<T> type = new TypeReference<T>() {};
     * @return
     */
    public static <T> T str2obj(String str, TypeReference<T> typeReference) {
        try {
            return StringUtils.isEmpty(str) ? null : objectMapper.readValue(str, typeReference);
        } catch (JsonProcessingException e) {
            log.error("jackson str2obj error, str={}, e={}", str, e.getMessage());
            return null;
        }
    }

    /**
     * 从json串中获取某个节点的值：子json
     *
     * @param src json
     * @return 子json
     */
    public static String childJson(String src, String node) {
        try {
            JsonNode jsonNode = objectMapper.readTree(src);
            return jsonNode.get(node).toString();
        } catch (Exception e) {
            log.error("parse json[{}] node[{}], error", src, node, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 从json串中获取某个节点的值：json的节点的值
     *
     * @param src json
     * @return string
     */
    public static String childStr(String src, String node) {
        try {
            JsonNode jsonNode = objectMapper.readTree(src);
            return jsonNode.get(node).toString();
        } catch (Exception e) {
            log.error("get json[{}] node[{}], error", src, node, e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 从json串中获取某个节点的值：json的节点的值
     *
     * @param src json
     * @return int
     */
    public static int childInt(String src, String node) {
        try {
            JsonNode jsonNode = objectMapper.readTree(src);
            return jsonNode.get(node).asInt();
        } catch (Exception e) {
            log.error("get json[{}] node[{}], error", src, node, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * xml 字符串转换成对象
     *
     * @param str   xml 字符串
     * @param clazz 转换类型
     * @return
     */
    public static <T> T xml2obj(String str, Class<T> clazz) {
        try {
            return StringUtils.isEmpty(str) ? null : xmlMapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            log.error("jackson xml2obj error, str={}, e={}", str, e.getMessage());
            return null;
        }
    }

    /**
     * xml 字符串转换成对象
     *
     * @param str           xml 字符串
     * @param typeReference 转换类型 TypeReference<T> type = new TypeReference<T>() {};
     * @return
     */
    public static <T> T xml2obj(String str, TypeReference<T> typeReference) {
        try {
            return StringUtils.isEmpty(str) ? null : xmlMapper.readValue(str, typeReference);
        } catch (JsonProcessingException e) {
            log.error("jackson xml2obj error, str={}, e={}", str, e.getMessage());
            return null;
        }
    }

    /**
     * 对象转化成json
     *
     * @param obj 对象
     * @return xml
     */
    public static <T> String obj2xml(T obj) {
        try {
            return xmlMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("jackson obj2xml error, obj={}, e={}", obj, e.getMessage());
            return null;
        }
    }
}
