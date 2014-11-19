package multi;

/**
 * Created by zinchenko on 19.11.14.
 */
public class BadThreads {

//    static String message;

    static Message message = new Message();

    private static class Message {
        private String m;

        public synchronized String getM() {
            return m;
        }

        public synchronized void setM(String m) {
            this.m = m;
        }
    }


    private static class CorrectorThread
            extends Thread {

        public void run() {
            try {
                sleep(2000);
            } catch (InterruptedException e) {}
            // Key statement 1:
            message.setM("Mares do eat oats.");
        }
    }

    public static void main(String args[])
            throws InterruptedException {

        Thread t = new CorrectorThread();
        t.start();

        message.setM("Mares do not eat oats.");
        Thread.sleep(1000);

//        t.join();
        // Key statement 2:
        System.out.println(message.getM());
    }
}
