package com.example.caesar.hadoop.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Partition {
    /**
     * Define a pair of integers that are writable.
     * They are serialized in a byte comparable format.
     */
    public static class CallInfo implements WritableComparable<CallInfo> {

        private long callNumber = 0;
        private long callNumber2 = 0;
        private int callDuration = 0;

        /**
         * Set the left and right values.
         */
        public void set(long num1, long num2, int duration) {
            callNumber = num1;
            callNumber2 = num2;
            callDuration = duration;
        }

        public long getCallNumber() {
            return callNumber;
        }

        public long getCallNumber2() {
            return callNumber2;
        }

        public int getCallDuration() {
            return callDuration;
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeLong(callNumber);
            out.writeLong(callNumber2);
            out.writeInt(callDuration);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            callNumber = in.readLong();
            callNumber2 = in.readLong();
            callDuration = in.readInt();
        }


        @Override
        public int compareTo(CallInfo o) {
            if (o == null) {
                throw new RuntimeException();
            }

            if (this.getCallNumber() == o.getCallNumber()) {
                return o.getCallDuration() - this.getCallDuration();
            }

            return o.getCallNumber() - this.getCallNumber() > 0 ? -1:1;
        }
    }

    /**
     * Compare only the first part of the pair, so that reduce is called once
     * for each value of the first part.
     */
    public static class CallInfoGroupingComparator extends WritableComparator {
        protected CallInfoGroupingComparator() {
            super(CallInfo.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            CallInfo ca = (CallInfo) a;
            CallInfo cb = (CallInfo) b;

            return Long.compare(ca.getCallNumber(), cb.getCallNumber());
        }
    }

    public static class MapClass
            extends Mapper<LongWritable, Text, CallInfo, IntWritable> {

        private final CallInfo key = new CallInfo();
        private final IntWritable value = new IntWritable();

        @Override
        public void map(LongWritable inKey, Text inValue, Context context) {
            String line = inValue.toString();
            String[] split = line.split("\\s+");
            System.out.println(line);
            try {
                long send = Long.parseLong(split[0]);
                long receive = Long.parseLong(split[1]);
                int callDuration = Integer.parseInt(split[3]);
                key.set(send, receive, callDuration);
                value.set(callDuration);
                context.write(key, value);

                key.set(receive, send, callDuration);
                value.set(callDuration);
                context.write(key, value);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public static class Reduce
            extends Reducer<CallInfo, IntWritable, Text, Text> {
        private static final Text SEPARATOR =
                new Text("号码,通话号码,通话时长");
        private MultipleOutputs<Text, Text> multipleOutputs;

        @Override
        protected void setup(Context context) {
            multipleOutputs = new MultipleOutputs<>(context);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            multipleOutputs.close();
        }

        @Override
        public void reduce(CallInfo key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            multipleOutputs.write(SEPARATOR, null, key.getCallNumber()+"");

            for(IntWritable value: values) {
                multipleOutputs.write(new Text(key.getCallNumber() + "," + key.getCallNumber2()), new Text(value.toString()), key.getCallNumber()+"");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", ",");

        Job job = Job.getInstance(conf, "CallPartition");
        job.setJarByClass(Partition.class);
        job.setMapperClass(MapClass.class);
        job.setReducerClass(Reduce.class);

        // group and partition by the first int in the pair
        job.setGroupingComparatorClass(CallInfoGroupingComparator.class);

        // the map output is IntPair, IntWritable
        job.setMapOutputKeyClass(CallInfo.class);
        job.setMapOutputValueClass(IntWritable.class);

        // the reduce output is Text, IntWritable
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileSystem.get(conf).delete(new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/java/com/example/caesar/hadoop/partition/out"),true);
        FileInputFormat.setInputPaths(job,new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/resources/call.log"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/denial/IdeaProjects/caesar-boot/caesar-hadoop/src/main/java/com/example/caesar/hadoop/partition/out"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
