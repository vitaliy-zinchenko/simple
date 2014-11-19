package httpfiles.impl;

import httpfiles.FileUploader;
import httpfiles.FileUploadersFactory;

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

    @Override
    public Collection<FileUploader> createFileUploaders(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        String line = reader.readLine();
        Collection<FileUploader> uploaders = new ArrayList<>();
        while(line != null) {
            uploaders.add(buildFileUploader(line));
            line = reader.readLine();
        }
        return uploaders;
    }

    private FileUploader buildFileUploader(String line) {
        String[] tokens = line.split("\\s");
        return new FileUploader(tokens[0], tokens[1]);
    }

}
