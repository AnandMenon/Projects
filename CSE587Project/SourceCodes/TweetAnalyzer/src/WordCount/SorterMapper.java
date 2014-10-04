package WordCount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import WordCount.PairsKey;

public class SorterMapper extends Mapper<Object,Text, PairsKey, IntWritable> 
{

	public void map(Object key, Text value,Context context) throws IOException,InterruptedException
	{
		StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
		
		while (itr.hasMoreTokens()) 
		{
			String line=itr.nextToken();
			String[] comp=line.split("\t");
			PairsKey newKey=new PairsKey(comp[0], comp[1]);
			//System.out.println(newKey.key1.toString()+---->"+newKey.key2.toString());
			context.write(newKey,new IntWritable(Integer.parseInt(newKey.key2.toString())));
		}
	}
	
}
