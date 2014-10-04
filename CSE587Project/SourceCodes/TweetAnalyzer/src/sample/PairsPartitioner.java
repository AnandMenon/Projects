package sample;

//import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class PairsPartitioner extends Partitioner<PairsKey, IntWritable> {

	@Override
	public int getPartition(PairsKey key, IntWritable value, int reducerCount) {
		
		//Code for splitting without hash
		/*String[] keyVal = key.toString().split(",");
		if((keyVal[0].charAt(1) >= 'a' && keyVal[0].charAt(1) <= 'g') || (keyVal[0].charAt(1) >= 'A' && keyVal[0].charAt(1) <= 'G'))
			return 0;
		
		else if((keyVal[0].charAt(1) >= 'h' && keyVal[0].charAt(1) <= 'r') || (keyVal[0].charAt(1) >= 'H' && keyVal[0].charAt(1) <= 'R'))
			return 1 % reducerCount;
		
		else if((keyVal[0].charAt(1) >= 's' && keyVal[0].charAt(1) <= 'z') || (keyVal[0].charAt(1) >= 'S' && keyVal[0].charAt(1) <= 'Z'))
			return 1 % reducerCount;*/
		//System.out.println("key in partitioner is:"+key.toString()+" value is"+value.get()+" going to reducer:"+Math.abs(key.key1.hashCode()) % reducerCount);
		return( Math.abs(key.key1.hashCode()) % reducerCount);
		
		
		//return 0;
	}

}
