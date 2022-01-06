package com.wuyiling.worktest.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取redis配置文件工具类
 * @author 陶海峰
 * @Date 2017-11-11
 */
public class PropertyUtils {

    private static final Logger logger = LoggerFactory.getLogger(com.yuuwei.faceview.util.PropertyUtils.class);

    private static Properties redisProps;
    private static Properties appProps;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        logger.info("开始加载properties文件内容.......");
        redisProps = new Properties();
        appProps = new Properties();
        InputStream in = null;
        try {
            // 通过类加载器进行获取properties文件流
            in = com.yuuwei.faceview.util.PropertyUtils.class.getClassLoader().getResourceAsStream("properties/redis_key_prefix.properties");
            redisProps.load(in);
            //todo: 改为value注解
            in = com.yuuwei.faceview.util.PropertyUtils.class.getClassLoader().getResourceAsStream("application.properties");
            Properties app = new Properties();
            app.load(in);
            Environment env = new StandardEnvironment();
            String profile = env.getProperty("SPRING_PROFILES_ACTIVE");
            logger.info("SPRING_PROFILES_ACTIVE: {}", profile);
            in = com.yuuwei.faceview.util.PropertyUtils.class.getClassLoader().getResourceAsStream("application-"+profile+".properties");
            appProps.load(in);
        } catch (FileNotFoundException e) {
            logger.error("文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("redisProperties文件内容：" + redisProps);
        logger.info("applicationProperties文件内容：" + appProps);
    }

    public static String getRedisProperty(String key) {
        if (null == redisProps) {
            loadProps();
        }
        return redisProps.getProperty(key);
    }

    public static String getRedisProperty(String key, String defaultValue) {
        if (null == redisProps) {
            loadProps();
        }
        return redisProps.getProperty(key, defaultValue);
    }

    public static String getAppProperty(String key) {
        if (null == appProps) {
            loadProps();
        }
        return appProps.getProperty(key);
    }

    public static String getAppProperty(String key, String defaultValue) {
        if (null == appProps) {
            loadProps();
        }
        return appProps.getProperty(key, defaultValue);
    }
}
