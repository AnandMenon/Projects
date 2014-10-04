package WordCount;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<Object,Text, Text, IntWritable> {
	private HashMap<String, Integer> localWordCounts;
	
	protected void setup(Context context) throws IOException, InterruptedException
	{ 
		localWordCounts = new HashMap<String,Integer>();
	}
	
	protected void map(Object key, Text value,Context context) throws IOException,InterruptedException
	{
		StringTokenizer itr = new StringTokenizer(value.toString()," ");
		while (itr.hasMoreTokens()) 
		{
			String keyString=itr.nextToken();
			int prevValue=0;
			if(localWordCounts.containsKey(keyString))
				prevValue=localWordCounts.get(keyString).intValue();
			prevValue++;
			localWordCounts.put(keyString,prevValue);
			
		}
	}
	protected void cleanup(Context context) throws IOException, InterruptedException
	{
		Iterator<String> itr=localWordCounts.keySet().iterator();
		while(itr.hasNext())
		{
			String keyString=itr.next();
			context.write(new Text(keyString),new IntWritable(localWordCounts.get(keyString)));
		}
		localWordCounts.clear();
	}

               
}

