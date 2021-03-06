package wfsnrnet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import wfsnrnet.data.TopicList;
import wfsnrnet.io.LogFile;
import wfsnrnet.modules.SensorNetworkModule;
import wfsnrnet.xml.parser.TopicListParser;


public class SensorNetworkInterface {
	
	static final String LOG_PROPERTIES_FILE = "./config/SNI.Log4J.Properties";
	static final SimpleDateFormat sdf = new SimpleDateFormat();
	private static String LOG_PATH = "./log/SensorNetwork.log";
	
	
	public static void main(String[] args) throws Exception {
		
		TopicList topicList;
		
		if (args.length < 3){
			System.err.println("Use: java SensorNetworkInterface Network_MQTT_Config_File Listener_MQTT_Config_File MQTT_Topic_List [Interface_Port] &");
			throw new Exception();	
		}
		
		LogFile.setLogFile(LOG_PATH);
	    Properties logProperties = new Properties();
	    try
	    {
	      logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
	      (new File(logProperties.getProperty("log4j.appender.file.File"))).delete();
	      PropertyConfigurator.configure(logProperties);
	    }
	    catch(IOException e)
	    {
	      throw new RuntimeException("SensorNetworkInterface::main Unable to load logging property " + LOG_PROPERTIES_FILE);
	    }
	    
		System.out.println("SensorNetworkInterface::main Parsing topics list file "+args[2]);
		topicList = TopicListParser.parse(args[2]);
	    
	    String interfacePortName = "/dev/ttyUSB0"; //default
	    if (args.length == 4) //a portname was defined
	    	interfacePortName = args[3];
		
		System.out.println("SensorNetworkInterface::main Starting sensor network module");
		System.out.println("SensorNetworkInterface::main Connecting on port "+interfacePortName);
		SensorNetworkModule sensorNetworkModule=new SensorNetworkModule(args[0],args[1],topicList);
		sensorNetworkModule.start(interfacePortName);

	}
	
	


}
