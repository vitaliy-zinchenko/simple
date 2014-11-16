package multi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;

/**
 * Created by zinchenko on 16.11.14.
 */
public class MultiTest1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        t5();

    }

    static void t1 () throws InterruptedException {
        final String[] s = {""};

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("iteration....");
                    System.out.println("start work " + System.currentTimeMillis());
                    for (int i = 0; i < 49999; i++) {
                        i ++;
                        s[0] = s[0] + "qw" + i;
                    }
                    System.out.println("end work " + System.currentTimeMillis());
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("go out");
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("interruption !!!");
                        return;
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        System.out.println("main sleep");
        Thread.sleep(1000);

        System.out.println("main interrupt!!");
        thread.interrupt();
        System.out.println("main interrupter!!");
    }

    static void t2 (){

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("started sub " + Thread.currentThread().getName());
                    for (int j = 0; j < 3; j++) {
                        System.out.println("("+j+") sub " + Thread.currentThread().getName());
                    }
                }
            });

            System.out.println("("+i+")run " + thread.getName());
            thread.start();
            Thread.yield();
        }

        System.out.println("end main");

    }

    static void t3() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("sub");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end sub");
            }
        });
        System.out.println("start sub");
        thread.start();

        System.out.println("main sleep");
        Thread.sleep(1000);

        thread.join();
        System.out.println("end main");
    }

    static void  t4() throws InterruptedException, ExecutionException {
        class Task implements Callable<String> {
            private String s;

            Task(String s) {
                this.s = s;
            }

            @Override
            public String call() throws Exception {
                System.out.println("call " + s);
                return Thread.currentThread().getName() + " " + s;
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Task task0 = new Task("f0");
        Future<String> stringFuture0 = executorService.submit(task0);

        Task task00 = new Task("f00");
        Future<String> stringFuture00 = executorService.submit(task00);

        Task task1 = new Task("f");
        Future<String> stringFuture1 = executorService.submit(task1);

        System.out.println("main sleep");
        Thread.sleep(1000);

        Task task2 = new Task("s");
        Future<String> stringFuture2 = executorService.submit(task2);

        System.out.println("get results");
        System.out.println(stringFuture1.get());
        System.out.println(stringFuture2.get());
    }

    static void t5() throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(20);

        class Task implements Callable<String> {

            private int i;

            Task(int i) {
                this.i = i;
            }

            @Override
            public String call() throws Exception {
                System.out.println("call " + i + " " + Thread.currentThread().getName());
                done.countDown();
                return Thread.currentThread().getName();
            }
        }

        Collection<Callable<String>> callables = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            callables.add(new Task(i));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.invokeAll(callables);

        System.out.println("main wait");
        done.await();

        System.out.println("end");
    }

}
