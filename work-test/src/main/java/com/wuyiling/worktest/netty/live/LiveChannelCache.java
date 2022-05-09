package com.wuyiling.worktest.netty.live;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author wyl
 * @Description 因为我们需要将Channel和ScheduledFuture缓存在Map里面，所以需要将两个对象组合成一个JavaBean
 * @Date 10:36 2022/5/7
 * @Param
 * @return
 **/
@Data
@AllArgsConstructor
public class LiveChannelCache {
    private Channel channel;
    private ScheduledFuture scheduledFuture;
}
