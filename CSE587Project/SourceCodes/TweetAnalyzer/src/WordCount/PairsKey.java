package WordCount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

@SuppressWarnings("rawtypes")
public class PairsKey implements WritableComparable {
	
	public Text key1;
	public Text key2;
	
	public PairsKey(String key1, String key2)
	{
		this.key1 = new Text(key1);
		this.key2 = new Text(key2);
	}
	
	public PairsKey()
	{
		this.key1 = new Text();
		this.key2 = new Text();
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		// TODO Auto-generated method stub
		key1.set(input.readUTF());
		key2.set(input.readUTF());
		
	}

	@Override
	public void write(DataOutput output) throws IOException {
		// TODO Auto-generated method stub
		output.writeUTF(key1.toString());
		output.writeUTF(key2.toString());
		
	}

	

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		PairsKey incoming = (PairsKey)o; 
		
		int value1=Integer.parseInt(this.key2.toString());
		int value2=Integer.parseInt(incoming.key2.toString());
		if(value2!=value1)
			return value2-value1;
		
		return this.key1.toString().compareTo(incoming.key1.toString());
//		if(this.key2.toString().equalsIgnoreCase("*"))
//			return -1;
//		else if(incoming.toString().equalsIgnoreCase("*"))
//			return 1;
//		else
//			return this.key1.toString().compareTo(incoming.toString());
		
		
	}

}
