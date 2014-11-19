package httpfiles;

/**
 * Created by zinchenko on 19.11.14.
 */
public class UploadContext {

    private String destination;

    private String source;

    private int ThreadsNumber;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getThreadsNumber() {
        return ThreadsNumber;
    }

    public void setThreadsNumber(int threadsNumber) {
        this.ThreadsNumber = threadsNumber;
    }
}
