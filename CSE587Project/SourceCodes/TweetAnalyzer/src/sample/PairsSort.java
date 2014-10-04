package sample;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class PairsSort extends WritableComparator {
	
	PairsSort()
	{
		super(PairsKey.class,true);
	}
	
	@SuppressWarnings("rawtypes")
	public int compare(WritableComparable key1, WritableComparable key2)
	{
		PairsKey k1 = (PairsKey)key1;
		PairsKey k2 = (PairsKey)key2;
		
		return k1.compareTo(k2);
		
	}

}
