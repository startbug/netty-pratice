package com.ggs.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 获得Selector对象
        Selector selector = Selector.open();

        // 绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把ServerSocketChannel注册到selector上，关注的事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端链接
        while (true) {
            if (selector.select(1000) == 0) {
                // 没有事件发生
                System.out.println("服务器等待1s，无连接");
                continue;
            }

            // 如果返回值>0,就获取到相关的selectionKey集合
            // 1.表示已经获取到关注的事件
            // 2.selector.selectedKeys()返回关注事件的集合
            // 3.通过SelectionKey获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历Set<SelectionKey>
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                // 根据key获取对应的通道，根据事件类型做相应处理
                if (selectionKey.isAcceptable()) { // 如果是OP_ACCEPT，有新的客户端连接
                    ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    // 将Socket注册到Selector中
                    SocketChannel socketChannel = acceptServerSocketChannel.accept();
                    // 设置为非阻塞
                    System.out.println("客户端连接成功，生成socketChannel" + socketChannel);
                    socketChannel.configureBlocking(false);
                    // 绑定Selector，并给该Channel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) { // 发生OP_READ
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取到该Channel关联的Buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    socketChannel.read(buffer);
                    System.out.println("from client:" + new String(buffer.array()));
                }
                // 已经处理完的SelectionKey需要手动移除SelectionKey
                iterator.remove();
            }

        }

    }

}
