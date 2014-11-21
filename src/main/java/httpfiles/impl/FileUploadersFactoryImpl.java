package httpfiles.impl;

import httpfiles.FileUploader;
import httpfiles.FileUploadersFactory;
import httpfiles.UploadContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zinchenko on 19.11.14.
 */
public class FileUploadersFactoryImpl implements FileUploadersFactory {

    public static final String SPLIT = "\\s";

    private UploadContext uploadContext;

    public FileUploadersFactoryImpl(UploadContext uploadContext) {
        this.uploadContext = uploadContext;
    }

    @Override
    public Collection<FileUploaderImpl> createFileUploaders(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        String line = reader.readLine();
        Collection<FileUploaderImpl> uploaders = new ArrayList<>();
        for (int i = 0; line != null; i++) {
            uploaders.add(buildFileUploader(i+1, line));
            line = reader.readLine();
        }
        return uploaders;
    }

    protected FileUploaderImpl buildFileUploader(int number, String line) {
        String[] tokens = line.split(SPLIT);
        String filename = tokens.length > 1 ? tokens[1] : null;
        return new FileUploaderImpl(uploadContext, number, tokens[0], filename);
    }

}
