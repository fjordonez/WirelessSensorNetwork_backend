package wfsnrnet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import wfsnrnet.data.DelayedNotification;
import wfsnrnet.data.SensorList;
import wfsnrnet.data.TopicList;
import wfsnrnet.io.LogFile;
import wfsnrnet.io.db.SensorsTableHandler;
import wfsnrnet.io.db.sql.DBConnection;
import wfsnrnet.io.db.sql.ParamsDB;
import wfsnrnet.modules.DelayedNotificationsHandlerModule;
import wfsnrnet.modules.EventHandlerModule;
import wfsnrnet.xml.parser.SensorListParser;
import wfsnrnet.xml.parser.TopicListParser;

public class WifiSensorNetwork {
	
	private static String LOG_PATH = "./log/WifiSensorNetwork.log";
	private static SimpleDateFormat sdf = new SimpleDateFormat();

	public static void main(String[] args) throws Exception {
		
		SensorList sensorList;
		TopicList topicList;
		
		if (args.length < 4){
			System.err.println("Use: java WifiSensorNetwork Sensor_List_File EventHandler_MQTT_Config_File EventListener_MQTT_Config_File MQTT_Topic_List &");
			throw new Exception();	
		}
		
		LogFile.setLogFile(LOG_PATH);
	        System.out.println("WifiSensorNetwork::main Starting monitor: "+sdf.format((Calendar.getInstance()).getTime()));
		System.out.println("WifiSensorNetwork::main Parsing sensors list file "+args[0]);
		sensorList = SensorListParser.parse(args[0]);
		System.out.println("WifiSensorNetwork::main Sensors list ->");
		System.out.println(sensorList.toString());
		System.out.println("WifiSensorNetwork::main Parsing topics list file "+args[3]);
		topicList = TopicListParser.parse(args[3]);

		DBConnection connection = null;
		try {
			connection = new DBConnection(new ParamsDB("root","1234","WifiSensorNetwork"));
			SensorsTableHandler.insert(connection, sensorList);
		}
		finally {
			connection.closeConnection();
		}
		
		List<DelayedNotification> delayedNotifications = Collections.synchronizedList(new ArrayList<DelayedNotification>());
		
		int delayCheckingTimeMillis = 1000;
		System.out.println("SensorNetworkInterface::main Starting debounce time handler module");
	        (new Timer()).schedule(new DelayedNotificationsHandlerModule(args[1],topicList,delayedNotifications), 0, delayCheckingTimeMillis);  
		
		System.out.println("SensorNetworkInterface::main Starting sensor network module");
		EventHandlerModule eventHandlerModule=new EventHandlerModule(sensorList,args[1],args[2],topicList,delayedNotifications);
		eventHandlerModule.start();

	}

}
