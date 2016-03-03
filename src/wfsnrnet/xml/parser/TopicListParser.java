package wfsnrnet.xml.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wfsnrnet.data.TopicData;
import wfsnrnet.data.TopicList;
import wfsnrnet.xml.XMLSerial;

public class TopicListParser {

	public static TopicList parse(String fileName){
		XMLSerial xmlSerial=new XMLSerial();
		TopicList topicList = new TopicList();
		try{
		    Node mainNode = xmlSerial.load(fileName);
		    if (mainNode==null)
		    	System.out.println("TopicListParser::parse Unable to parse file "+fileName);
		    else{
		    	Node listNode = xmlSerial.getChild(mainNode,"TopicsList");
		    	if (listNode!=null){
			    	NodeList topicsNode = listNode.getChildNodes();
			    	for (int i = 0; i < topicsNode.getLength(); i++){
			    	    Node topicNode = topicsNode.item(i);
			    	    if (topicNode.getNodeName().equals("Topic"))
			    	    {
			    	    	TopicData topicData = new TopicData();
							
				    		String name = xmlSerial.getAttribute(topicNode,"name");
			    	    	if (name!=null)
			    	    		topicData.setName(name);
			    	    	
				    		String alias = xmlSerial.getAttribute(topicNode,"alias");
			    	    	if (alias!=null)
			    	    		topicData.setAlias(alias);
						    
						    topicList.add(topicData);

			    	    }
			    		
			    	}
			    		
		    	}
		    }
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return topicList;
	}
}
