package com.wuyiling.worktest.netty.live;

import lombok.Data;

@Data
public class LiveMessage {

    /** 输入1表示发心跳包，输入2表示发content，5秒内不输入1则服务端会自动断开连接 **/
    static final byte TYPE_HEART = 1;
    static final byte TYPE_MESSAGE = 2;

    private byte type;
    private int length;
    private String content;
}
