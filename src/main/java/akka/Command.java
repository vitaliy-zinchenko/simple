package akka;

import java.io.Serializable;

/**
 * Created by zinchenko on 17.12.14.
 */
public class Command implements Serializable {

    private final String data;

    public Command(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "Command{" +
                "data='" + data + '\'' +
                '}';
    }
}
