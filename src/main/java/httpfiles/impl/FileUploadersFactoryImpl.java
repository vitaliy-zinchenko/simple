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

    public static final String SPLIT = "\\s";

    @Override
    public Collection<FileUploader> createFileUploaders(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        String line = reader.readLine();
        Collection<FileUploader> uploaders = new ArrayList<>();
        for (int i = 0; line != null; i++) {
            uploaders.add(buildFileUploader(i+1, line));
            line = reader.readLine();
        }
        return uploaders;
    }

    private FileUploader buildFileUploader(int number, String line) {
        String[] tokens = line.split(SPLIT);
        String filename = tokens.length > 1 ? tokens[1] : null;
        return new FileUploader(number, tokens[0], filename);
    }

}
