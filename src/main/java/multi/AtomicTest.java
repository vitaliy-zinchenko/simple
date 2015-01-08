package multi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zinchenko on 02.12.14.
 */
public class AtomicTest {
    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    static class Test1 implements Runnable {
        Test1(AtomicLong atomicLong, long start) {
            this.atomicLong = atomicLong;
            this.start = start;
        }

        AtomicLong atomicLong;
        long start;
        @Override
        public void run() {
            while(true) {
                long time = System.currentTimeMillis() - start;
                if(time >= 1000) {
                    break;
                }
                atomicLong.incrementAndGet();
            }
        }


    }

    public static void test1() throws InterruptedException {

        long start = System.currentTimeMillis();
        AtomicLong readCount = new AtomicLong(0);

        Thread t1 = new Thread(new Test1(readCount, start));
        Thread t2 = new Thread(new Test1(readCount, start));
        Thread t3 = new Thread(new Test1(readCount, start));
        Thread t4 = new Thread(new Test1(readCount, start));
        Thread t5 = new Thread(new Test1(readCount, start));
        Thread t6 = new Thread(new Test1(readCount, start));
        Thread t7 = new Thread(new Test1(readCount, start));
        Thread t8 = new Thread(new Test1(readCount, start));
        Thread t9 = new Thread(new Test1(readCount, start));
        Thread t10 = new Thread(new Test1(readCount, start));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
//        t5.start();
//        t6.start();
//        t7.start();
//        t8.start();
//        t9.start();
//        t10.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
//        t5.join();
//        t6.join();
//        t7.join();
//        t8.join();
//        t9.join();
//        t10.join();

        System.out.println("result="+readCount.get());
    }
}
