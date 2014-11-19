package httpfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created by zinchenko on 19.11.14.
 */
public interface FileUploadersFactory {

    Collection<FileUploader> createFileUploaders(InputStream source) throws IOException;

}
