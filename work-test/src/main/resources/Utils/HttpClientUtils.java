package com.wuyiling.worktest.Utils;

import com.alibaba.fastjson.JSONObject;
import com.yuuwei.faceview.constant.NeteaseConsts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HttpClient请求工具类
 * @author 陶海峰
 * @date 2017-10-13
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(com.yuuwei.faceview.util.HttpClientUtils.class);

    public static final String CHARSET = "UTF-8";
    private static final int ERROR_CODE = 500;
    private static final int FORBIDDEN_CODE = 404;

    /**
     * 根据接口路径创建相应的HttpPost
     * @param url 接口地址
     * @return
     */
    public static HttpPost getHttpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        String curTime = String.valueOf(System.currentTimeMillis() / 1000L);
        String checkSum = EncryptUtils.sha1(NeteaseConsts.APP_SECRET.concat(NeteaseConsts.NONCE).concat(curTime));
        httpPost.setHeader("AppKey", NeteaseConsts.APP_KEY);
        httpPost.setHeader("Nonce", NeteaseConsts.NONCE);
        httpPost.setHeader("CurTime", curTime);
        httpPost.setHeader("CheckSum", checkSum);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        return httpPost;
    }

    /**
     * 根据接口路径创建相应的HttpGet
     * @param url 接口地址
     * @return
     */
    public static HttpGet getHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return httpGet;
    }

    /**
     * 根据接口路径创建相应的HttpGet 专门用于网易通讯 Header中设置参数
     * @param url 接口地址
     * @return
     */
    public static HttpGet getNeteaseHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        String curTime = String.valueOf(System.currentTimeMillis() / 1000L);
        String checkSum = EncryptUtils.sha1(NeteaseConsts.APP_SECRET.concat(NeteaseConsts.NONCE).concat(curTime));
        httpGet.setHeader("AppKey", NeteaseConsts.APP_KEY);
        httpGet.setHeader("Nonce", NeteaseConsts.NONCE);
        httpGet.setHeader("CurTime", curTime);
        httpGet.setHeader("CheckSum", checkSum);
        httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
        return httpGet;
    }

    /**
     * POST请求
     *
     * @param url        url链接
     * @param bodyObject 请求体json
     * @return 返回响应json
     */
    public static JSONObject post(String url, JSONObject bodyObject) {
        logger.info("url: {}, request: {}", url, bodyObject);
        HttpPost post;
        HttpEntity httpEntity;
        CloseableHttpClient httpClient = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "application/json; charset=utf-8");
            // 构建消息实体
            StringEntity entity = new StringEntity(bodyObject.toString(), CHARSET);
            entity.setContentEncoding(CHARSET);
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("status code : {}", statusCode);
            if (statusCode == ERROR_CODE || statusCode == FORBIDDEN_CODE) {
                return null;
            }
            httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
            logger.info("result: {}", result);
        } catch (Exception e) {
            logger.error("调失败: {}", e.getMessage());
            throw new RuntimeException("http 调失败");
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("关闭连接失败");
                }
            }
        }
        if (result == null || "".equals(result)) {
            result = "{}";
        }
        return JSONObject.parseObject(result);
    }
}
