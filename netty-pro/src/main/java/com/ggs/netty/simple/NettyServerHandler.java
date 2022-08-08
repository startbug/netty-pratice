package com.ggs.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 说明：
 * 自定义一个Handler需要继承Netty规定的HandlerAdapter(规范)
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据(可以读取客户端发送的消息)


    /**
     * @param ctx 上下文对象，含有管道pipeline,通道channel,地址
     * @param msg 就是客户端发送的数据，默认Object类型
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程 " + Thread.currentThread().getName() + "channel = " + ctx.channel());
        System.out.println("server ctx = " + ctx);
        System.out.println("channel & pipeline");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();  // 本质是一个双向链表，出栈入栈


        // 将msg转换成ByteBuf
        // ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("111111111111111111111" + LocalDateTime.now().format(dtf));

        // ---------------------------------用户程序自定义的普通任务-------------------------------------- begin
        // 加入此时有一个耗时很长的业务 --> 异步执行 --> 提交到该Channel的对应的NIOEventLoop的taskQueue
        // 在taskQueue中的任务，也是同一个线程执行的，所以先执行完任务1才会执行任务2
        ctx.channel().eventLoop().execute(() -> {
            // 任务1
            try {
                TimeUnit.SECONDS.sleep(5);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端-111111111---", CharsetUtil.UTF_8));
                System.out.println("22222222222222222222" + LocalDateTime.now().format(dtf));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("333333333333333333333" + LocalDateTime.now().format(dtf));
        ctx.channel().eventLoop().execute(() -> {
            // 任务2
            try {
                TimeUnit.SECONDS.sleep(10);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端-22222222222---", CharsetUtil.UTF_8));
                System.out.println("444444444444444444444" + LocalDateTime.now().format(dtf));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // ---------------------------------用户程序自定义的普通任务-------------------------------------- end

        System.out.println("555555555555555555555" + LocalDateTime.now().format(dtf));

        // ---------------------------------用户自定义定时任务-------------------------------------- begin
        // 用户自定义定时任务 --> 提交到eventLoop中的scheduledTaskQueue中
        ctx.channel().eventLoop().schedule(() -> {
            // 定时任务
            try {
                TimeUnit.SECONDS.sleep(5);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端---喵捏嘎", CharsetUtil.UTF_8));
                System.out.println("66666666666666666" + LocalDateTime.now().format(dtf));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);

        // ---------------------------------用户自定义定时任务-------------------------------------- end

        System.out.println("7777777777777777777" + LocalDateTime.now().format(dtf));
        System.out.println("客户端发送消息是: " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址: " + ctx.channel().remoteAddress());
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush是write + flushh
        // 将数据写入到缓存，并刷新
        // 一般讲，我们对这个发送得到数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~~", CharsetUtil.UTF_8));
    }

    // 处理异常,一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
