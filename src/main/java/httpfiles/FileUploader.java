package httpfiles;

/**
 * Created by zinchenko on 19.11.14.
 */
public class FileUploader implements Runnable {

    private String url;

    private String name;

    public FileUploader(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public void run() {

    }
}
