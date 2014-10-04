package ShortestPathCalculator;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import FollowersCountClustering.TweeterDetails;
import FollowersCountClustering.ClusterReducer.Converged;

public class ShortestPathReducer extends Reducer<IntWritable,VectorWritable,IntWritable,VectorWritable>
{
	public static enum Counter
	{
		 DISCONNECTED;
	}
	
	protected void setup(Context context) throws IOException, InterruptedException
	{
		context.getCounter(Counter.DISCONNECTED).setValue(0);
	}
	protected void reduce(IntWritable key, Iterable<VectorWritable> values,Context context) throws IOException,  InterruptedException
	{
		int minDistance=10000;
		
		VectorWritable outputVector=new VectorWritable();
		Iterator<VectorWritable> vItr=values.iterator();
		while(vItr.hasNext())
		//for(VectorWritable v:values)
		{
			VectorWritable v=vItr.next();
			System.out.println("received######"+key.get()+"---->"+v.toString());
			if(v.nodeList.size()>0 && outputVector.nodeList.isEmpty())
			{
				System.out.println("Setting nodelist"+v.nodeList.toString());
				outputVector.nodeList.addAll(v.nodeList);
			}
//			else
//			{
				int dist=v.distance;
				if(dist<minDistance)
					minDistance=dist;
			//}
		}
		outputVector.distance=minDistance;
		
		context.write(key, outputVector);
		if(outputVector.distance>=10000)
			context.getCounter(Counter.DISCONNECTED).increment(1);
	}
}