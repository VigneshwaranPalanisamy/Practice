package main.com.aspire.nocparser;

import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class ReadNOCDescriptions {

	//static String filepath = "C:\\Users\\vignesh.palani\\Desktop\\NOCRes.xml";
	static String filepath = "C:\\FO\\CustomerOnboarding\\Solutions\\CustomerOnboarding\\new.xml";

	public static void main(String[] args) {
		
		try {
			//File inputFile = new File();
			//File inputFile = new File(filepath);
			/*stringToDom(inputFile,
					"<NOCWS xmlns:nocws=\"NOCWebService\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://cnp-noc.services.gc.ca/WebServices/Service.asmx/Schema/NOCWS.xsd\"> "
							+ "<BRANDING>   <PRODUCT VERSION=\"2017\">     <ENGLISH>       <NAME WEB_LINK=\"http://noc.esdc.gc.ca/English/NOC/Welcome.aspx?ver=16\">National Occupational Classification</NAME>       <DESCRIPTION>         <PARAGRAPH>String</PARAGRAPH>       </DESCRIPTION>       "
							+ "<AUTHORITY WEB_LINK=\"https://www.canada.ca/en.html\">Government of Canada</AUTHORITY>     </ENGLISH>     <FRENCH>       <NAME WEB_LINK=\"http://noc.esdc.gc.ca/Francais/CNP/Bienvenue.aspx?ver=16\">Classification nationale des professions</NAME>       "
							+ "<DESCRIPTION>         <PARAGRAPH>String</PARAGRAPH>       </DESCRIPTION>       <AUTHORITY WEB_LINK=\"https://www.canada.ca/fr.html\">Gouvernement du Canada</AUTHORITY>     </FRENCH>     <SUPPORT>       <ENGLISH>http://noc.esdc.gc.ca/English/NOC/ContactUs.aspx?ver=16</ENGLISH>     "
							+ "  <FRENCH>http://noc.esdc.gc.ca/Francais/CNP/ContactezNous.aspx?ver=16</FRENCH>     </SUPPORT>   </PRODUCT>   <COPYRIGHT>     <ENGLISH>       <TEXT>The National Occupational Classification, Her Majesty the Queen in Right of Canada, represented by the Minister of Employment "
							+ "and Social Development Canada. All rights reserved. This information has been provided by Employment and Social Development Canada and is reproduced with the permission of the Minister of Employment and Social Development Canada</TEXT>     </ENGLISH>     <FRENCH>      "
							+ " <TEXT>La Classification nationale des professions, Sa Majest&amp;eacute; la Reine du chef du Canada, repr&amp;eacute;sent&amp;eacute;e par le ministre d&apos;Emploi et D&amp;eacute;veloppement social Canada. Tous droits r&amp;eacute;serv&amp;eacute;s. Cette information est "
							+ "rendue disponible par Emploi et D&amp;eacute;veloppement social Canada et est reproduite avec la permission du ministre d&apos;Emploi et D&amp;eacute;veloppement social Canada.</TEXT>     </FRENCH>   </COPYRIGHT>   <NOC_WEBSITE>     <ENGLISH_URL>http://noc.esdc.gc.ca/English/noc/welcome.aspx?ver=16</ENGLISH_URL>     "
							+ "<FRENCH_URL>http://noc.esdc.gc.ca/Francais/CNP/Bienvenue.aspx?ver=16</FRENCH_URL>   </NOC_WEBSITE> </BRANDING> <RESULTS RETURNED=\"15\" CONTEXT_E=\"Results\"><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>0422</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T08:54:47\">"
							+ "School principals and administrators of elementary and secondary education</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4011</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-09-28T15:33:27\">University professors and lecturers</TITLE>"
							+ "</ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4012</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:07:55\">Post-secondary teaching and research assistants</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\">"
							+ "<CODE><NUMBER>4021</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:08:24\">College and other vocational instructors</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4031</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH>"
							+ "<TITLE PUBLISH_DATE=\"2011-01-21T14:08:40\">Secondary school teachers</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4032</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:09:01\">Elementary school and kindergarten teachers</TITLE></ENGLISH>"
							+ "</UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4033</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:09:17\">Educational counsellors</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4214</NUMBER></CODE>"
							+ "<UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:18:41\">Early childhood educators and assistants</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4215</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:19:07\">"
							+ "Instructors of persons with disabilities</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4413</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:50:35\">Elementary and secondary school teacher assistants</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE>"
							+ "<NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>5133</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:54:29\">Musicians and singers</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>5134</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\">"
							+ "<ENGLISH><TITLE PUBLISH_DATE=\"2001-01-01T00:00:00\">Dancers</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>5135</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:54:49\">Actors and comedians</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE>"
							+ "<NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>5136</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:55:17\">Painters, sculptors and other visual artists</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>5254</NUMBER></CODE>"
							+ "<UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-24T13:54:40\">Program leaders and instructors in recreation, sport and fitness</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE></RESULTS></NOCWS>");
			*/
			String content = "<NOCWS xmlns:nocws=\"NOCWebService\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://cnp-noc.services.gc.ca/WebServices/Service.asmx/Schema/NOCWS.xsd\"> <BRANDING>   <PRODUCT VERSION=\"2017\">     <ENGLISH>       <NAME WEB_LINK=\"http://noc.esdc.gc.ca/English/NOC/Welcome.aspx?ver=16\">National Occupational Classification</NAME>       <DESCRIPTION>         <PARAGRAPH>String</PARAGRAPH>       </DESCRIPTION>       <AUTHORITY WEB_LINK=\"https://www.canada.ca/en.html\">Government of Canada</AUTHORITY>     </ENGLISH>     <FRENCH>       <NAME WEB_LINK=\"http://noc.esdc.gc.ca/Francais/CNP/Bienvenue.aspx?ver=16\">Classification nationale des professions</NAME>       <DESCRIPTION>         <PARAGRAPH>String</PARAGRAPH>       </DESCRIPTION>       <AUTHORITY WEB_LINK=\"https://www.canada.ca/fr.html\">Gouvernement du Canada</AUTHORITY>     </FRENCH>     <SUPPORT>       <ENGLISH>http://noc.esdc.gc.ca/English/NOC/ContactUs.aspx?ver=16</ENGLISH>       <FRENCH>http://noc.esdc.gc.ca/Francais/CNP/ContactezNous.aspx?ver=16</FRENCH>     </SUPPORT>   </PRODUCT>   <COPYRIGHT>     <ENGLISH>       <TEXT>The National Occupational Classification, Her Majesty the Queen in Right of Canada, represented by the Minister of Employment and Social Development Canada. All rights reserved. This information has been provided by Employment and Social Development Canada and is reproduced with the permission of the Minister of Employment and Social Development Canada</TEXT>     </ENGLISH>     <FRENCH>       <TEXT>La Classification nationale des professions, Sa Majest&amp;eacute; la Reine du chef du Canada, repr&amp;eacute;sent&amp;eacute;e par le ministre d&apos;Emploi et D&amp;eacute;veloppement social Canada. Tous droits r&amp;eacute;serv&amp;eacute;s. Cette information est rendue disponible par Emploi et D&amp;eacute;veloppement social Canada et est reproduite avec la permission du ministre d&apos;Emploi et D&amp;eacute;veloppement social Canada.</TEXT>     </FRENCH>   </COPYRIGHT>   <NOC_WEBSITE>     <ENGLISH_URL>http://noc.esdc.gc.ca/English/noc/welcome.aspx?ver=16</ENGLISH_URL>     <FRENCH_URL>http://noc.esdc.gc.ca/Francais/CNP/Bienvenue.aspx?ver=16</FRENCH_URL>   </NOC_WEBSITE> </BRANDING> \r\n" + 
					"<RESULTS RETURNED=\"9\" CONTEXT_E=\"Results\">\r\n" + 
					"<NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>1243</NUMBER></CODE>\r\n" + 
					"<UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T09:39:22\">Medical administrative assistants</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE>\r\n" + 
					"<NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>1251</NUMBER></CODE>\r\n" + 
					"<UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-25T15:39:50\">Court reporters, medical transcriptionists and related occupations</TITLE></ENGLISH></UNIT_GROUP_TITLE>\r\n" + 
					"</NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>3012</NUMBER></CODE>\r\n" + 
					"<UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T13:44:39\">Registered nurses and registered psychiatric nurses</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE>\r\n" + 
					"<NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>3112</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T13:45:39\">General practitioners and family physicians</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>3121</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2001-01-01T00:00:00\">Optometrists</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>3122</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2001-01-01T00:00:00\">Chiropractors</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>3125</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T13:46:55\">Other professional occupations in health diagnosing and treating</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>3233</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-01-21T14:02:20\">Licensed practical nurses</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE><NOC_PROFILE CONTEXT_E=\"Descriptions\"><CODE><NUMBER>4011</NUMBER></CODE><UNIT_GROUP_TITLE CONTEXT_E=\"Unit Group\"><ENGLISH><TITLE PUBLISH_DATE=\"2011-09-28T15:33:27\">University professors and lecturers</TITLE></ENGLISH></UNIT_GROUP_TITLE></NOC_PROFILE></RESULTS></NOCWS>";
			//stringToDom(inputFile, content);
			InputSource input = new InputSource (new StringReader(content));
			SAXReader reader = new SAXReader();
			Document document = reader.read(input);

			@SuppressWarnings("unchecked")
			List<Node> nodes = document.selectNodes("/NOCWS/RESULTS/NOC_PROFILE");
			System.out.println(nodes.size());
			for (Node node : nodes) {
				System.out.print(
						"Code - " + node.selectSingleNode("CODE").selectSingleNode("NUMBER").getText() + ", ");
				System.out.println(node.selectSingleNode("UNIT_GROUP_TITLE").selectSingleNode("ENGLISH")
						.selectSingleNode("TITLE").getText());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} /*catch (SAXException | ParserConfigurationException | IOException
				| TransformerException e) {
			e.printStackTrace();
		}*/
	}
/*
	public static void stringToDom(File file, String xmlSource)
			throws SAXException, ParserConfigurationException, IOException, TransformerException {
		// Parse the given input
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

		// Write the parsed document to an xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
	}
	
	public void convertToXMLFile(File file, String xmlSource)
			throws SAXException, ParserConfigurationException, IOException, TransformerException {
		// Parse the given input
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

		// Write the parsed document to an xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
		System.out.println("/////NOCParser -> String to XML file Conversion Successfully done");
	}*/
}
