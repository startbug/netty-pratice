package com.ggs.netty.codec2;

import com.ggs.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 说明：
 * 自定义一个Handler需要继承Netty规定的HandlerAdapter(规范)
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    // 读取数据(可以读取客户端发送的消息)


    /**
     * @param ctx 上下文对象，含有管道pipeline,通道channel,地址
     * @param msg 就是客户端发送的数据，默认Object类型
     * 实现 ChannelInboundHandlerAdapter 类
     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        // 读取从客户端发送的StudentPOJO.Studnet
//        StudentPOJO.Student student = (StudentPOJO.Student) msg;
//
//        System.out.println("客户端发送的数据 id = " + student.getId() + " name = " + student.getName());
//    }


    /**
     * 实现 SimpleChannelInboundHandler<StudentPOJO.Student> 抽象类
     **/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage message) throws Exception {
        // 根据dataType来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = message.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = message.getStudent();
            System.out.println("客户端发送的数据 学生信息: id = " + student.getId() + " name = " + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = message.getWorker();
            System.out.println("客户端发送的数据 打工人信息: age = " + worker.getAge() + " name = " + worker.getName());
        } else {
            System.out.println("传输数据类型不正确");
        }
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
