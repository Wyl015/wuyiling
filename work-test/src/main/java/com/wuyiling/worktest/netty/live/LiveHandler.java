package com.wuyiling.worktest.netty.live;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author wyl
 * @Description 简单的长连接demo
 * @Date 11:51 2022/5/7
 * @Param
 * @return
 **/
@Slf4j
public class LiveHandler extends SimpleChannelInboundHandler<LiveMessage> {
    // 步骤：
    // 1 创建连接（Channel）
    // 2 发心跳包
    // 3 发消息，并通知其他用户
    // 4 一段时间没收到心跳包或者用户主动关闭之后关闭连接

//    保存已创建的Channel: 放在一个Map中，以Channel.hashCode()作为key
//    自动关闭没有心跳的连接: ScheduledFuture,ChannelHandlerContext.executor().schedule()创建，支持延时提交，也支持取消任务

    private static Map<Integer, LiveChannelCache> channelCache = new HashMap<>();

    // region 必须覆写的抽象方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LiveMessage msg) throws Exception {
        Channel channel = ctx.channel();
        final int hashCode = channel.hashCode();
        log.info("==================================channel hashCode:" + hashCode + " msg:" + msg + " cache:" + channelCache.size());

        if (!channelCache.containsKey(hashCode)) {
            System.out.println("channelCache.containsKey(hashCode), put key:" + hashCode);
            channel.closeFuture().addListener(future -> {
                System.out.println("channel close, remove key:" + hashCode);
                channelCache.remove(hashCode);
            });
            ScheduledFuture scheduledFuture = ctx.executor().schedule(
                    () -> {
                        log.info("==========schedule runs, close channel:" + hashCode);
                        channel.close();
                    }, 10, TimeUnit.SECONDS);
            channelCache.put(hashCode, new LiveChannelCache(channel, scheduledFuture));
        }

        switch (msg.getType()) {
            case LiveMessage.TYPE_HEART: {

                log.info("===============msg type : {}", LiveMessage.TYPE_HEART);
                log.info("===============msg : {}", msg);
                LiveChannelCache cache = channelCache.get(hashCode);
                ScheduledFuture scheduledFuture = ctx.executor().schedule(
                        () -> {
                            log.info("====================schedule runs, close channel:" + hashCode);
                            channel.close();
                        }, 5, TimeUnit.SECONDS);
                cache.getScheduledFuture().cancel(true);
                cache.setScheduledFuture(scheduledFuture);
                ctx.channel().writeAndFlush(msg);
                break;
            }
            case LiveMessage.TYPE_MESSAGE: {
                channelCache.entrySet().stream().forEach(entry -> {
                    Channel otherChannel = entry.getValue().getChannel();
                    otherChannel.writeAndFlush(msg);
                });
                break;
            }
            default: log.error("msgtype not found: {}", msg.getType()); break;
        }
    }
    // endregion

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.debug("========================channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("=====================exceptionCaught");
        if(null != cause) {cause.printStackTrace();}
        if(null != ctx) {ctx.close();}
    }
}
