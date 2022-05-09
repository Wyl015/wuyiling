package com.wuyiling.worktest.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Short msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(msg);
    }
}
