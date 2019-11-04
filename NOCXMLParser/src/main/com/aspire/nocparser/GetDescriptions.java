package main.com.aspire.nocparser;

import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class GetDescriptions {

	public String getNOCDescriptions(String xmlContent) {
		
		String description = "";
		
		try {
			System.out.println("---- "+ xmlContent +"---");
			System.out.println("/////NOCParser -> NOC Code Parser Request Received Successfully\n"+xmlContent);
			
			if(xmlContent == null || xmlContent.isEmpty())
				System.out.println("/////NOCParser -> Request is Empty");
			else
				System.out.println("/////NOCParser -> Request is not Empty");
			InputSource input = new InputSource (new StringReader(xmlContent));
			System.out.println("/////NOCParser -> source created");
			SAXReader reader = new SAXReader();
			System.out.println("/////NOCParser -> reader created");
			Document document = reader.read(input);
			System.out.println("/////NOCParser -> Your Request is now ready to be parsed");
			@SuppressWarnings("unchecked")
			List<Node> nodes = document.selectNodes("/NOCWS/RESULTS/NOC_PROFILE");
			System.out.println("/////NOCParser -> Number of description found for given occupation is:"+nodes.size());
			for (Node node : nodes) {
				
				description = description + node.selectSingleNode("CODE").selectSingleNode("NUMBER").getText()+"#"  
						+ node.selectSingleNode("UNIT_GROUP_TITLE").selectSingleNode("ENGLISH").selectSingleNode("TITLE").getText()+"|";					
			}
			
			if(nodes.isEmpty()) {
				description = "9999#No Job Descriptions Found";
				System.out.println("///// NOCParser -> Default description is added since no relevent jobs found with given occupation");
			}
			
		} catch (Exception e) {
			System.out.println("///// NOCParser Error -> "+e.getMessage());
			e.printStackTrace();
		}
		
		return description;
	}
	
}
