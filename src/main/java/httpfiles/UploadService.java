package httpfiles;

import httpfiles.impl.FileUploaderImpl;
import httpfiles.impl.FileUploadersFactoryImpl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zinchenko on 19.11.14.
 */
public class UploadService {

    private static final Logger LOGGER = Logger.getLogger(UploadService.class.getName());

    FileUploadersFactory factory;

    public UploadService(UploadContext uploadContext) {
        factory = new FileUploadersFactoryImpl(uploadContext);
    }

    public void upload(UploadContext context) {
        try {
            InputStream stream = new FileInputStream(context.getSource());

            Collection<FileUploaderImpl> fileUploaders = factory.createFileUploaders(stream);

            ExecutorService executorService = Executors.newFixedThreadPool(context.getThreadsNumber());
            List<Future<ItemUploadResult>> resultFutures = executorService.invokeAll(fileUploaders);

            UploadResult uploadResult = extractResult(resultFutures);

            LOGGER.info(MessageFormat.format("Finished. {0} file(s) was uploaded. {1} bytes was uploaded.",
                    uploadResult.getSuccessCount(), uploadResult.getTotalSize()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while uploading files", e);
        }
    }

    private UploadResult extractResult(List<Future<ItemUploadResult>> resultFutures) {
        UploadResult uploadResult = new UploadResult();
        for(Future<ItemUploadResult> future: resultFutures) {
            ItemUploadResult result = null;
            try {
                result = future.get();
                if (result.isSuccess()) {
                    uploadResult.incrementCount();
                    uploadResult.addTotalSize(result.getSize());
                    continue;
                }
                logErrors(result);
            } catch (Exception e) {
                result.addError("Unexpected error: " + e.getMessage());
            }
        }
        return uploadResult;
    }

    private void logErrors(ItemUploadResult result) {
        for(String error: result.getErrors()) {
            LOGGER.info(MessageFormat.format(" * Error in line {0}: {1}", result.getLineNumber(), error));
        }
    }

}
