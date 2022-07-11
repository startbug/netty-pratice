package com.ggs.copy.old;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class OldIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 7001);

        String fileName = "CinebenchR23.zip";

        InputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[4096];
        long readCount;
        long total = 0;

        long start = System.currentTimeMillis();

        while ((readCount = inputStream.read(bytes)) != -1) {
            total += readCount;
            dataOutputStream.write(bytes);
        }

        long end = System.currentTimeMillis();

        System.out.println("发送总字节数:" + total + ",耗时:" + (end - start));

        dataOutputStream.close();
        inputStream.close();
        socket.close();

    }

}
