package httpfiles;

import httpfiles.impl.FileUploadersFactoryImpl;

import java.io.*;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by zinchenko on 19.11.14.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        UploadService service = new UploadService();
        UploadContext uploadContext = new UploadContext();
        uploadContext.setThreadsNumber(5);
        uploadContext.setDestination("/home/zinchenko/Загрузки/test");
        uploadContext.setSource("/home/zinchenko/work/projects/zinjvi/simple/src/main/resources/httpfiles/file.txt");
        service.upload(uploadContext);

    }

}
