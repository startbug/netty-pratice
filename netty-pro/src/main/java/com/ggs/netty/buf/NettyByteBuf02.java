package com.ggs.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuf02 {

    public static void main(String[] args) {
        // 创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!你好", CharsetUtil.UTF_8);

        // 使用相关的方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();

            // 将content转换成字符串
            System.out.println(new String(content, Charset.forName("UTF-8")));

            System.out.println("byteBuf=" + byteBuf);

            System.out.println(byteBuf.arrayOffset());  // 0
            System.out.println(byteBuf.readerIndex());  // 0
            System.out.println(byteBuf.writerIndex());  // 18
            System.out.println(byteBuf.capacity());     // 42

//            System.out.println(byteBuf.readByte());

            int len = byteBuf.readableBytes();  // 可读长度 18

            System.out.println("len=" + len);

            // index: 从index位开始读取
            // length: 从index位开始读取length个字符
            System.out.println(byteBuf.getCharSequence(4, 6, Charset.forName("UTF-8")));

        }

    }

}
