package com.ggs.netty.codec2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        // 客户端只需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();

            // 设置相关参数
            bootstrap.group(group) // 设置线程组
                    .channel(NioSocketChannel.class)    // 设置客户端通道的实现类(反射)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 在pipeline中加入ProtoBufEncoder编码器
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new NettyClientHandler());    // 加入处理器
                        }
                    });

            System.out.println("客户端已经准备好了~");

            // 启动客户端去连接服务器端
            // ChannelFuture涉及到Netty的异步模型(后面讲解)
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();

            // 监听通道关闭事件
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

}
