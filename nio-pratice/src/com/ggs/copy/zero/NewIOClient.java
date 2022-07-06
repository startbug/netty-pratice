package com.ggs.copy.zero;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7002));

        String fileName = "CinebenchR23.zip";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long start = System.currentTimeMillis();

        // 在linux下一个transferTo方法可以完成传输
        // 在windows下以此调用transferTo只能发送8m，就需要分段传输文件，而且要记录传输时的位置
        // transferTo底层使用了零拷贝
        int perSize = 8 * 1024 * 1024;  // 8m
        int curIndex = 1;
        int curCount;
        long totalSize = fileChannel.size();
        long actualCount = 0;
        while ((curCount = (curIndex - 1) * perSize) < totalSize) {
            long len = fileChannel.transferTo(curCount, perSize, socketChannel);
            actualCount += len;
            curIndex++;
        }

        long end = System.currentTimeMillis();

        System.out.println("文件大小:" + totalSize + "字节,传输了" + curCount + "字节,实际传输了" + curCount + "字节,耗时:" + (end - start));

        fileChannel.close();
    }

}
