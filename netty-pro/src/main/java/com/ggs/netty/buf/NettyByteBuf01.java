package com.ggs.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * <p>说明:</p>
 * <p>1.创建对象,该对象包含一个数组arr,是一个byte[10]</p>
 * <p>2.在netty的buffer中,不需要使用flip进行反转</p>
 * <p>  在底层维护了readerIndex和writerIndex</p>
 * <p>3.通过readerIndex、writerIndex和capacity,将buffer分成三个区域</p>
 * <p> 0 --> readerIndex: 已读取区域</p>
 * <p> readerIndex --> writerIndex: 可读取区域</p>
 * <p> writerIndex --> capacity: 可写区域</p>
 **/
public class NettyByteBuf01 {

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity=" + buffer.capacity());

//        for (int i = 0; i < buffer.capacity(); i++) {
//            System.out.println(buffer.getByte(i));  // 这种方式不会改变readIndex
//        }

        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte()); // 这种方式会改变readIndex
        }

    }

}
