package com.wuyiling.worktest.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ToIntegerDecoder extends ByteToMessageDecoder {

    // decode和decodeLast的不同之处，在于他们的调用时机不同，正如描述所说，decodeLast只有在Channel的生命周期结束之前会调用一次，默认是调用decode方法。
    // 因为不知道这次请求发过来多少数据，所以每次都要判断byte长度够不够4，如果你的数据长度更长，且不固定的话，这里的逻辑会变得非常复杂
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 4) {
            list.add(byteBuf.readInt());
        }
    }

    // 所以在这里介绍另一个我们常用的解码器 ：ReplayingDecoder。
    //​ ReplayingDecoder 是 byte-to-message 解码的一种特殊的抽象基类，读取缓冲区的数据之前需要检查缓冲区是否有足够的字节，使用ReplayingDecoder就无需自己检查；若ByteBuf中有足够的字节，则会正常读取；若没有足够的字节则会停止解码。
    // 详情 见 LiveDecoder
}
