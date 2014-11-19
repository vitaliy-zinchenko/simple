package httpfiles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by zinchenko on 19.11.14.
 */
public class Main {

    public static void main(String[] args) {
        InputStream stream = Main.class.getResourceAsStream("file.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));




//        reader.readLine();
    }

}
