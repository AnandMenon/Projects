package FollowersCountClustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.util.ReflectionUtils;

public class SecondaryMapper extends Mapper<Object,Text,DoubleWritable, TweeterDetails> {
	
	private ArrayList<DoubleWritable> centroids;
	
	protected void setup(Context context) throws IOException,InterruptedException 
	{
		centroids=new ArrayList<DoubleWritable>();
		
		//Load the contents of the centroids into memory
		
		
		Configuration confObj=context.getConfiguration();
		Path centroidsPath=new Path(confObj.get("centroids_path"));
		FileSystem fsObj=FileSystem.get(confObj);
		
		// Read from the centroids file
//		SequenceFile.Reader r=new SequenceFile.Reader(confObj,SequenceFile.Reader.file(centroidsPath));
//		
//		
//		DoubleWritable key=(DoubleWritable) ReflectionUtils.newInstance(r.getKeyClass(),confObj);
//		IntWritable value=(IntWritable) ReflectionUtils.newInstance(r.getValueClass(), confObj);
//		while(r.next(key,value)) // reading by reference
//			centroids.add(key);
//		
//		r.close();
		
		FSDataInputStream fsIn=fsObj.open(centroidsPath);
		byte[] inputDump=new byte[50];
		int byteCount=fsIn.read(inputDump);
		
		String[] comp=(new String(inputDump,0,byteCount)).split("\n");
		for(String s:comp)
		{
			if(s!=null && s.length()>0)
				centroids.add(new DoubleWritable(Double.parseDouble(s)));
		}
		fsIn.close();
		
		System.out.println("dsfdafdsfadsfdasfda######"+centroids.toString());
	}
	
	protected void map(Object key,Text value,Context context) throws IOException,InterruptedException
	{
		//Document as input
		StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
		
		while (itr.hasMoreTokens()) 
		{
			String line=itr.nextToken();
			String[] comp=line.split("\t");
			TweeterDetails newT=new TweeterDetails(new Text(comp[1]),new IntWritable(Integer.parseInt(comp[2])));
		//System.out.println(value.twitterHandle+"   "+value.followerCount);
		int fCount=newT.followerCount.get();
		Iterator<DoubleWritable> cItr=centroids.iterator();
		DoubleWritable nearestCentroid=null;
		double minDistance=-1;
		while(cItr.hasNext())
		{
			DoubleWritable cValue=cItr.next();
			double distance=Math.abs(cValue.get()-(double)fCount);
			if(nearestCentroid==null)
			{
				
				nearestCentroid=cValue;
				minDistance=distance;
			}
			else 
			{
				if(distance<minDistance)
				{
					nearestCentroid=cValue;
					minDistance=distance;
				}
			}
			
		}
		System.out.println(nearestCentroid+"for "+newT.twitterHandle+"sdfsdfd--->"+newT.followerCount);
		
		context.write(nearestCentroid, newT);
		}
	}
		
//	protected void cleanup(Context context) throws IOException, InterruptedException
//	{
//		
//	}
}
