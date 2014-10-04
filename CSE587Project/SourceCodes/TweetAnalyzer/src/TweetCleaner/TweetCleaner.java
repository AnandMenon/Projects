package TweetCleaner;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class TweetCleaner {
	
	public static final HashSet<String> stopWordCollection=new HashSet<String>(Arrays.asList("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "i","ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your","you're", "yours", "yourself", "yourselves", "the"));
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String tweetInputFolder="/home/subhranil/Data/Tweets/";
		String tweetOutputFolder="/home/subhranil/Data/Cleaned/Tweets/";
		for(int i=1;i<=75;i++)
		{
			String fileName=tweetInputFolder+"file_"+String.valueOf(i);
			String outputFile=tweetOutputFolder+"file_"+String.valueOf(i);
			try {
				FileReader fr = new FileReader(new File(fileName));
				BufferedReader br= new BufferedReader(fr);
				String line="";
				String finalOutput="";
				while((line = br.readLine())!=null) // once for every line 
				{
					//line=br.readLine();
					StringTokenizer st=new StringTokenizer(line," ");
					while(st.hasMoreTokens())
					{
						String tok=st.nextToken();
						tok=TweetCleaner.trimSpecialCharacters(tok);
						if(tok!=null && tok.length()>0 && !TweetCleaner.stopWordCollection.contains(tok.toLowerCase()))
						{
							finalOutput+=tok;
						}
						if(st.hasMoreTokens())
							finalOutput+=" ";
						else
							finalOutput+="\n";
					}
					
						
					
						
					
						if(finalOutput.length()>5000)
						{
							TweetCleaner.writeToFile(finalOutput,outputFile);
							finalOutput="";
						}
					}

					

				
				TweetCleaner.writeToFile(finalOutput,outputFile);
				finalOutput="";
			} catch (Exception e) {
				e.printStackTrace();
		
			}
			
		}
		
	}
 	
	private static String trimSpecialCharacters(String str)
	{
		if(str.startsWith("#") || str.startsWith("@") )
			return str; // hashtag , nothing to be done
		
		int i=str.length()-1;
		while(i>=0)
		{
			
			if(TweetCleaner.isSpecialCharacter(str.charAt(i))==false)
				break;
			
			i--;
			 
		}
		if(i<0)
			return "";
		else
			return str.substring(0,i+1);
		
	}
	
	private static boolean isSpecialCharacter(char c)
	{
		if((c>='a' && c<='z')|| (c>='A' && c<='Z') || (c>='0' && c<='9'))
			return false;
		return true;
	}
	
	private static void writeToFile(String text,String outputPath)
	{
		try
		{
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(outputPath),true));
			bw.write(text);
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

}
