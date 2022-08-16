package com.ggs.netty.codec2;

import com.ggs.netty.codec.StudentPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    // 当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送Student或者Worker对象
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        switch (random) {
            case 0: {
                myMessage = MyDataInfo.MyMessage
                        .newBuilder()
                        .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                        .setStudent(MyDataInfo.Student.newBuilder().setId(20).setName("赛博顶真").build()).build();
                break;
            }
            case 1: {
                myMessage = MyDataInfo.MyMessage
                        .newBuilder()
                        .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                        .setWorker(MyDataInfo.Worker.newBuilder().setAge(100).setName("赛博板砖").build()).build();
                break;
            }
        }
        ctx.writeAndFlush(myMessage);
    }

    // 当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址:" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
