package com.ggs.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 提供服务端IP和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            // 如果连接不成功
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他事");
            }
        }

        // 连接成功....
        String str = "hello,starbug啊啊";

        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据，将Buffer写入到Channel
        socketChannel.write(byteBuffer);

        System.in.read();
    }

}
