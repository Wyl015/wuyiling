package com.wuyiling.worktest.Utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.yuuwei.faceview.util.common.json.FormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq工具类
 *
 * @author thf
 * @version v1.2
 * @date 2018-03-12
 */
public class RabbitMqFactoryUtils {

    private static final Logger logger = LoggerFactory.getLogger(com.yuuwei.faceview.util.RabbitMqFactoryUtils.class);

    private static Connection connection = null;
    private static ConnectionFactory factory = null;
    private static final String QUEUE_NAME = PropertyUtils.getAppProperty("mq.queue.name");
    private static final String HOST = PropertyUtils.getAppProperty("mq.host");
    private static final String USERNAME = PropertyUtils.getAppProperty("mq.username");
    private static final String PASSWORD = PropertyUtils.getAppProperty("mq.password");
    private static final String VIRTUAL_HOST = PropertyUtils.getAppProperty("mq.virtual-host");

    static {
        logger.info("rabbitMQ初始化========================开始");
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        factory.setAutomaticRecoveryEnabled(true);
        // 设置每10s ，重试一次
        factory.setNetworkRecoveryInterval(2);
        // 设置不重新声明交换器，队列等信息。
        factory.setTopologyRecoveryEnabled(false);
        logger.info("rabbitMQ初始化========================结束");
    }

    public static Connection getConnection() {
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            logger.error("RabbitMQ创建connection失败:{}", e.getMessage(), e);
        }
        return connection;
    }

    /**
     * 发送消息
     *
     * @param obj
     */
    public static void sendMq(Object obj) {
        String message = FormatUtils.obj2str(obj);
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            // 队列名称，是否持久化（true表示是，队列将在服务器重启时生存），是否独占队列，当所有消费者客户端断开连接时是否自动删除队列，队列其他参数
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 交换机的名称， 队列映射的路由key， 消息的其他属性，
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            logger.info("向MQ发送消息成功：{}.", message);
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            logger.error("向MQ发送消息失败：{},失败原因：{}", message, e.getMessage(), e);
        }
    }

}
