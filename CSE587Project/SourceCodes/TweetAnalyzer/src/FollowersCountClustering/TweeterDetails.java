package FollowersCountClustering;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

 public class TweeterDetails implements WritableComparable
{
	public Text twitterHandle;
	public IntWritable followerCount;
	
	public TweeterDetails(Text s,IntWritable i)
	{
		twitterHandle=s;
		followerCount=i;
	}
	
	public TweeterDetails(TweeterDetails t)
	{
		this.twitterHandle=new Text(t.twitterHandle);
		this.followerCount=new IntWritable(t.followerCount.get());
	}
	
	public TweeterDetails()
	{
		twitterHandle=new Text();
		followerCount=new IntWritable();
	}
	public void readFields(DataInput in) throws IOException
	{
//		twitterHandle=new Text(in.readUTF());
//		followerCount=new IntWritable(Integer.parseInt(in.readUTF()));
		
		String inputStr=new String(in.readUTF());
		System.out.println(inputStr);
		String[] comp=new String[2];
		comp=inputStr.split("\t");
		if(comp==null)
			System.out.println("NULLLLLL");
		else
			System.out.println(comp[0]);
		twitterHandle=new  Text(comp[0]);
		followerCount=new IntWritable(Integer.parseInt(comp[1]));
	}
	
	public void write(DataOutput out)throws IOException
	{
//		out.writeUTF(twitterHandle.toString());
//		out.writeUTF(String.valueOf(followerCount.get()));
		//String outputString=twitterHandle.toString()+"$#$#$#"+String.valueOf(followerCount.get());
		
		//System.out.println("RPint : "+outputString);
		out.writeUTF(this.toString());
	
	}
	@Override
	public String toString() {
		System.out.println("called");
		String outputString=twitterHandle.toString()+"\t"+String.valueOf(followerCount.get());
		return outputString;
		//return Arrays.toString(get());
	}
	
	
	public int compareTo(Object o)
	{
		TweeterDetails incoming=(TweeterDetails)o;
		if(incoming.followerCount.get()!=this.followerCount.get())
			return incoming.followerCount.get()-this.followerCount.get();
		else
			return this.twitterHandle.compareTo(incoming.twitterHandle);
	}
	
}
