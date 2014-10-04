package ShortestPathCalculator;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class ShortestPathMapper extends Mapper<Object, Text, IntWritable, VectorWritable>
{
//	protected void setup(Context context) throws IOException,InterruptedException
//	{
//		
//		
//	}
	
	protected void map(Object key,Text value,Context context) throws IOException,InterruptedException
	{
		StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
		
		while (itr.hasMoreTokens()) 
		{
			String line=itr.nextToken();
			String[] comp=line.split(" ");
			
			IntWritable nodeId=new IntWritable(Integer.parseInt(comp[0]));
			VectorWritable newVector=new VectorWritable();
			newVector.readFieldsFromString(comp[1]+" "+comp[2]);
			System.out.println(nodeId.get()+"---->"+newVector.toString());
			context.write(nodeId, newVector);
			
			
			for(Integer neighbour:newVector.nodeList)
			{
				VectorWritable neighbourVector=new VectorWritable();
				neighbourVector.distance=newVector.distance+1;
				
				System.out.println(neighbour.intValue()+"---->"+neighbourVector.toString());
				context.write(new IntWritable(neighbour), neighbourVector);
			}
			
		
		}
	}
	
}
