package com.example.caesar.hadoop.sumcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SumCount {

    public static class SumCountMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] split = line.split("\\s+");
            System.out.println(line);
            String send = split[0];
            context.write(new Text(send), one);
        }
    }

    public static class SumCountReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
        private final IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", ",");
        Job job = Job.getInstance(conf, "SumCount");
        job.setJarByClass(SumCount.class);
        job.setMapperClass(SumCountMapper.class);
        job.setCombinerClass(SumCountReducer.class);
        job.setReducerClass(SumCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileSystem.get(conf).delete(new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/java/com/example/caesar/hadoop/sumcount/out"),true);
        FileInputFormat.setInputPaths(job,new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/resources/data.log"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/java/com/example/caesar/hadoop/sumcount/out"));

        final boolean flag = job.waitForCompletion(true);
        System.exit(flag ? 0 : 1);
    }
}
