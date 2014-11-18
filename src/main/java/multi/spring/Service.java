package multi.spring;

/**
 * Created by zinchenko on 17.11.14.
 */
public class Service {

    private AsyncService asyncService;

    public void m() {
        m2();
    }

    private void m1() {
        System.out.println("call asyncVoid() method");
        asyncService.asyncVoid();
        System.out.println("called asyncVoid() method");

        System.out.println("m end");
    }

    private void m2() {
        System.out.println("call asyncVoidWithException() method");
        asyncService.asyncVoidWithException();
        System.out.println("called asyncVoidWithException() method");

        System.out.println("m end");
    }

    public AsyncService getAsyncService() {
        return asyncService;
    }

    public void setAsyncService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }
}
