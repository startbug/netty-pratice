package com.ggs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author lianghaohui
 * @Date 2022/7/4 13:05
 * @Description
 */
public class NIOFileChannel03 {

    public static void main(String[] args) throws IOException {
        // 往1.txt中填入数据
        FileOutputStream fos = new FileOutputStream("1.txt");
        FileChannel outputChannel = fos.getChannel();
        byte[] bytes = "hello\r\nfffffffofff\r\nstarbug".getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        outputChannel.write(buffer);
        buffer.clear();

        // 将1.txt的数据复制到2.txt中
        FileInputStream fis1 = new FileInputStream("1.txt");
        FileChannel channel1 = fis1.getChannel();
        FileOutputStream fos2 = new FileOutputStream("2.txt");
        FileChannel channel2 = fos2.getChannel();
        while (true) {
            if (channel1.read(buffer) == -1) {
                break;
            }
            buffer.flip();
            channel2.write(buffer);
            buffer.clear();
        }
        fos.close();
        fis1.close();
        fos2.close();
    }

}
