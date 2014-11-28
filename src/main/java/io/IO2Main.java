package io;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zinchenko on 26.11.14.
 */
public class IO2Main {
    public static void main(String[] args) {
        Thread t = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        URL r = IOMain.class.getResource("t5.txt");
                        File file = new File(r.getFile());
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                        FileChannel channel = randomAccessFile.getChannel();

                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        byteBuffer.clear();

                        System.out.println("sub try to read");
                        channel.read(byteBuffer);

                        System.out.println(byteBuffer.toString());


                        System.out.println("Sleep sub");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
}
