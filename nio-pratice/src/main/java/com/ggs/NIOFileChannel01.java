package com.ggs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel01 {

    public static void main(String[] args) throws IOException {
        String str = "hello,starbug";
        FileOutputStream fos = new FileOutputStream("E:\\picture\\pixiv\\my.txt");

        // 通过fos获取对应的FileChannel
        // 真实类型是FileChannelImpl
        FileChannel channel = fos.getChannel();

        // 创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将str放入到byteBuffer
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));

        byteBuffer.flip();

        // 将ByteBuffer数据i邪恶入道FileChannel
        channel.write(byteBuffer);

        fos.close();
    }

}
