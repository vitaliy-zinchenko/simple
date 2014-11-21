package httpfiles;

/**
 * Created by zinchenko on 19.11.14.
 */
public class UploadResult {

    private int successCount;

    private int bytes;

    private int totalSize;

    public void incrementCount() {
        successCount++;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public void addTotalSize(int size) {
        this.totalSize += size;
    }
}
