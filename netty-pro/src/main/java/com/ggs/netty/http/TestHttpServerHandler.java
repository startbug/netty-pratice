package com.ggs.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * <p>说明</p>
 * <p>1.SimpleChannelInboundHandler是ChannelInboundHandlerAdapter</p>
 * <p>2.HttpObject客户端和服务器端相互通讯的数据被封装成HttpObject</p>
 **/
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // channelRead0读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg是不是HttpRequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("ctx 类型=" + ctx.getClass());
            System.out.println("pipeline hashcode:" + ctx.pipeline().hashCode() + "----" + "TestHttpServerHandler hashcode:" + this.hashCode());

            System.out.println("msg 类型=" + msg.getClass());
            System.out.println("客户端地址:" + ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico，不做响应");
                return;
            }

            // 回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, I'm server-side啊啊啊啊啊啊啊啊作者", CharsetUtil.UTF_8);

            // 构造一个Http的响应，即HttpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的Response返回
            ctx.writeAndFlush(response);
        }
    }

}
