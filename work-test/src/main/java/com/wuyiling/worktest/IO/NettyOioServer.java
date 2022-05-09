package com.wuyiling.worktest.IO;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class NettyOioServer {

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            // ServerBootstrap继承AbstractBootstrap
            ServerBootstrap b = new ServerBootstrap();

            b.
                    // 利用这个方法设置EventLoopGroup,Boos线程和Worker线程就是同一个
                    // b.group(bossGroup)
                    // 利用这个方法设置EventLoopGroup,Boos线程和Worker线程就是两个
                    // b.group(bossGroup, workerGroup)
                    // 第一个参数:处理socket连接的线程就是Boos线程,处理socket的请求
                    // 第二个参数:服务端接受了socket连接求后，会产生一个channel（一个打开的socket对应一个打开的channel），并把这个channel交给Worker线程来处理
                    // 如果客户端数目不是很多的情况,Boos线程和Worker线程可以是同一个。
                    group(group)
                    // Netty的中的Channel实现类主要有：
                    // NioServerSocketChannel（用于服务端非阻塞地接收TCP连接）
                    // NioSocketChannel（用于维持非阻塞的TCP连接）
                    // NioDatagramChannel（用于非阻塞地处理UDP连接）
                    // OioServerSocketChannel（用于服务端阻塞地接收TCP连接）
                    // OioSocketChannel（用于阻塞地接收TCP连接）
                    // OioDatagramChannel（用于阻塞地处理UDP连接）：
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}