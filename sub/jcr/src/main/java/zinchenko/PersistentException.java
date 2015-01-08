package zinchenko;

/**
 * Created by zinchenko on 05.01.15.
 */
public class PersistentException extends RuntimeException {

    public PersistentException(String message, Throwable cause) {
        super(message, cause);
    }
}
