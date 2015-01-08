package zinchenko;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by zinchenko on 14.12.14.
 */
public class MyJob {

    public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println("~~~~~~~~~~~~ start map: " + key + ", " + value);

            String line = value.toString();
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
        f("");

        System.out.println("~~~~~~~~~~~~ start main ");

        Configuration configuration = new Configuration();

        Job job = new Job(configuration, "MyJob");

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

        System.out.println("~~~~~~~~~~~~ end main ");
    }

    private static void f(String s) {
        System.out.println("f1");
    }

    private static void f(String... s) {
        System.out.println("f2");
    }

}
