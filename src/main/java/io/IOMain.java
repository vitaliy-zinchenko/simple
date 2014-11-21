package io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by zinchenko on 20.11.14.
 */
public class IOMain {
    public static void main(String[] args) throws IOException {
//        URL fromUrl = new URL("https://www.google.com.ua");
//        InputStream s = new BufferedInputStream(fromUrl.openStream(), 10);

        InputStream is = IOMain.class.getResourceAsStream("t.txt");
        BufferedInputStream s = new BufferedInputStream(is, 2);

        System.out.println(new Date());


        int n = 0;
        p(s.read(), n);
        s.mark(1);
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);
        System.out.println(s.markSupported());
        s.reset();
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);
        p(s.read(), n);



//        int n = 0;
//        try {
//            while (true) {
//                p(s.read(), n);
//                n++;
//            }
//        } catch (Exception e) {
//            System.out.println("m ----------> " + n);
//        }
    }

    public static void p(int b, int n) {
        System.out.println("n = "  + n + " -  " + new String(new int[]{b}, 0, 1) + " - " + b);
    }
}
