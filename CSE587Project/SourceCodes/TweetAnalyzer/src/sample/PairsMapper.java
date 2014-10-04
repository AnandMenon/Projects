package sample;

import java.io.IOException;
//import java.util.StringTokenizer;


//import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Mapper.Context;

public class PairsMapper extends Mapper<Object, Text, PairsKey, IntWritable> {
	

	private final static IntWritable one = new IntWritable(1);
	//private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		//StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
		//System.out.println("The line is:"+itr.nextToken());
		//String out=new String("");
		//while (itr.hasMoreTokens()) {
			//int flag=0;
			//out = "";
			String[] tweetContents = value.toString().split("\\s+");
			//System.out.println("first word is"+tweetContents[0]);
			//Code for all words
			for(int i=0;i<tweetContents.length;i++)
			{
				PairsKey specialPair = new PairsKey(tweetContents[i], "*");
				context.write(specialPair, new IntWritable(tweetContents.length-1));
				//System.out.println("First pair:"+specialPair.toString()+" value:"+(tweetContents.length-1));
				for(int j=0;j<tweetContents.length;j++)
				{
					if(tweetContents[i].equalsIgnoreCase(tweetContents[j]))
						continue;
					PairsKey pairsKey = new PairsKey(tweetContents[i],tweetContents[j]);
					context.write(pairsKey, one);
					//System.out.println("Keys:"+pairsKey.toString());
				}
				
			}
			
			//Code for only Hash Tags
			/*for(String tag : tweetContents)
			{
				if(flag>=2)
					break;
				if(tag.charAt(0)=='#')
				{
					if(out.equalsIgnoreCase(""))
							out=tag;
					else
						out=out+","+tag;
					
					context.write(new Text(out+",*"), one);
					flag+=1;
				}
			}
			word.set(out);
			context.write(word, one);*/
			
		//}
	}	

}
