package com.ggs.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        System.out.println("server ctx = " + ctx);
        // 将msg转换成ByteBuf
        // ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println(LocalDateTime.now().format(dtf));
        // 加入此时有一个耗时很长得到业务 --> 异步执行 --> 提交到该Channel的对应的NIOEventLoop的taskQueue
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端-111111111---", CharsetUtil.UTF_8));
                System.out.println(LocalDateTime.now().format(dtf));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端-22222222222---", CharsetUtil.UTF_8));
                System.out.println(LocalDateTime.now().format(dtf));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


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
