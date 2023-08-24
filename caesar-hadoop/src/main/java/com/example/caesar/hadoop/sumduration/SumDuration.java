package com.example.caesar.hadoop.sumduration;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SumDuration {
    public static class SumDurationMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] split = line.split("\\s+");
            System.out.println(line);
            String send = split[0];
            String phoneTime = split[3];
            context.write(new Text(send), new Text(phoneTime));
        }
    }

    public static class SumDurationReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            long sendTime = 0;
            try {
                for (Text value : values) {
                    sendTime += Long.parseLong(value.toString());
                }
                context.write(new Text(key), new Text(String.valueOf(sendTime)));
            } catch (NumberFormatException ignored) {}
        }
    }

    public static void main(String[] args) throws Exception {
        final Configuration configuration = new Configuration();
        configuration.set("mapred.textoutputformat.separator", ",");
        final Job job = Job.getInstance(configuration, "SumDuration");
        job.setJarByClass(SumDuration.class);
        job.setMapperClass(SumDurationMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(SumDurationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileSystem.get(configuration).delete(new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/java/com/example/caesar/hadoop/sumduration/out"),true);
        FileInputFormat.setInputPaths(job,new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/resources/data.log"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/java/com/example/caesar/hadoop/sumduration/out"));

        final boolean flag = job.waitForCompletion(true);
        //jvm退出：正常退出0，非0值则是错误退出
        System.exit(flag ? 0 : 1);
    }
}
