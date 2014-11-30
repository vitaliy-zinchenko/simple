package io;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by zinchenko on 21.11.14.
 */
public class BufferMain {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put((byte)'H').put((byte)' ');


        System.out.println("end");
    }
}
