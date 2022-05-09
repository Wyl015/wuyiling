package com.wuyiling.worktest.netty;

import com.wuyiling.worktest.netty.live.LiveHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


/**
 * @Author wyl
 * @Description 启动类,负责启动（BootStrap）和main方法
 * @Date 9:46 2022/5/7
 * @Param
 * @return
 **/
public class HttpServer {

    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + HttpServer.class.getSimpleName() + " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);

        System.out.println("==================start=================" + "port" + port);
        new HttpServer(port).start();
    }

    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        System.out.println("initChannel ch:" + ch);
                        ch.pipeline()
                                //Decoder和Encoder，他们分别就是ChannelInboundHandler和ChannelOutboundHandler，分别用于在数据流进来的时候将字节码转换为消息对象和数据流出去的时候将消息对象转换为字节码。
                                .addLast("decoder", new HttpRequestDecoder())   //Encoder最重要的实现类是MessageToByteEncoder<T>，这个类的作用就是将消息实体T从对象转换成byte，写入到ByteBuf,通过之后的ChannelOutboundHandler传递到客户端
                                .addLast("encoder", new HttpResponseEncoder())  //将字节流转换为实体对象Message,数据传到服务端有可能不是一次请求就能完成的，中间可能需要经过几次数据传输，并且每一次传输传多少数据也是不确定的,  decode和decodeLast
                                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                //消息聚合器,如此代表聚合的消息内容长度不超过512kb FullHttpRequest,FullHttpResponse
                                //如果没有aggregator，那么一个http请求就会通过多个Channel被处理
                                .addLast("handler", new LiveHandler());   //自定义处理接口
                    }

                    //Encoder和Decoder除了能完成Byte和Message的相互转换之外，为了处理复杂的业务逻辑，还能帮助使用者完成Message和Message的相互转换，Http协议的处理中就用到了很多MessageToMessage的派生类。

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        b.bind(port).sync();
    }
}
