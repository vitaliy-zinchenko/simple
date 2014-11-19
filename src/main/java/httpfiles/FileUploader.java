package httpfiles;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * Created by zinchenko on 19.11.14.
 */
public class FileUploader implements Callable<ItemUploadResult> {

    private String url;

    private String name;

    private int lineNumber;

    private UploadContext context;

    public FileUploader() {
    }

    public FileUploader(UploadContext context, int lineNumber, String url, String name) {
        this.context = context;
        this.lineNumber = lineNumber;
        this.url = url;
        this.name = name;
    }

    @Override
    public ItemUploadResult call() throws Exception {
        ItemUploadResult uploadResult = new ItemUploadResult(lineNumber);

        validate(uploadResult);

        if(!uploadResult.isSuccess()) {
            return uploadResult;
        }

        try {
            File file = new File(url);
            InputStream inputStream = new BufferedInputStream(
                    new FileInputStream(file));
            OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(context.getDestination()));

            inputStream.
            outputStream.write();
        } catch (Exception e) {
            System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        }

        System.out.println("upload - " + url + " - " + name);
        return uploadResult;
    }

    private void validate(ItemUploadResult uploadResult) {
        if(Utils.isBlank(url)) {
            uploadResult.addError("The URL should be specified.");
        }
        if(Utils.isBlank(name)) {
            uploadResult.addError("The file name should be specified.");
        }
    }

}
