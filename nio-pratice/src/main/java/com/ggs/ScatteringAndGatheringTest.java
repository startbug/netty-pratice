package com.ggs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * <p>Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入(分散)</p>
 * <p>Gathering：从Buffer读取数据时，可以采用buffer数组，依次读取(聚合)</p>
 * <p>测试步骤:</p>
 * <ol>
 *     <li>进入cmd命令行</li>
 *     <li>执行<b>telnet</b>命令</li>
 *     <li>点击<b>Ctrl+]</b>快捷键进入telnet命令行</li>
 *     <li>通过<b>send</b>命令发送消息</li>
 * </ol>
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {
        // 使用ServerSocketChannel和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到Socket中并启动
        serverSocketChannel.bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端链接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;// 假定客户端接受8个字节
        // 循环读取
        while (true) {
            int byteRead = 0;

            while (byteRead < messageLength) {
                long len = socketChannel.read(byteBuffers);
                byteRead += len;
                System.out.println("byteRead = " + byteRead);

                Arrays.asList(byteBuffers).stream().map(buffer -> "position = " + buffer.position() + ", limit = " + buffer.limit()).forEach(System.out::println);

                // 切换读写模式
                Arrays.asList(byteBuffers).forEach(ByteBuffer::flip);
            }

            // 将数据显示在客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long len = socketChannel.write(byteBuffers);
                byteWrite += len;
            }

            // 将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);
            System.out.println("byteRead = " + byteRead + "byteWrite = " + byteWrite);
        }

    }

}
