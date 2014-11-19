package httpfiles;

/**
 * Created by zinchenko on 19.11.14.
 */
public class Utils {

    public static boolean isBlank(String s) {
        if (s == null) {
            return true;
        }
        String prepared = s.trim();
        if(prepared.isEmpty()) {
            return true;
        }
        return false;
    }

}
