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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileCleaner {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String tweetInputFolder="/home/hduser/";
		String tweetOutputFolder="/home/hduser/";
		
			String fileName=tweetInputFolder+"input-graph-large";
			String outputFile=tweetOutputFolder+"edges";
			try {
				FileReader fr = new FileReader(new File(fileName));
				BufferedReader br= new BufferedReader(fr);
				String line="";
				String finalOutput="";
				int nodeCount =1;
				int edgeCount=1;
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				Document document = builder.newDocument();
				
				Element root = document.createElement("graph");
				document.appendChild(root);
				
				Element nodeElement = document.createElement("nodes");
				root.appendChild(nodeElement);
				
				Attr attribute = document.createAttribute("count");
				attribute.setValue("110");
				nodeElement.setAttributeNode(attribute);
				
		 
				
				while((line = br.readLine())!=null && nodeCount<=110) // once for every line 
				{
					//line=br.readLine();
					System.out.println(line);
					String[] graphContents = line.split("\\s+");
					
					String nodeId = graphContents[0];
					String neighbourList[] = graphContents[2].split(":");
					
					//Element[] element = null;
					int i=0;
					for(String neighbour : neighbourList)
					{
					
					Element element = document.createElement("edge");
					Attr elementAttribute = document.createAttribute("id");
					elementAttribute.setValue(String.valueOf(edgeCount));
					element.setAttributeNode(elementAttribute);
					//nodeElement.appendChild(element[i]);
					
					Attr sourceAttribute = document.createAttribute("source");
					sourceAttribute.setValue(nodeId);
					element.setAttributeNode(sourceAttribute);
					//nodeElement.appendChild(element[i]);
					
					Attr targetAttribute = document.createAttribute("target");
					targetAttribute.setValue(neighbour);
					element.setAttributeNode(targetAttribute);
					nodeElement.appendChild(element);
					edgeCount++;
					
					}
					
					nodeCount++;
				}
				
				TransformerFactory t = TransformerFactory.newInstance();
				Transformer tf = t.newTransformer();
				
				DOMSource domSource = new DOMSource(document);
				StreamResult result = new StreamResult(new File(outputFile));
				
				tf.transform(domSource, result);
					
								} catch (Exception e) {
				e.printStackTrace();
		
			}
			
		
		
	}
 	

}
