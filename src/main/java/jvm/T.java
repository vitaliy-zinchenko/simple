package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zinchenko on 15.11.14.
 */
public class T {

    private static final String C = "qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qweqwe qweqweqwe qwe qwe qwe";

//    List<Cl> list = new ArrayList<>(2000000000);

    private static final List l = new ArrayList();

    public static void main(String[] args) throws InterruptedException {
        new T().go();
    }

    public void go() throws InterruptedException {
        System.out.println("SSSS");

        for (long i = 0; i < 2000000000; i++) {
//            list.add(new Cl(C+i, i));
            String s = new String(C + i + System.currentTimeMillis());

            l.add(s.intern());
            System.out.println(i);
            Thread.sleep(5);
        }


        Thread.sleep(111111L);
    }

//    class Cl {
//        Cl(String n, Long age) {
//            this.n = n;
//            this.age = age;
//        }
//
//        String n;
//        Long age;
//    }

}
