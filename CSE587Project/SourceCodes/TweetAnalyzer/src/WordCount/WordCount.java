package WordCount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCount {

	private static final transient Logger LOG = LoggerFactory.getLogger(WordCount.class);

	public static void main(String[] args) throws Exception {
		
		
		Configuration conf = new Configuration();		

		LOG.info("HDFS Root Path: {}", conf.get("fs.defaultFS"));
		LOG.info("MR Framework: {}", conf.get("mapreduce.framework.name"));
		/* Set the Input/Output Paths on HDFS */
		String inputPath  =  "/input";
		String outputPath = "/output";
		String finalOutput = "/finalOutput";

		/* FileOutputFormat wants to create the output directory itself.
		 * If it exists, delete it:
		 */
		deleteFolder(conf,outputPath);
		deleteFolder(conf,finalOutput);
		
		Job counterJob = Job.getInstance(conf);
		//System.out.println("setting count to"+3);
		
		//job.set("mapred.reduce.tasks","3");
		counterJob.setJarByClass(WordCount.class);
		counterJob.setNumReduceTasks(3);
		counterJob.setMapperClass(WordCountMapper.class);
		counterJob.setCombinerClass(IntSumReducer.class);
		counterJob.setPartitionerClass(WordCountPartitioner.class);
		counterJob.setReducerClass(IntSumReducer.class);
		counterJob.setOutputKeyClass(Text.class);
		counterJob.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPath(counterJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(counterJob, new Path(outputPath));
		
		counterJob.waitForCompletion(true);
		
		// Start with the sorting pahase;
		Job sorterJob=Job.getInstance(conf);
		sorterJob.setJarByClass(WordCount.class);
		sorterJob.setMapperClass(SorterMapper.class);
		sorterJob.setNumReduceTasks(3);
			//sorterJob.setSortComparatorClass(WordCountValueComparator.class);
		//sorterJob.setCombinerClass(SorterReducer.class);
		sorterJob.setPartitionerClass(SorterPartitioner.class);
		sorterJob.setReducerClass(SorterReducer.class);
		
		sorterJob.setMapOutputKeyClass(PairsKey.class);
		sorterJob.setMapOutputValueClass(IntWritable.class);
		
		sorterJob.setOutputKeyClass(Text.class);
		sorterJob.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(sorterJob, new Path(outputPath));
		FileOutputFormat.setOutputPath(sorterJob, new Path(finalOutput));
		
		System.exit(sorterJob.waitForCompletion(true) ? 0 : 1);
	}
	
	/**
	 * Delete a folder on the HDFS. This is an example of how to interact
	 * with the HDFS using the Java API. You can also interact with it
	 * on the command line, using: hdfs	 dfs -rm -r /path/to/delete
	 * 
	 * @param conf a Hadoop Configuration object
	 * @param folderPath folder to delete
	 * @throws IOException
	 */
	private static void deleteFolder(Configuration conf, String folderPath ) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(folderPath);
		if(fs.exists(path)) {
			fs.delete(path,true);
		}
	}
}