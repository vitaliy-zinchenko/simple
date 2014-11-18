package multi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;

/**
 * Created by zinchenko on 16.11.14.
 */
public class MultiTest1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        t11();

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

    static void t6() {
        class Task6 implements Callable<Boolean> {

            private String arg;

            Task6(String arg) {
                this.arg = arg;
            }

            @Override
            public Boolean call() throws Exception {
                if("f".equals(arg)) {
                    System.out.println("going to make fail");
                    throw new RuntimeException("BA-BAH!!");
                }

                if("e".equals(arg)) {
                    return false;
                }

                return true;
            }
        }


        ExecutorService service = Executors.newFixedThreadPool(2);

        System.out.println("submit");
        Future<Boolean> future = service.submit(new Task6("f"));

        try {
            System.out.println("going to get");
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("EEEE");
            e.printStackTrace();
        }

    }

    static public class MySingleton {

        private static MySingleton instance;

        public static MySingleton getInstance() {

            if (instance == null) {
                synchronized (MySingleton.class) {
                    if (instance == null)
                        instance = new MySingleton();
                }
            }
            return instance;
        }
    }

    public static volatile Integer volatileInt = 0;

    private static void t10() throws InterruptedException {
        class Task10 implements Runnable {

//            private volatile Integer i = 0;

            @Override
            public void run() {
                MySingleton s = MySingleton.getInstance();
                System.out.println(s);

            }
//                p();
//
//                volatileInt++;
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                p();
//            }

            public void p() {
                System.out.println(Thread.currentThread().getName() + " - i is " + volatileInt);
            }
        }

        Task10 task1 = new Task10();
//        task1.p();
        Task10 task2 = new Task10();
//        task2.p();

        System.out.println("start1");
        Thread thread1 = new Thread(task1);
        thread1.start();

        System.out.println("start2");
        Thread thread2 = new Thread(task2);
        thread2.start();

        thread1.join();
        thread2.join();

//        task1.p();
//        task2.p();

    }

    private static volatile int volatileIntT11 = 0;

    private static void t11() {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                int v1 = volatileIntT11;
                while (true) {
                    if(v1 != volatileIntT11) {
                        System.out.println("volatileIntT11 from t1 = " + volatileIntT11);
                        v1 = volatileIntT11;
                    }
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                int v2 = volatileIntT11;
                while(true) {
                    ++volatileIntT11;
                    System.out.println("incremented = " + volatileIntT11);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println();
                }
            }
        };

        t1.start();
        t2.start();
    }

}
