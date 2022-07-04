package com.ggs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author lianghaohui
 * @Date 2022/7/4 13:01
 * @Description
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws IOException {
        File file = new File("my.txt");
        FileInputStream fis = new FileInputStream(file);

        FileChannel channel = fis.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        channel.read(buffer);

        System.out.println(new String(buffer.array()));
        fis.close();
    }

}
