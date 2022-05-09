package com.wuyiling.worktest.netty;

import com.wuyiling.worktest.netty.live.LiveMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;


public class LiveDecoder extends ReplayingDecoder<LiveDecoder.LiveState> {
// 继承ReplayingDecoder，泛型LiveState，用来表示当前读取的状态
// RelayingDecoder在使用的时候需要搞清楚的两个方法是checkpoint(S s)和state()

    public enum LiveState {
        // 描述LiveState，有读取长度和读取内容两个状态
        LENGTH,
        CONTENT
    }

    private LiveMessage message = new LiveMessage();

    public LiveDecoder() {
//        初始化的时候设置为读取长度的状态
        super(LiveState.LENGTH);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch (state()) {
            case LENGTH:
                int length = byteBuf.readInt();
                if (length > 0) {
                    checkpoint(LiveState.CONTENT);  // 如果读取出来的长度大于0，则设置为读取内容状态，下一次读取的时候则从这个位置开始
                } else {
                    list.add(message);
                }
                break;
            case CONTENT:
                byte[] bytes = new byte[message.getLength()];
                byteBuf.readBytes(bytes);
                String content = new String(bytes);
                message.setContent(content);
                list.add(message);
                break;
            default:
                throw new IllegalStateException("invalid state:" + state());
        }
    }

    // 不是所有的标准 ByteBuf 操作都被支持，如果调用一个不支持的操作会抛出 UnreplayableOperationException
    //
    // ReplayingDecoder 略慢于 ByteToMessageDecoder
    //
    // ==>如果不引入过多的复杂性 使用 ByteToMessageDecoder 。否则,使用ReplayingDecoder。
}
