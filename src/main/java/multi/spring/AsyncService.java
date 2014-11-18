package multi.spring;

import org.springframework.scheduling.annotation.Async;

/**
 * Created by zinchenko on 17.11.14.
 */
public class AsyncService {

    @Async
    public void asyncVoid() {
        System.out.println("start doing some long work");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("finished doing some long work");
    }

    @Async
    public void asyncVoidWithException() {
        System.out.println("start doing some long work");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (true) {
            throw new RuntimeException();
        }
        System.out.println("finished doing some long work");
    }

}
