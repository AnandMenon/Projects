package WordCount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class SorterReducer extends Reducer<PairsKey, IntWritable,Text, IntWritable> {
	public void reduce(PairsKey key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
//		for (IntWritable val : values) {
//			sum += val.get();
//		}
		
		context.write(key.key1, new IntWritable(Integer.parseInt(key.key2.toString())));
	}
}
