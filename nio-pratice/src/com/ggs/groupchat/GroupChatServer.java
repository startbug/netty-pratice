package com.ggs.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 将channel注册到selector中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 监听
    public void listen() {
        System.out.println("监听线程 " + Thread.currentThread().getName());
        while (true) {
            try {
                int count = selector.select(2000);
                if (count > 0) {
                    // 有事件需要处理
                    // 遍历得到SelectKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        // 获取selectionKey
                        SelectionKey key = iterator.next();

                        // 监听accept事件
                        if (key.isAcceptable()) {
                            // 从channel中获取当前的channel
                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                            // 当前是接受连接的事件，直接获取SocketChannel
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            // 将sc注册到selector中,监听读取事件
                            sc.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }

                        // 监听read事件
                        if (key.isReadable()) {
                            // 处理读取
                            readData(key);
                        }

                        // 移除key，防止重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 异常处理
            }

        }
    }

    // 读取客户端消息
    private void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            // 得到Channel
            channel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);

            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("from 客户端" + msg);
                // 向其他客户端转发消息(排除自己)
                sendInfoToOtherClients(msg, channel);
            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了... ");
                // 取消注册
                key.channel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    // 转发消息给其他客户(通道)
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        System.out.println("服务器转发数据给线程：" + Thread.currentThread().getName());

        for (SelectionKey key : selector.keys()) {
            // 取出通道
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer gcs = new GroupChatServer();

        gcs.listen();
    }

}
