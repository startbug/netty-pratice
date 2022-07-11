package com.ggs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Author lianghaohui
 * @Date 2022/7/4 13:22
 * @Description 图片拷贝
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("nio-pratice/img/Selector、Channel和Buffer的关系.png");
        FileOutputStream fos = new FileOutputStream("xxx.png");

        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        inChannel.transferTo(0, inChannel.size(), outChannel);

        // 关闭通道
        inChannel.close();
        outChannel.close();
        fis.close();
        fos.close();
    }

}
