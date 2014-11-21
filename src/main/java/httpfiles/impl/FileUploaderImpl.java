package httpfiles.impl;

import httpfiles.FileUploader;
import httpfiles.ItemUploadResult;
import httpfiles.UploadContext;
import httpfiles.Utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zinchenko on 19.11.14.
 */
public class FileUploaderImpl implements FileUploader {

    private static final Logger LOGGER = Logger.getLogger(FileUploaderImpl.class.getName());

    private String url;

    private String name;

    private int lineNumber;

    private UploadContext context;

    public FileUploaderImpl() {
    }

    public FileUploaderImpl(UploadContext context, int lineNumber, String url, String name) {
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
            int size = copy(getInputStream(), getOutputStream());
            uploadResult.setSize(size);
            LOGGER.info(MessageFormat.format("The file {0}({1}) uploaded. File size is {2}.", url, name, size));
        } catch (MalformedURLException e) {
            uploadResult.addError(MessageFormat.format("Wrong url {0}.", url));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, MessageFormat.format("Fatal loading file {0}({1}).", url, name), e);
        }
        return uploadResult;
    }

    protected InputStream getInputStream() throws MalformedURLException, IOException {
        URL fromUrl = new URL(url);
        return new BufferedInputStream(fromUrl.openStream());
    }

    protected OutputStream getOutputStream() throws FileNotFoundException {
        String toFile = context.getDestination() + File.separator + name;
        return new BufferedOutputStream(new FileOutputStream(toFile));
    }

    protected int copy(InputStream in, OutputStream out) throws IOException {
        int item = in.read();
        int n = 0;
        while(item != -1) {
            out.write(item);
            item = in.read();
            n++;
        }
        // TODO !!!!!!!!!!!!!!!!!
        out.flush();
        out.close();
        in.close();
        return n;
    }

    protected void validate(ItemUploadResult uploadResult) {
        if(Utils.isBlank(url)) {
            uploadResult.addError("The URL should be specified.");
        }
        if(Utils.isBlank(name)) {
            uploadResult.addError("The file name should be specified.");
        }
    }

}
