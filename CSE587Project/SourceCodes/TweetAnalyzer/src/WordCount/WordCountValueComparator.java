package WordCount;

import java.nio.ByteBuffer;

import org.apache.hadoop.io.WritableComparator;

import WordCount.PairsKey;

public class WordCountValueComparator extends WritableComparator 
{

	public int compare(Object a, Object b)
	{
		int value1=Integer.parseInt(((PairsKey)a).key2.toString());
		int value2=Integer.parseInt(((PairsKey)b).key2.toString());
		return value2-value1;
		
		
	}
//	public int compare(byte[] b1, int s1, int l1,
//            byte[] b2, int s2, int l2) {
//
//        Integer v1 = ByteBuffer.wrap(b1, s1, l1).getInt();
//        Integer v2 = ByteBuffer.wrap(b2, s2, l2).getInt();
//
//        return v1.compareTo(v2) * (-1);
//	}

}
