package com.ggs.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author starbug
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个Channel组,管理所有的Channel
    //
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其他在线的客户端
        channelGroup.add(channel);
        /**
         * 该方法会将ChannelGroup中所有的Channel遍历并发送消息
         * 不需要手动遍历
         */
        channelGroup.writeAndFlush(LocalDateTime.now().format(dtf) + "-[客户端]" + channel.remoteAddress() + " 加入聊天\n");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(LocalDateTime.now().format(dtf) + "-[客户端]" + channel.remoteAddress() + " 离开了\n");
        System.out.println("Channel Group size: " + channelGroup.size());
    }

    /**
     * 表示Channel处于活动状态,提示xx上线了
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now().format(dtf) + ctx.channel().remoteAddress() + " 上线了~");
    }

    /**
     * 表示Channel处于不活动状态,提示xx离线了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now().format(dtf) + ctx.channel().remoteAddress() + " 离线了~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (ch != channel) {
                // 如果不是当前的Channel,则转发消息
                ch.writeAndFlush(LocalDateTime.now().format(dtf) + "-[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
            } else {
                // 如果是当前的Channel,则回显自己发送的消息
                ch.writeAndFlush(LocalDateTime.now().format(dtf) + "-[自己]发送了消息" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        System.out.println(LocalDateTime.now().format(dtf) + "-发生异常,关闭通道");
        ctx.close();
    }

}
