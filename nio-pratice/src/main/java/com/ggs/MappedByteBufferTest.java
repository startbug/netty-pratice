package com.ggs;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <b>说明</b>
 * <p>MappedByteBuffer可以让文件直接在内存(对外内存)修改,操作系统不需要拷贝一次</p>
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        // 获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：FileChannel.MapMode.READ_WRITE使用读写模式
         * 参数2：0,可以直接修改的起始位置
         * 参数3：5,是映射到内存的大小,即将1.txt的多少个字节映射到内存
         * 可以直接修改的范围就是0-5(不包括5)
         * 返回值：实际类型是java.nio.DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');

        randomAccessFile.close();
    }

}
