package com.ggs.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 说明：
        // 1.创建两个线程组BossGroup和WorkerGroup
        // 2.BossGroup只处理连接请求，真正和客户端进行业务处理会交给WorkerGroup完成
        // 3.两个都是无限循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 配置参数
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class)  // 使用NioServerSocketChannel作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG, 128)  // 设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)   // 设置连接保持活动状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象
                        // 向pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 通过pipeline可以获取channel，通过channel也可以获取pipeline
                            ch.pipeline().addLast(new NettyServerHandler());    // 向管道的最后添加一个处理器(Handler)
                        }
                    });

            System.out.println("服务器已经准备好...");

            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 对关闭通道事件进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
