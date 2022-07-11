package com.ggs;

import java.nio.ByteBuffer;

/**
 * @Author lianghaohui
 * @Date 2022/7/4 13:27
 * @Description 存入指定格式的数据，就按照指定格式去获取
 */
public class NIOByteBufferPutGet {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.putInt(10);
        buffer.putLong(20);
        buffer.putChar('啊');
        buffer.putShort(Short.valueOf("10"));

        buffer.flip();

        System.out.println();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());

        // 错误 java.nio.BufferUnderflowException
//        System.out.println(buffer.getInt());
//        System.out.println(buffer.getLong());
//        System.out.println(buffer.getChar());
//        System.out.println(buffer.getLong());

    }

}
