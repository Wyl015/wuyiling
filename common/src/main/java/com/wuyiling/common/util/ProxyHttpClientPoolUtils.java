package com.wuyiling.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * HttpClient连接池
 *
 * @author 陶海峰
 * @date 2017-10-18
 */

@Slf4j
public class ProxyHttpClientPoolUtils{

    private static final Logger logger = LoggerFactory.getLogger(ProxyHttpClientPoolUtils.class);

    private static PoolingHttpClientConnectionManager cm = null;
    public static CloseableHttpClient httpClient;

    static {
        logger.info("ProxyHttpClient连接池初始化开始");
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.error("创建SSL连接失败", e);
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 设置最大连接数
        cm.setMaxTotal(200);
        // 增加每个路由基础的连接
        cm.setDefaultMaxPerRoute(20);
        //初始化 httpClient
        httpClient = getConnection();
        logger.info("ProxyHttpClient连接池初始化完成");
        //在线程关闭的时候，关闭连接池
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                httpClient.close();
                logger.info("ProxyHttpClient连接池销毁");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private static CloseableHttpClient getConnection() {
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(50 * 1000)
                .setConnectTimeout(10 * 1000).build();

        HttpHost proxy = new HttpHost("192.168.22.150",808);
//        HttpHost proxy = new HttpHost("58.60.255.82",8118);

        return HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(config)
                // 重试机制，重试3次
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .setProxy(proxy)
                .build();
    }


    public static void main(String[] args) {
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        httpGet.setHeader("charset", StandardCharsets.UTF_8.name());
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            System.out.println(httpResponse);
        } catch (Exception e) {
            log.info("调用baidu失败：{}", e);
        }


    }

}
