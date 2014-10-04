package sample;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class PairsCombiner extends Reducer<PairsKey, IntWritable, PairsKey, IntWritable> {
	
	private IntWritable count = new IntWritable();
	
	public void reduce(PairsKey key, Iterable<IntWritable> val, Context context) throws IOException, InterruptedException
	{
		//System.out.println("key2 in combiner is"+key.key2.toString());
		if(!key.key2.toString().equalsIgnoreCase("*"))
		{
		int sum = 0;
		for (IntWritable v : val) {
			sum += v.get();
		}
		count.set(sum);
		context.write(key, count);
		//System.out.println("combiner key:"+key.toString()+" val:"+count.get());
		}
	}

}
