package zinchenko;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by zinchenko on 14.12.14.
 */
public class CustomJob {

    static class MyRecordReader extends RecordReader<String, StringWritable> {

        @Override
        public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {

        }

        @Override
        public boolean nextKeyValue() throws IOException, InterruptedException {
            return false;
        }

        @Override
        public String getCurrentKey() throws IOException, InterruptedException {
            return null;
        }

        @Override
        public StringWritable getCurrentValue() throws IOException, InterruptedException {
            return null;
        }

        @Override
        public float getProgress() throws IOException, InterruptedException {
            return 0;
        }

        @Override
        public void close() throws IOException {

        }
    }

    static class MyInputSplit extends InputSplit {

        private String[] content;

        MyInputSplit(String content) {
            this.content = content.split(",");
        }

        @Override
        public long getLength() throws IOException, InterruptedException {
            return content.length;
        }

        @Override
        public String[] getLocations() throws IOException, InterruptedException {
            return new String[0];
        }

        public String[] getContent() {
            return content;
        }

        public void setContent(String[] content) {
            this.content = content;
        }
    }

    static class StringInputFormat extends InputFormat<String, StringWritable> {

        @Override
        public List<InputSplit> getSplits(JobContext context) throws IOException, InterruptedException {
            List<InputSplit> inputSplits = new ArrayList<InputSplit>();

            inputSplits.add(new MyInputSplit(context.getConfiguration().get("str")));
            return inputSplits;
        }

        @Override
        public RecordReader<String, StringWritable> createRecordReader(InputSplit split,
                 TaskAttemptContext context) throws IOException, InterruptedException {
            RecordReader<String, StringWritable> recordReader = new MyRecordReader();

            return recordReader;
        }
    }

    public class StringWritable implements WritableComparable<StringWritable> {

        private String field;

        @Override
        public int compareTo(StringWritable o) {
            return field.compareTo(o.field);
        }

        @Override
        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeUTF(field);
        }

        @Override
        public void readFields(DataInput dataInput) throws IOException {
            field = dataInput.readUTF();
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    public static class MyMapper extends Mapper<LongWritable, StringWritable, Text, IntWritable> {

        @Override
        protected void map(LongWritable key, StringWritable value, Context context) throws IOException, InterruptedException {
            System.out.println("~~~~~~~~~~~~ start map: " + key + ", " + value);

            String line = value.getField();//.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while(tokenizer.hasMoreTokens()) {
                Text word = new Text(tokenizer.nextToken());
                context.write(word, new IntWritable(1));
                System.out.println("~~~~~~~~~~~~ write map: " + word);
            }

            System.out.println("~~~~~~~~~~~~ end map ");
        }
    }

    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        MyReducer() {
        }

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            System.out.println("~~~~~~~~~~~~ start reducer: " + key);

            int sum = 0;
            for(IntWritable val: values) {
                sum += val.get();
                System.out.println("     ~~~~~~~ value: " + val);
            }
            context.write(key, new IntWritable(sum));

            System.out.println("~~~~~~~~~~~~ end reducer: sum=" + sum);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("~~~~~~~~~~~~ start main ");

        Configuration configuration = new Configuration();

        configuration.set("str", "asdf,asdd,asds");

        Job job = new Job(configuration, "MyJob");

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(StringInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

//        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

        System.out.println("~~~~~~~~~~~~ end main ");
    }

}
