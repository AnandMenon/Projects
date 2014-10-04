package sample;

import java.io.IOException;
//import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Mapper.Context;

public class StripesMapper extends Mapper<Object, Text, Text, MapWritable> {
	
	private Text word = new Text();
	//private IntWritable sum = new IntWritable();
	private IntWritable one = new IntWritable(1);
	private MapWritable AssociativeArray = new MapWritable();
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		String[] tweetContents = value.toString().split("\\s+");
		
		for(int i=0;i<tweetContents.length;i++)
		{
			word.set(tweetContents[i]);
			AssociativeArray.clear();
			for(int j=0;j<tweetContents.length;j++)
			{
				if(tweetContents[i].equalsIgnoreCase(tweetContents[j]))
					continue;
				Text stripe = new Text(tweetContents[j]);
				//System.out.println("keys are:"+tweetContents[i]+" and "+tweetContents[j]);
				if(AssociativeArray.containsKey(stripe))
				{
					/*System.out.println("Entered here with "+tweetContents[j]);
					sum = (IntWritable) AssociativeArray.get(stripe);
					int increment = sum.get()+1;
					sum.set(increment);
					AssociativeArray.put(stripe, sum);*/
				}
				else
				{
					AssociativeArray.put(stripe, one);
				}
						
						
			}
			context.write(word, AssociativeArray);
		}
		
	}

}
