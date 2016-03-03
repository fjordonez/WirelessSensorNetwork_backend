package wfsnrnet.modules;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import wfsnrnet.comm.mqtt.MQTTConfiguration;
import wfsnrnet.comm.mqtt.MQTTModule;
import wfsnrnet.data.DelayedNotification;
import wfsnrnet.data.TopicList;
import wfsnrnet.io.db.SensorDataTableHandler;
import wfsnrnet.io.db.sql.DBConnection;
import wfsnrnet.io.db.sql.ParamsDB;
import wfsnrnet.xml.parser.MQTTConfigurationParser;

public class DelayedNotificationsHandlerModule extends TimerTask {

    private String notificationTopic;
    private DelayedNotificationsPublisher delayedNotfPub;
    private TopicList topicList;
    private List<DelayedNotification> delayedNotifications;
    DBConnection connection = null;
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yy HH:mm:ss:SSSS");
	
	public DelayedNotificationsHandlerModule(String delayedNotfPubMQTTConfigFile, TopicList topicList, List<DelayedNotification> delayedNotifications) throws Exception 
	{
		this.notificationTopic = topicList.findName("sensorNotifications");
    	this.topicList = topicList;
    	this.delayedNotifications = delayedNotifications;
		this.delayedNotfPub = new DelayedNotificationsPublisher(delayedNotfPubMQTTConfigFile);
		try {
			connection = new DBConnection(new ParamsDB("root","1234","WifiSensorNetwork"));
		}
		finally {
			//connection.closeConnection();
		}
		delayedNotfPub.start();
	}
	
	@Override
	public void run() {
		
		boolean del;
		//System.out.println();
		//System.out.println("List");
		
		long timeMillis = (Calendar.getInstance()).getTimeInMillis();
		
		for (int i=0; i<delayedNotifications.size(); i++){
			
			del = false;
			
			DelayedNotification dNi = delayedNotifications.get(i);	
			//System.out.print("  "+dNi.sensorData().hardwareID()+";"+dNi.value()+";"+dNi.date());

			if (dNi.timestamp() + dNi.millis() >= timeMillis){ //not to be published yet
				//if ( dNi.sensorData().interval() ){
					for (int j=i+1; j<delayedNotifications.size(); j++){
						DelayedNotification dNj = delayedNotifications.get(j);
						if ( dNj.sensorData().hardwareID().equals(dNi.sensorData().hardwareID()) ){
							if (dNj.value() == 1){
								delayedNotifications.remove(j);j--;
								//System.out.println("del("+j+")");
							}
							if (dNi.value() == 0) del = true;			
						}
					}
					if (del) {
						delayedNotifications.remove(i);i--;		
						//System.out.println("del("+i+")");
					}
				//}
			}else{ //time to publish
				
				//System.out.println(" +");
				try{
					//////PETA
					
					//SensorDataTableHandler.insert(connection, sensorData, value, sdf.parse(eventMsgData.date()));
					
					SensorDataTableHandler.insert(connection, dNi.sensorData(), dNi.value(), sdf.parse(dNi.date()));
					String dataMsg = dNi.sensorData().hardwareID()+";";
					dataMsg += dNi.sensorData().sensorPlace()+";";
					dataMsg += dNi.sensorData().sensorType()+";";
					dataMsg += dNi.value()+";";
					dataMsg += dNi.date();
					delayedNotfPub.publishMessage(dataMsg);
					delayedNotifications.remove(i);i--;
				}catch (Exception e){
					e.printStackTrace();
				}
				
			}
			
		}
	}
	
    class DelayedNotificationsPublisher extends MQTTModule {

    	private MQTTConfiguration delayedNotfPubMQTTConfig;

    	public DelayedNotificationsPublisher(String mqttConfigFile){      	
			System.out.println("DelayedNotificationsPublisher::DelayedNotificationsPublisher Parsing file "+mqttConfigFile);
			delayedNotfPubMQTTConfig = MQTTConfigurationParser.parse(mqttConfigFile,topicList);
			delayedNotfPubMQTTConfig.setClientId("delayedNotifPublisher");
			System.out.println("DelayedNotificationsPublisher::DelayedNotificationsPublisher Connection parameters ->");
			System.out.println(delayedNotfPubMQTTConfig.toString());
    	}
    	
		public void start() 
		{
			startMQTT(delayedNotfPubMQTTConfig);
		}
		
		protected void publishMessage(String message) 
		{
			//System.out.println("    Publicando "+message+" en "+notificationTopic);
			publishMessage(notificationTopic,message);
		}
    	
    }
    
}
