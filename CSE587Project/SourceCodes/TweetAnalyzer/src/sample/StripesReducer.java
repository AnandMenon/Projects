package sample;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.Reducer.Context;

public class StripesReducer extends Reducer<Text, MapWritable, Text, Text> {
	
	private MapWritable currentMap = new MapWritable();
	private IntWritable currentCount = new IntWritable();
	private IntWritable sentCount = new IntWritable();
	
	public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException 
	{
		currentMap.clear();
		for(MapWritable m:values)
		{
			Set<Writable> stripeSet = m.keySet();
			Iterator<Writable> iterator = stripeSet.iterator();
			while(iterator.hasNext())
			{
				Writable stripe = iterator.next();
				sentCount = (IntWritable) m.get(stripe);
				if(currentMap.containsKey(stripe))
				{
					currentCount = (IntWritable) currentMap.get(stripe);
					int add = (currentCount.get() + sentCount.get());
					currentCount.set(add);
					currentMap.put(stripe, currentCount);
				}
				else
				{
					currentMap.put(stripe, sentCount);
				}
				
			}
		}
		
		
		String outputString="";
		int totalCount=0;
		Iterator<Writable> i = currentMap.keySet().iterator();
		while(i.hasNext())
		{
			Text keyValue = (Text) i.next();
			IntWritable tempCount = (IntWritable) currentMap.get(keyValue);
			totalCount+=tempCount.get();
			outputString += keyValue.toString()+",!,"+tempCount.get()+":!:"; 
		}
		
		//System.out.println("output string is"+outputString);
		
		if(outputString.trim().equalsIgnoreCase(""))
		{
			//System.out.println("Entered here");
			context.write(key, key);
		}
		else
		{
		String[] processing1 = outputString.split(":!:");
		for(String s: processing1)
		{
			//System.out.println("s is:"+s);
			String[] processing2 = s.split(",!,");
			double d = Double.parseDouble(processing2[1]);
			d = (double)d/totalCount;
			Text key2 = new Text(key.toString()+","+processing2[0]);
			Text outputCount = new Text(String.valueOf(d));
			context.write(key2, outputCount);
		}
		}	
			
		//Text output = new Text(outputString+" total:"+totalCount);		
				
				
		
		
			
	
	}
	

}
