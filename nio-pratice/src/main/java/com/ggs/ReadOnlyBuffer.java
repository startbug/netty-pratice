package com.ggs;

import java.nio.ByteBuffer;

/**
 * @Author lianghaohui
 * @Date 2022/7/4 13:30
 * @Description
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            buffer.put((byte) i);
        }

        buffer.flip();
        System.out.println(buffer.getClass());

        // 得到一个只读的buffer
        ByteBuffer byteBuffer = buffer.asReadOnlyBuffer();
        System.out.println(byteBuffer.getClass());

        // 读取
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }

        // 写入 java.nio.ReadOnlyBufferException
//        byteBuffer.put("xx".getBytes());

    }

}
