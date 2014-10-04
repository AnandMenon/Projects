package sample;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.Reducer.Context;

public class PairsReducer extends Reducer<PairsKey, IntWritable, PairsKey, DoubleWritable> {
	
	private DoubleWritable result = new DoubleWritable();
	private IntWritable marginal = new IntWritable(1);
	private String preservedState = new String("begin");
	
	public void reduce(PairsKey key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		
		//System.out.println("first key is:"+key.toString() +" marginal is:"+marginal.get());
		
		if(key.key2.toString().equalsIgnoreCase("*"))
		{
			if(!key.key1.toString().equalsIgnoreCase(preservedState))
			{
				//new special pair
				preservedState = key.key1.toString();
				int sum = 0;
				for (IntWritable val : values) {
					sum += val.get();
				}
				//System.out.println("sum for preserved is"+sum);
				marginal.set(sum);
			}
			else
			{
				int sum = 0;
				for (IntWritable val : values) {
					sum += val.get();
				}
				marginal.set(marginal.get()+sum);
				
			}
			
		}
		else
		{
		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		
		//sum = sum/((double)marginal.get());
		//System.out.println("sum 3 is:"+sum+" marginal is"+marginal.get());
		result.set((double)sum/((double)marginal.get()));
		context.write(key, result);
		}
	}

}
