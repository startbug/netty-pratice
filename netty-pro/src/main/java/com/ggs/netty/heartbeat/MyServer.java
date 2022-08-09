package com.ggs.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入一个Netty提供的IdleStateHandler
                            /*
                             * 1.IdleStateHandler是Netty提供的处理空闲状态的处理器
                             * 2.long readerIdleTime: 表示多长时间没有读,就会发送一个心跳检测包检测连接
                             * 3.long writerIdleTime: 表示多长时间没有写,就会发送一个心跳检测包检测连接
                             * 4.long allIdleTime: 表示多长时间没有读写,就会发送一个心跳检测包检测连接
                             * 5.TimeUnit unit: 时间单位
                             *
                             * 官方文档说明:
                             * Triggers an IdleStateEvent when a Channel has not performed read, write, or both operation for a while.
                             *
                             * 当IdleStateHandler触发后,就会传递给管道(pipeline)的下一个handler去处理
                             * 通过调用(触发)下一个handler的userEventTriggered,在该方法中处理IdleStateEvent(读空闲,写空闲,读写空闲)
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的Handler(自定义)
                            // 3s触发读空闲时间,5s触发写空闲事件,7s触发读写空闲时间
                            // 该事件会交给双向链表中的下一个Handler处理
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
