package sample;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

@SuppressWarnings("rawtypes")
public class PairsKey implements WritableComparable {
	
	Text key1;
	Text key2;
	
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
	
	public String toString()
	{
		return key1.toString()+","+key2.toString();
	}

	

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		PairsKey incoming = (PairsKey)o;
		//System.out.println("Entered compareto with:"+incoming.toString()+" and "+this.toString());
		if(this.key1.compareTo(incoming.key1)!=0)
			return this.key1.compareTo(incoming.key1);
		
		if(this.key2.toString().equalsIgnoreCase("*"))
			return -1;
		else if(incoming.key2.toString().equalsIgnoreCase("*"))
			return 1;
		else
			return this.key2.toString().compareTo(incoming.key2.toString());
		
		
	}

}
