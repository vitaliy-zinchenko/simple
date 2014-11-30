package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zinchenko on 23.11.14.
 */
public class NIOMain {

    public static void main(String[] args) throws Exception {


        t1();


    }

    private static void t1() throws Exception {
        URL url = NIOMain.class.getResource("nio_t1.txt");
        try (
//            FileOutputStream fileOutputStream = new FileOutputStream(url.getFile());
            RandomAccessFile randomAccessFile = new RandomAccessFile(url.getFile(), "rw");
//            FileChannel fileChannel = fileOutputStream.getChannel()
            FileChannel fileChannel = randomAccessFile.getChannel()
        ) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("str".getBytes("UTF-8"));
            byteBuffer.flip();

            fileChannel.write(byteBuffer);

            fileChannel.position(10);

            byteBuffer.flip();

            fileChannel.write(byteBuffer);



        }
    }

}
