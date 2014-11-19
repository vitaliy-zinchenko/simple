package httpfiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zinchenko on 19.11.14.
 */
public class ItemUploadResult {

    private List<String> errors = new ArrayList<>();

    private int lineNumber;

    public ItemUploadResult(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
    }

}
