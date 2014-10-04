import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class DataSplitter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName=args[0];
		String outputFolder=args[1];
		try {
			FileReader fr = new FileReader(new File(fileName));
			BufferedReader br= new BufferedReader(fr);
			String line="";
			String outputDump="";
			int tCount=0;
			int fCount=0;
			while(br.ready()) // once for every line 
			{
				line=br.readLine();
				outputDump+=line+"\n";
				tCount++;
				if(tCount%10000==0)
				{
					
					BufferedWriter bw=new BufferedWriter(new FileWriter(new File(outputFolder+"file_"+String.valueOf(fCount)),true));
					bw.write(outputDump);
					outputDump="";
					bw.close();
					System.gc();
					fCount++;
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
	
		}
	}

}
