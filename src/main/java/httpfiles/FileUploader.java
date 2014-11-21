package httpfiles;

import java.util.concurrent.Callable;

/**
 * Created by zinchenko on 20.11.14.
 */
public interface FileUploader extends Callable<ItemUploadResult> {
}
