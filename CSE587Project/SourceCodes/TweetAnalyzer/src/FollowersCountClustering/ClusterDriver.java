package FollowersCountClustering;

import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FSInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;

public class ClusterDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 // Initialize paths
		try
		{

			long numberOfClusters=3;
			Path inputPath=new Path("/input");
			String tempDirStr="/tempDir";
			Path tempDir=new Path(tempDirStr);
			Path finalOutput= new Path("/finalOutput");
			Path centroidSource=new Path("/tempDir/centroids");
			int counter=0; // keep track of how many times tasks were spawned
			Configuration confObj=new Configuration();
			confObj.set("centroids_path",centroidSource.toString());
			
			
			//Create the neceaary files / folders
			FileSystem fsobj=FileSystem.get(confObj);
			
			if(fsobj.exists(centroidSource))
				fsobj.delete(centroidSource);
			
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

			FSDataOutputStream fsOut=fsobj.create(centroidSource);
			String outputDump="100.0\n2000.0\n10000.0\n";
			fsOut.write(outputDump.getBytes());
			fsOut.close();
			
			
			String tempoutputPathStr=tempDirStr+"/run_1";
			
			
			// Initialize job
			
			System.out.println("Starting primary job #####!!!!!!!");
			Job clusterJob=Job.getInstance(confObj);
			clusterJob.setJarByClass(ClusterDriver.class);
			clusterJob.setNumReduceTasks(1);
			clusterJob.setMapOutputKeyClass(DoubleWritable.class);
			clusterJob.setMapOutputValueClass(TweeterDetails.class);
			clusterJob.setMapperClass(PrimaryMapper.class);
			clusterJob.setReducerClass(ClusterReducer.class);
			clusterJob.setOutputKeyClass(DoubleWritable.class);
			clusterJob.setOutputValueClass(TweeterDetails.class);
			clusterJob.setOutputFormatClass(TextOutputFormat.class);
			clusterJob.setInputFormatClass(TextInputFormat.class);
			FileInputFormat.addInputPath(clusterJob, inputPath);
			FileOutputFormat.setOutputPath(clusterJob,new Path(tempoutputPathStr));
			
			//clusterJob.setInputFormatClass(SequenceFileInputFormat.class);
			//clusterJob.setOutputFormatClass(SequenceFileOutputFormat.class);

			clusterJob.waitForCompletion(true);
			
			long convergenceCount=clusterJob.getCounters().findCounter(ClusterReducer.Converged.CENTROIDCOUNT).getValue();
			counter++;
			while(convergenceCount!=numberOfClusters && counter<=20)
			{
				
				clusterJob=Job.getInstance(confObj);
				clusterJob.setJarByClass(ClusterDriver.class);
				clusterJob.setMapOutputKeyClass(DoubleWritable.class);
				clusterJob.setMapOutputValueClass(TweeterDetails.class);
				clusterJob.setMapperClass(SecondaryMapper.class);
				clusterJob.setReducerClass(ClusterReducer.class);
				clusterJob.setOutputKeyClass(DoubleWritable.class);
				clusterJob.setOutputValueClass(TweeterDetails.class);
				clusterJob.setOutputFormatClass(TextOutputFormat.class);
				String tempInputPathStr=tempDirStr+"/run_"+counter;
				tempoutputPathStr=tempDirStr+"/run_"+(counter+1);
				
				FileInputFormat.addInputPath(clusterJob, new Path(tempInputPathStr));
				FileOutputFormat.setOutputPath(clusterJob,new Path(tempoutputPathStr));
				
				//clusterJob.setInputFormatClass(SequenceFileInputFormat.class);
				//clusterJob.setOutputFormatClass(SequenceFileOutputFormat.class);

				clusterJob.waitForCompletion(true);
				convergenceCount=clusterJob.getCounters().findCounter(ClusterReducer.Converged.CENTROIDCOUNT).getValue();
				counter++;

			}
		}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}

}
