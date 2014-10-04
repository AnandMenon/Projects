package FollowersCountClustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import WordCount.PairsKey;
import WordCount.WordCount;

public class PrimaryMapper extends Mapper<Object,Text, DoubleWritable,TweeterDetails>
{
	ArrayList<DoubleWritable> centroids;
	protected void setup(Context context) throws IOException,InterruptedException 
	{
		
		centroids=new ArrayList<DoubleWritable>();
//		centroids.add(new DoubleWritable(50));
//		centroids.add(new DoubleWritable(150));
//		centroids.add(new DoubleWritable(500));
		
		//Load the contents of the centroids into memory
		
		
		Configuration confObj=context.getConfiguration();
		Path centroidsPath=new Path(confObj.get("centroids_path"));
		FileSystem fsObj=FileSystem.get(confObj);
		
		// Read from the centroids file
		//SequenceFile.Reader r=new SequenceFile.Reader(confObj,SequenceFile.Reader.file(centroidsPath));
		
		//SequenceFile.Reader r=new SequenceFile.Reader(fsObj,centroidsPath,confObj);
		
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
//		
//		DoubleWritable key=new DoubleWritable();//(DoubleWritable) ReflectionUtils.newInstance(r.getKeyClass(),confObj);
//		IntWritable value= new IntWritable();//(IntWritable) ReflectionUtils.newInstance(r.getValueClass(), confObj);
//		while(r.next(key,value)) // reading by reference
//			
//		
//		r.close();
//		//Logger l = LoggerFactory.getLogger(PrimaryMapper.class);
		//l.info(centroids.toString());
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
			
			int fCount=Integer.parseInt(comp[1]);
			TweeterDetails newTweeter=new TweeterDetails(new Text(comp[0]), new IntWritable(fCount));
			// Get the nearest centroid
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
			
			Logger l = LoggerFactory.getLogger(PrimaryMapper.class);
			l.info(nearestCentroid+"for "+newTweeter.twitterHandle);
			System.out.println(nearestCentroid+"for "+newTweeter.twitterHandle);
			context.write(nearestCentroid, newTweeter);
		}
	}
//		
//	protected void cleanup(Context context) throws IOException, InterruptedException
//	{
//	}
            
}