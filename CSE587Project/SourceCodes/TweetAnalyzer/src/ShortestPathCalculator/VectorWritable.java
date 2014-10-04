package ShortestPathCalculator;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.WritableComparable;

public class VectorWritable implements WritableComparable {
	public int distance ;
	public ArrayList<Integer> nodeList;
	public VectorWritable()
	{
		distance=-1;
		nodeList=new ArrayList<Integer>();
	}
	public VectorWritable(VectorWritable v)
	{
		distance=v.distance;
		nodeList=v.nodeList;
	}
	
	public void readFieldsFromString(String str)
	{
		System.out.println("Splitting"+str);
		String[] compStrings=str.split(" ");
		distance=Integer.parseInt(compStrings[0]);
		if(compStrings.length>1) // has a valid node list
		{
			nodeList.clear();
			String [] minorComp=compStrings[1].split(":");
			for(String s:minorComp)
			{
				if(s==null || s.length()==0)
					continue;
				nodeList.add(Integer.parseInt(s));
			}
		}
		else
			nodeList.clear();
	}
	@Override
	public void readFields(DataInput arg0) throws IOException
	{
		// TODO Auto-generated method stub
		String inputStr=arg0.readUTF();
		this.readFieldsFromString(inputStr);
	}

	public String toString()
	{
		String output=String.valueOf(this.distance)+" ";

		for(Integer i:this.nodeList)
			output=output+String.valueOf(i)+":";
		System.out.println("converted to"+output);
		return output;
	}
	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		arg0.writeUTF(this.toString());
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		VectorWritable incoming=(VectorWritable)o;
		if(incoming.distance!=this.distance)
			return incoming.distance-this.distance;
		else
			return -1; // order really does not matter
	}

}
