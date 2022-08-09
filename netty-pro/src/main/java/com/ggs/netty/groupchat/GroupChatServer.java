package com.ggs.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author starbug
 * <h2>ChannelOption.SO_BACKLOG参数简介:</h2>
 * <ul>
 * <b>backlog 指定了内核为此套接口排队的最大连接个数；</b>
 * <li>对于给定的监听套接口，内核要维护两个队列: 未连接队列和已连接队列</li>
 * <li>backlog 的值即为未连接队列和已连接队列的和。</li>
 * <li>详解地址:https://blog.csdn.net/weixin_44730681/article/details/113728895</li>
 * </ul>
 **/
public class GroupChatServer {

    /**
     * 监听端口
     **/
    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    // 处理客户端请求
    public void run() throws InterruptedException {
        // 创建两个线程组(boss group和worker group)
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();    // 默认是2 * cpu核心数

        ServerBootstrap serverBootstrap = null;
        try {
            serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            // 向pipeline加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 向pipeline加入编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            // 加入自定义的Handler
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "-Netty 服务器启动");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(7000).run();
    }

}
