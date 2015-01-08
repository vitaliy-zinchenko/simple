package java8;

import java.util.Comparator;

/**
 * Created by zinchenko on 30.11.14.
 */
public class Java8MainLambda {

    interface Interface01 {
        public void run();
    }

    interface Interface02 {
        public String run();
    }

    interface Interface03 {
        public void run(String s, Integer i);
    }

    interface Interface04 {
        public void run(String s);
    }

    interface Interface05 {
        public void run(String s);
        default public void run2(String s) {
            System.out.println("def " + s);
        };
    }

    public static void main(String[] args) {
        String str = "as";

        runner01(new Interface01() {
            @Override
            public void run() {
                System.out.println(str);
            }
        });

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");

        runner01(() -> {
            System.out.println(str);
            System.out.println("r01");
        });

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");

        runner02(() -> {
            System.out.println("r02");
            return "rrr";
        });

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");

        runner03((s, i) -> {
            System.out.println("s=" + s + " i=" + i);
        });
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");

        runner04(p -> {
            System.out.println(p);
        });
        runner04((p) -> {
            System.out.println(p);
        });
        runner04((String p) -> {
            System.out.println(p);
        });

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");

        runner05((String p) -> {
            System.out.println(p);
        });

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");


    }

    private static void runner01(Interface01 interface01) {
        interface01.run();
    }

    private static void runner02(Interface02 interface02) {
        String r = interface02.run();
        System.out.println(r);
    }

    private static void runner03(Interface03 interface03) {
        interface03.run("p1", 2);
    }

    private static void runner04(Interface04 interface04) {
        interface04.run("p4");
    }

    private static void runner05(Interface05 interface05) {
        interface05.run("p5");
        interface05.run2("p52");
    }

}
