package FollowersCountClustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Reducer;

public class ClusterReducer extends Reducer<DoubleWritable, TweeterDetails, DoubleWritable, TweeterDetails>
{
	private static final double errorLimit=0.005;
	private ArrayList<DoubleWritable> nextCentroids;
	public static enum Converged
	{
		CENTROIDCOUNT
	}
	protected void setup(Context context) throws IOException, InterruptedException
	{
		
		nextCentroids=new ArrayList<DoubleWritable>();
		context.getCounter(Converged.CENTROIDCOUNT).setValue(0);
	}
	
	protected void reduce(DoubleWritable key, Iterable<TweeterDetails> values,Context context) throws IOException,  InterruptedException
	{
		int sum=0,count=0;
		
		for(TweeterDetails t:values)
		{
			sum+=t.followerCount.get();
			count++;
			context.write(key,new TweeterDetails(t));
		}
		double avg=(double)sum/(double)count;
		double convergenceFactor=Math.abs(key.get()-avg);
		if(convergenceFactor<errorLimit)
		{
			System.out.println(String.valueOf(avg)+ " converging to "+String.valueOf(key.get()));
			context.getCounter(Converged.CENTROIDCOUNT).increment(1);
		}
		nextCentroids.add(new DoubleWritable(avg));
		
	}
	
	protected void cleanup(Context context) throws IOException,InterruptedException
	{
		// dump nextCentroids to file
		
		Configuration confObj=context.getConfiguration();
		Path centroidsPath=new Path(confObj.get("centroids_path"));
		FileSystem fsObj=FileSystem.get(confObj);
		
//		SequenceFile.Writer w=SequenceFile.createWriter(fsObj, confObj, centroidsPath,DoubleWritable.class,IntWritable.class);
//		
//		Iterator<DoubleWritable> itr=nextCentroids.iterator();
//		while(itr.hasNext())
//		{
//			w.append(itr.next(), new IntWritable(1));
//		}
//		w.close();
		
		
		Iterator<DoubleWritable> itr=nextCentroids.iterator();
		String outputDump="";
		while(itr.hasNext())
		{
			outputDump=outputDump+String.valueOf(itr.next().get())+"\n";
		}
		
		fsObj.delete(centroidsPath);
		FSDataOutputStream fsOut=fsObj.create(centroidsPath);
		
		fsOut.writeBytes(outputDump);
		fsOut.close();

	}
                    
                    
                    
}
