package WordCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SorterPartitioner extends Partitioner<PairsKey, IntWritable> {
	public int getPartition(PairsKey key, IntWritable value, int numPartitions) 
	{
		 

		 String keyString=key.key1.toString();
		 if(numPartitions==0)
			 return 0;
		 if(keyString.startsWith("#"))
			 return 1%numPartitions;
		 else if(keyString.startsWith("@"))
			 return 2%numPartitions;
		 else 
			 return 0;
			 
		
	}
}
