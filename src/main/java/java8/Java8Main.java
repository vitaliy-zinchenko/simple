package java8;

/**
 * Created by zinchenko on 30.11.14.
 */
public class Java8Main {

    interface Printer {
        void print(String s);
    }

    class PrinterImpl implements Printer {
        @Override
        public void print(String s) {
            System.out.println(s);
        }
    }

    public static void p(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
        String str = "sss";

        print(s -> {
            System.out.println(s);
        }, str);

        System.out.println("~~~~~~~~~~");

        print(Java8Main::p, str);

        System.out.println("~~~~~~~~~~");


    }

    public static void print(Printer printer, String s) {
        printer.print(s);
    }
}
