import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;




public class VisualisationDataGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName=args[0];
		String outputFile=args[1];
		try {
			FileReader fr = new FileReader(new File(fileName));
			BufferedReader br= new BufferedReader(fr);
			String line="";
			
			while(br.ready()) // once for every line 
			{
				line=br.readLine();
				String[] comp=line.split("\t");
				String word=comp[0];
				int count=Integer.parseInt(comp[1]);
				String output="";
				for(int i=1;i<=count;i++)
					output+=word+" ";
				output+="\n";
				
				
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File(outputFile),true));
				bw.write(output);
				bw.close();
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
	
		}
	}

}
