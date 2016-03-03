package wfsnrnet.modules;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import wfsnrnet.comm.mqtt.MQTTConfiguration;
import wfsnrnet.comm.mqtt.MQTTModule;
import wfsnrnet.data.DelayedNotification;
import wfsnrnet.data.EventMessage;
import wfsnrnet.data.SensorData;
import wfsnrnet.data.SensorList;
import wfsnrnet.data.TopicList;
import wfsnrnet.io.db.SensorDataTableHandler;
import wfsnrnet.io.db.SensorEventsTableHandler;
import wfsnrnet.io.db.sql.DBConnection;
import wfsnrnet.io.db.sql.ParamsDB;
import wfsnrnet.xml.parser.MQTTConfigurationParser;

public class EventHandlerModule extends MQTTModule {
	
    private String eventHandlerMQTTConfigFile;
    private String eventListenerMQTTConfigFile;
    private String notificationTopic;
    private SensorList sensorList;
    private TopicList topicList;
    private List<DelayedNotification> delayedNotifications;
    private MQTTConfiguration eventHandlerMQTTConfig;
    protected BlockingQueue<String> eventQueue = null;
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yy HH:mm:ss:SSSS");
  
	
    public EventHandlerModule(SensorList sensorList, String eventHandlerMQTTConfigFile, String eventListenerMQTTConfigFile, TopicList topicList, List<DelayedNotification> delayedNotifications)
    {
    	eventQueue = new LinkedBlockingQueue<String>();
    	this.eventHandlerMQTTConfigFile = eventHandlerMQTTConfigFile;
    	this.eventListenerMQTTConfigFile = eventListenerMQTTConfigFile;
    	this.sensorList = sensorList;
    	this.topicList = topicList;
    	this.delayedNotifications = delayedNotifications;
    	this.notificationTopic = topicList.findName("sensorNotifications");
    }
	
    public void start() throws Exception
    {
		new Thread ( new EventListener(eventListenerMQTTConfigFile, eventQueue, topicList) ).start();
		
		System.out.println("EventHandlerModule::start Parsing file "+eventHandlerMQTTConfigFile);
		eventHandlerMQTTConfig = MQTTConfigurationParser.parse(eventHandlerMQTTConfigFile,topicList);
		System.out.println("EventHandlerModule::start Connection parameters ->");
		System.out.println(eventHandlerMQTTConfig.toString());
		startMQTT(eventHandlerMQTTConfig);
		
        try {
            while (true) 
            {
            	String eventMessage = eventQueue.take();
        		DBConnection connection = null;
        		try {
        			connection = new DBConnection(new ParamsDB("root","1234","WifiSensorNetwork"));
        			EventMessage eventMessageData = new EventMessage();
        			eventMessageData.parse(eventMessage);
        			//System.out.println(eventMessageData.toString());
        			SensorEventsTableHandler.insert(connection, eventMessageData.id(), Integer.parseInt(eventMessageData.value()), sdf.parse(eventMessageData.date()));
        			processEventMessage(connection, eventMessageData);
        		}
        		finally {
        			connection.closeConnection();
        		}
            }
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        
        endMQTT();
    }
    
    private void processEventMessage(DBConnection connection, EventMessage eventMsgData) throws ParseException, NumberFormatException, SQLException{
    	String dataMsg = "";
		try{
	    	for (int i=0;i<sensorList.size();i++){
	    		SensorData sensorData = sensorList.get(i);
	    		if (sensorData.hardwareID().equals(eventMsgData.id())){  //para el simulador
	    		//if (sensorData.sensorAlias() == Integer.parseInt(eventMsgData.id())){	
	    			dataMsg += sensorData.hardwareID()+";";
	    			dataMsg += sensorData.sensorPlace()+";";
	    			dataMsg += sensorData.sensorType()+";";
	    			
	    			//System.out.println("EventHandlerModule::processEventMessage To publish message: " + dataMsg);
	    			
	    			int value = -1;
					try{
						value = Integer.parseInt(eventMsgData.value());
					}catch (NumberFormatException e){
						System.out.println("EventHandlerModule::processEventMessage -value- Cant parse '"+eventMsgData.value()+"'");
					}
	    			if (sensorData.inverted()){
	    				switch (value) {
		    	            case 0:  value = 1; break;
		    	            case 1:  value = 0; break;
		    	            default: throw new IllegalArgumentException("EventHandlerModule::processEventMessage Incorrect value of the sensor -"+value); 
	    				}
	    			}
	    			
	    			if (!sensorData.interval()) 
	    				value = 1;
	    			
		    		dataMsg += value+";";
		    		dataMsg += eventMsgData.date();
		    		
		    		//System.out.println("EventHandlerModule::processEventMessage To publish message: " + dataMsg);
		    			
	    			if (sensorData.debounceTime() > 0){
	    				DelayedNotification delayNotif = new DelayedNotification((Calendar.getInstance()).getTimeInMillis(),sensorData.debounceTime(),sensorData,value,eventMsgData.date());
	    				//System.out.println(delayNotif.toString());
	    				delayedNotifications.add(delayNotif);
	    				//delayedNotifications.add(new DelayedNotification((Calendar.getInstance()).getTimeInMillis(),sensorData.debounceTime(),sensorData,value,eventMsgData.date()));
	    			}else{
	    				SensorDataTableHandler.insert(connection, sensorData, value, sdf.parse(eventMsgData.date()));
	    				publishMessage(notificationTopic,dataMsg);
	    			}

	    		}
	    	}
		}catch (NumberFormatException e){
			System.out.println("EventHandlerModule::processEventMessage -sensorAlias- Cant parse '"+eventMsgData.id()+"'");
		}

    }
	
    class EventListener extends MQTTModule implements Runnable {

    	private MQTTConfiguration eventListenerMQTTConfig;
    	private BlockingQueue<String> eventQueue = null;

    	public EventListener(String mqttConfigFile, BlockingQueue<String> eventQueue, TopicList topicList){
    		this.eventQueue = eventQueue;
			System.out.println("EventListener::EventListener Parsing file "+mqttConfigFile);
			eventListenerMQTTConfig = MQTTConfigurationParser.parse(mqttConfigFile,topicList);
			System.out.println("EventListener::EventListener Connection parameters ->");
			System.out.println(eventListenerMQTTConfig.toString());
    	}
    	
		@Override 	
		public void run() 
		{
			startMQTT(eventListenerMQTTConfig);
		}
		
		protected void processMessage(String message) 
		{
	    	try {
				//System.out.println("EventListener::processMessage Got message: " + message);
				eventQueue.put(message);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

		}
    	
    }
    
    
}
