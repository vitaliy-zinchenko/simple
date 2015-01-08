package multi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zinchenko on 02.12.14.
 */
public class CollectionTest {
    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    static class Test1 implements Runnable {
        Test1(List<Object> list, long start) {
            this.list = list;
            this.start = start;
        }

        List<Object> list;
        long start;
        @Override
        public void run() {
            while(true) {
                long time = System.currentTimeMillis() - start;
                if(time >= 1000) {
                    break;
                }
                list.add(Thread.currentThread().getName());
            }
        }


    }

    public static void test1() throws InterruptedException {
        List<Object> list = Collections.synchronizedList(new ArrayList<>());

        long start = System.currentTimeMillis();

//        AtomicLong readCount =

        Thread t1 = new Thread(new Test1(list, start));
        Thread t2 = new Thread(new Test1(list, start));
        Thread t3 = new Thread(new Test1(list, start));
        Thread t4 = new Thread(new Test1(list, start));
        Thread t5 = new Thread(new Test1(list, start));
        Thread t6 = new Thread(new Test1(list, start));
        Thread t7 = new Thread(new Test1(list, start));
        Thread t8 = new Thread(new Test1(list, start));
        Thread t9 = new Thread(new Test1(list, start));
        Thread t10 = new Thread(new Test1(list, start));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
        t8.join();
        t9.join();
        t10.join();

        System.out.println("result="+list.size());
    }
}
