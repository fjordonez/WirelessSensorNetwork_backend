package wfsnrnet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import wfsnrnet.comm.mqtt.MQTTConfiguration;
import wfsnrnet.comm.mqtt.MQTTModule;
import wfsnrnet.data.TopicList;
import wfsnrnet.xml.parser.MQTTConfigurationParser;
import wfsnrnet.xml.parser.TopicListParser;

public class Subscriber extends MQTTModule{

	private MQTTConfiguration SubscriberMQTTConfig;
	static final SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yy HH:mm:ss:SSS");
	
	public Subscriber(String subscriberMQTTConfigFile, TopicList topicList){
    	System.out.println("Parsing MQTT config file: "+subscriberMQTTConfigFile);
		SubscriberMQTTConfig = MQTTConfigurationParser.parse(subscriberMQTTConfigFile,topicList);
		System.out.println("Connection parameters ->");
		System.out.println(SubscriberMQTTConfig.toString());
	}
	
	protected void start()
    {
    	System.out.println("Connecting to MQTT broker...");
		super.startMQTT(SubscriberMQTTConfig);
    }
    
	protected void processMessage(String message) {
		System.out.println("["+sdf.format((Calendar.getInstance()).getTime())+"]Got message: " + message);
	}
    
	public static void main(String[] args) throws Exception {
		
		String SUBSCRIBER_MQTT_PATH = "./config/Subscriber_MQTT.xml";
		String TOPIC_LIST_PATH = "./config/TopicList.xml";
		TopicList topicList = TopicListParser.parse(TOPIC_LIST_PATH);
		Subscriber subscriber = new Subscriber(SUBSCRIBER_MQTT_PATH, topicList);
		subscriber.start();

	}


}
