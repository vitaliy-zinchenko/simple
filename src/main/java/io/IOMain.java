package io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

/**
 * Created by zinchenko on 20.11.14.
 */
public class IOMain {
    public static void main(String[] args) throws Exception {




//        t3Client();
        t5();




    }

    public static void p(int b, int n) {
        System.out.println("n = "  + n + " -  " + new String(new int[]{b}, 0, 1) + " - " + b);
    }

    private static void t1() throws Exception {
        //        URL fromUrl = new URL("https://www.google.com.ua");
//        InputStream s = new BufferedInputStream(fromUrl.openStream(), 10);

        InputStream is = IOMain.class.getResourceAsStream("t.txt");
        BufferedInputStream s = new BufferedInputStream(is, 2);

        System.out.println(new Date());


        int n = 0;
        p(s.read(), n);
        s.mark(1);
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);
        System.out.println(s.markSupported());
        s.reset();
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);



//        int n = 0;
//        try {
//            while (true) {
//                p(s.read(), n);
//                n++;
//            }
//        } catch (Exception e) {
//            System.out.println("m ----------> " + n);
//        }
    }

    private static void t2() throws Exception {
        try(ReadableByteChannel src = Channels.newChannel(System.in);
            WritableByteChannel dest = Channels.newChannel(System.out)){

            ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);


//            ByteBuffer buffer2 = ByteBuffer.allocate(16 * 1024);
//            buffer2.


            while (src.read(buffer) != -1) {
                buffer.flip();
                dest.write(buffer);
                buffer.clear();
            }

            System.out.println("e");


//            while (src.read (buffer) != -1) {
//                buffer.flip();
//                dest.write (buffer);
//                buffer.compact();
//            }
//
//            buffer.flip();
//
//            while (buffer.hasRemaining()) {
//                dest.write (buffer);
//            }
        }
    }

    private static void t3Client() throws Exception {
        String host = "echo.websocket.org";
        int port = 8090;

        try (
            Socket socket = new Socket(host, port);
            Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Reader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            writer.write("qqq");

            Thread.sleep(111);

            System.out.println(reader.read());
            System.out.println(reader.read());
            System.out.println(reader.read());
        }
    }


    private static void t4() throws Exception {

        String host = "echo.websocket.org";
        int port = 8090;


        try (
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

        ) {

        }
    }

    private static void t5() throws Exception {
        URL r = IOMain.class.getResource("t5.txt");
        File file = new File(r.getFile());
//        file.delete();
        file.createNewFile();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        int i = 0;
        while(true) {
            System.out.println("Lock whole file");
            FileLock fileLock = fileChannel.lock(0, 1024, true);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(("v" + i).getBytes());
            byteBuffer.flip();

            fileChannel.write(byteBuffer);
            byteBuffer.clear();

            System.out.println("main sleep with lock");
            Thread.sleep(3000);
            System.out.println("main wake up with lock");

            System.out.println("Release lock");
            fileLock.release();

            System.out.println("main sleep");
            Thread.sleep(1000);

            i++;
        }


    }

}
