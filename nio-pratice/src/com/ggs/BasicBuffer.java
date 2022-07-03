package com.ggs;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        // 创建Buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 存入数据
//        intBuffer.put(1);
//        intBuffer.put(12);
//        intBuffer.put(11);
//        intBuffer.put(31);
//        intBuffer.put(41);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 读写切换
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

    }

}
