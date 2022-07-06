package com.ggs.copy.zero;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {

    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7002);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(inetSocketAddress);

        ByteBuffer buffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int len;
            int totalSize = 0;

            while ((len = socketChannel.read(buffer)) != -1) {
                totalSize += len;
                buffer.rewind();    // 倒带 position = 0, mark作废
            }

            System.out.println(totalSize);
        }

    }

}
