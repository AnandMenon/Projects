package ShortestPathCalculator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import FollowersCountClustering.ClusterDriver;
import FollowersCountClustering.ClusterReducer;
import FollowersCountClustering.PrimaryMapper;
import FollowersCountClustering.SecondaryMapper;
import FollowersCountClustering.TweeterDetails;

public class ShortestPathDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		try
		{
			Path inputPath=new Path("/input");
			String tempDirStr="/tempDir";
			Path tempDir=new Path(tempDirStr);
			Path finalOutput= new Path("/finalOutput");
			int counter=0; // keep track of how many times tasks were spawned
			Configuration confObj=new Configuration();
			confObj.set("mapred.textoutputformat.separator"," ");
			
			//Create the neceaary files / folders
			FileSystem fsobj=FileSystem.get(confObj);
			
			if(fsobj.exists(finalOutput))
				fsobj.delete(finalOutput);
			if(fsobj.exists(tempDir))
				fsobj.delete(tempDir);

			// Set initial centroids
//			SequenceFile.Writer w=SequenceFile.createWriter(fsobj, confObj, centroidSource,DoubleWritable.class,IntWritable.class);
//			w.append(new DoubleWritable(50.0),new IntWritable(1));
//			w.append(new DoubleWritable(150.0),new IntWritable(1));
//			w.append(new DoubleWritable(500.0),new IntWritable(1));
//			w.close();
//			

			
			String tempoutputPathStr=tempDirStr+"/run_1";
			
			
			// Initialize job
			
			System.out.println("Starting primary job #####!!!!!!!");
			Job shortestPathJob=Job.getInstance(confObj);
			shortestPathJob.setJarByClass(ShortestPathDriver.class);
			shortestPathJob.setNumReduceTasks(1);
			shortestPathJob.setMapOutputKeyClass(IntWritable.class);
			shortestPathJob.setMapOutputValueClass(VectorWritable.class);
			shortestPathJob.setMapperClass(ShortestPathMapper.class);
			shortestPathJob.setReducerClass(ShortestPathReducer.class);
			shortestPathJob.setOutputKeyClass(IntWritable.class);
			shortestPathJob.setOutputValueClass(VectorWritable.class);
			shortestPathJob.setOutputFormatClass(TextOutputFormat.class);
			shortestPathJob.setInputFormatClass(TextInputFormat.class);
			FileInputFormat.addInputPath(shortestPathJob, inputPath);
			FileOutputFormat.setOutputPath(shortestPathJob,new Path(tempoutputPathStr));
			
			//clusterJob.setInputFormatClass(SequenceFileInputFormat.class);
			//clusterJob.setOutputFormatClass(SequenceFileOutputFormat.class);

			shortestPathJob.waitForCompletion(true);
			
			long disconnectedCount=shortestPathJob.getCounters().findCounter(ShortestPathReducer.Counter.DISCONNECTED).getValue();
			counter++;
			while(disconnectedCount>0)
			{
				
				
				shortestPathJob=Job.getInstance(confObj);
				shortestPathJob.setJarByClass(ShortestPathDriver.class);
				shortestPathJob.setMapOutputKeyClass(IntWritable.class);
				shortestPathJob.setMapOutputValueClass(VectorWritable.class);
				shortestPathJob.setMapperClass(ShortestPathMapper.class);
				shortestPathJob.setReducerClass(ShortestPathReducer.class);
				shortestPathJob.setOutputKeyClass(IntWritable.class);
				shortestPathJob.setOutputValueClass(VectorWritable.class);
				shortestPathJob.setOutputFormatClass(TextOutputFormat.class);
				String tempInputPathStr=tempDirStr+"/run_"+counter;
				tempoutputPathStr=tempDirStr+"/run_"+(counter+1);
				
				FileInputFormat.addInputPath(shortestPathJob, new Path(tempInputPathStr));
				FileOutputFormat.setOutputPath(shortestPathJob,new Path(tempoutputPathStr));
				
				//clusterJob.setInputFormatClass(SequenceFileInputFormat.class);
				//clusterJob.setOutputFormatClass(SequenceFileOutputFormat.class);

				shortestPathJob.waitForCompletion(true);
				disconnectedCount=shortestPathJob.getCounters().findCounter(ShortestPathReducer.Counter.DISCONNECTED).getValue();
				counter++;

			}
		}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}

}
