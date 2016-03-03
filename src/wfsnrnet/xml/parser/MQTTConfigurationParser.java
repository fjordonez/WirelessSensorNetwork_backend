package wfsnrnet.xml.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wfsnrnet.comm.mqtt.MQTTConfiguration;
import wfsnrnet.data.TopicList;
import wfsnrnet.util.ListUtils;
import wfsnrnet.xml.XMLSerial;

public class MQTTConfigurationParser {

	public static MQTTConfiguration parse(String fileName, TopicList topicList){
		XMLSerial xmlSerial=new XMLSerial();
		MQTTConfiguration mqttConfig = new MQTTConfiguration();
		try{
		    Node mainNode = xmlSerial.load(fileName);
		    if (mainNode==null)
		    	System.out.println("MQTTConfigurationParser::parse Unable to parse file "+fileName);
		    else{
		    	Node mqttNode = xmlSerial.getChild(mainNode,"MQTTConfiguration");
		    	if (mqttNode!=null){
			    	String brokerHostName=xmlSerial.getAttribute(mqttNode,"brokerHostName");
			    	if (brokerHostName!=null)
			    		mqttConfig.setBrokerHostName(brokerHostName);
			    	
			    	String brokerPortNumber=xmlSerial.getAttribute(mqttNode,"brokerPortNumber");
					try{
						mqttConfig.setBrokerPortNumber(Integer.parseInt(brokerPortNumber));
					}catch (NumberFormatException e){
						System.out.println("MQTTConfigurationParser::parse -brokerPortNumber- Cant parse '"+brokerPortNumber+"'");
					}	
					
			    	String clientId=xmlSerial.getAttribute(mqttNode,"clientId");
			    	if (clientId!=null)
			    		mqttConfig.setClientId(clientId);
			    	
			    	Node subscriptionsNode = xmlSerial.getChild(mqttNode,"Subscriptions");
				    if (subscriptionsNode!=null)
				    {
				    	NodeList topicsNode = subscriptionsNode.getChildNodes();
				    	List<String> topicsNames = new ArrayList<String>();
				    	List<Integer> topicsQualities = new ArrayList<Integer>();
				    	for (int i = 0; i < topicsNode.getLength(); i++)
				    	{
				    	    Node topicNode = topicsNode.item(i);
				    	    if (topicNode.getNodeName().equals("Topic"))
				    	    {
					    		String topicAlias = xmlSerial.getAttribute(topicNode,"alias");
						    	if (topicAlias!=null)
						    		if (topicList.findName(topicAlias).equals(""))
						    			throw new IllegalArgumentException("MQTTConfiguration::parse Topic alias '"+topicAlias+"' not found parsing "+fileName);
						    		else
						    			topicsNames.add(topicList.findName(topicAlias));
						    	
					    		String topicQuality = xmlSerial.getAttribute(topicNode,"Quality");
								try{
							    	if (topicQuality!=null)
							    		topicsQualities.add(new Integer(Integer.parseInt(topicQuality)));
								}catch (NumberFormatException e){
									System.out.println("MQTTConfigurationParser::parse -topicQuality- Cant parse '"+topicQuality+"'");
								}

				    	    }
				    	}
				    	mqttConfig.setTopics(ListUtils.toArray_String(topicsNames));
				    	mqttConfig.setQualitiesOfService(ListUtils.toArray_Integer(topicsQualities));
				    }
				    
			    	Node qualityOfServiceNode = xmlSerial.getChild(mqttNode,"QualityOfService");
				    if (qualityOfServiceNode!=null)
				    {
				    	String value=xmlSerial.getAttribute(qualityOfServiceNode,"value");
						try{
							mqttConfig.setQualityOfService(Integer.parseInt(value));
						}catch (NumberFormatException e){
							System.out.println("MQTTConfigurationParser::parse -value- Cant parse '"+value+"'");
						}
				    }
				    
			    	Node keepAliveNode = xmlSerial.getChild(mqttNode,"KeepAlive");
				    if (keepAliveNode!=null)
				    {
				    	String seconds=xmlSerial.getAttribute(keepAliveNode,"seconds");
						try{
							mqttConfig.setKeepAliveSeconds((short) Integer.parseInt(seconds));
						}catch (NumberFormatException e){
							System.out.println("MQTTConfigurationParser::parse -seconds- Cant parse '"+seconds+"'");
						}
				    }
				    
			    	Node cleanStartNode = xmlSerial.getChild(mqttNode,"ENABLE_CLEAN_START");
				    if (cleanStartNode!=null)
				    	mqttConfig.setCleanStart(true);
				    else
				    	mqttConfig.setCleanStart(false);
				    
			    	Node retainedPublishNode = xmlSerial.getChild(mqttNode,"ENABLE_RETAINED_PUBLISH");
				    if (retainedPublishNode!=null)
				    	mqttConfig.setRetainedPublish(true);
				    else
				    	mqttConfig.setRetainedPublish(false);

		    	}

		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return mqttConfig;
	}
}
