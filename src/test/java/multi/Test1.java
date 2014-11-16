package multi;

import org.junit.Test;

import java.util.Date;

/**
 * Created by zinchenko on 16.11.14.
 */
public class Test1 {

    private int i;

    String s = "";

    @Test
    public void test1() throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("iteration....");
                    System.out.println("start work " + System.currentTimeMillis());
                    for (int i = 0; i < 9999999; i++) {
                        i ++;
                        s = s + "qw" + i;
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
                    i = 0;
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        Thread.sleep(1000);

        thread.interrupt();



    }

}
