package jvm;

/**
 * Created by zinchenko on 27.11.14.
 */
public class T2 {

    private static String C = "qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qwe";

//    private static String s = get();

    private static String get() {
        String s = C;
        for (int i = 0; i < 1000; i++) {
            s = s + C;
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println("start");
//        try {
//            Thread.sleep(1000000000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        while(true) {
//            s = s + C;
//            System.out.println(s.length());
        }
    }


}
