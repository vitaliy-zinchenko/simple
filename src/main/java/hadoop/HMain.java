package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;

/**
 * Created by zinchenko on 06.12.14.
 */
public class HMain {

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);

        Path file = new Path("hdfs:/");

        FileStatus[] fileStatuses = fileSystem.listStatus(file);
        for(FileStatus fileStatus: fileStatuses) {
            System.out.println(fileStatus.getPath().getName());
        }
    }

}
