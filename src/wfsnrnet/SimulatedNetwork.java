package wfsnrnet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import wfsnrnet.comm.mqtt.MQTTConfiguration;
import wfsnrnet.comm.mqtt.MQTTModule;
import wfsnrnet.data.TopicList;
import wfsnrnet.io.db.sql.DBConnection;
import wfsnrnet.io.db.sql.ParamsDB;
import wfsnrnet.xml.parser.MQTTConfigurationParser;
import wfsnrnet.xml.parser.TopicListParser;

public class SimulatedNetwork extends MQTTModule {

	private MQTTConfiguration SimNetMQTTConfig;
	private ParamsDB paramsDB = null;
	TopicList topicList = null;
	private DateFormat dfm = new SimpleDateFormat("MMM d HH:mm:ss 'CEST' yyyy");
	static final SimpleDateFormat sdfEvent = new SimpleDateFormat("dd/M/yy HH:mm:ss:SSS");
	
	public SimulatedNetwork(String SimNetMQTTConfigFile,TopicList topicList) throws Exception{
		this.topicList = topicList;
		SimNetMQTTConfig = MQTTConfigurationParser.parse(SimNetMQTTConfigFile,topicList);
		paramsDB = new ParamsDB("root","1234","SensorNetwork");
	}
		
	public void start() throws Exception{
		
    	System.out.println("Connecting to MQTT broker...");
	super.startMQTT(SimNetMQTTConfig);

	DBConnection connection = null;
	try {
		System.out.println("Connecting to database...");
		connection = new DBConnection(paramsDB);

            Statement stmt = null;
            ResultSet rsQuery = null;
            int eventID = 1;
			Calendar tableCal_a = null;
			Calendar tableCal_b = Calendar.getInstance();
		    Calendar myCal = null;
		    long remSecs=0;
            
            while(true){
                try{
                	stmt = connection.getStatement();
                	rsQuery = stmt.executeQuery("SELECT * FROM `DIGITAL-Event` WHERE eventID="+eventID);
                	if (rsQuery.first()){
        				String sensorID = rsQuery.getString("sensorID");       				
	        				Scanner scanner = new Scanner(sensorID);
	        			    scanner.useDelimiter("_");
	        			    scanner.next();
	        			    String Id = scanner.next();
        				String value = rsQuery.getString("value");
        				String dateString = rsQuery.getString("date");
        				scanner.close();
        				if (tableCal_a==null){
        					tableCal_a = Calendar.getInstance();
        					tableCal_a.setTime(dfm.parse(dateString.substring(4)));
        					tableCal_b.setTime(tableCal_a.getTime());
        					myCal = Calendar.getInstance();
        					remSecs = remainingSecs(tableCal_a.get(Calendar.SECOND),myCal.get(Calendar.SECOND));
        				}else{
        					tableCal_a.setTime(tableCal_b.getTime());
        					tableCal_b.setTime(dfm.parse(dateString.substring(4)));
        					remSecs = ( tableCal_b.getTimeInMillis() - tableCal_a.getTimeInMillis() ) / 1000;
        				}
        				
        				myCal.setTimeInMillis( myCal.getTimeInMillis() + (remSecs*1000) );
        				remSecs = 10; 
        				Thread.currentThread().sleep(remSecs*1000);
        				
        				String topic = topicList.findName("sensorEvents");
        				String message = "digitalInput;"+Id+";"+value+"_"+sdfEvent.format((Calendar.getInstance()).getTime());           				
        				
        				//System.out.println("Publishing message ("+message+") into topic "+topic);
        				publishMessage(topic, message);
                	}
                	
                }catch(SQLException e) {
                    throw(e);
                }finally{
	                if (rsQuery != null) {
	                    try {
	                    	rsQuery.close();
	                    } catch (SQLException sqlEx) { } 
	                    rsQuery = null;
	                }
	            	if (stmt != null) {
	                    try {
	                        stmt.close();
	                    } catch (SQLException sqlEx) { } 
	                    stmt = null;
	                }
	            }
                eventID++;
                
            }

		}
		finally {
		   connection.closeConnection();
		}
		
	}
	
	private static int remainingSecs(int secsA, int secsB){
		int ret = 0;
		if (secsB>secsA)
			ret =  secsA + (60 - secsB);
		else{
			if (secsA>secsB)
				ret =  secsA-secsB;
			else
				ret = 0;
		}
		return ret;
	}
	
	public static void main(String[] args) throws Exception {
		
		String SIMNET_MQTT_PATH = "./config/SimulatedNetwork_MQTT.xml";
		String TOPIC_LIST_PATH = "./config/TopicList.xml";
		TopicList topicList = TopicListParser.parse(TOPIC_LIST_PATH);
		SimulatedNetwork simNet = new SimulatedNetwork(SIMNET_MQTT_PATH,topicList);
		simNet.start();
		
	}

}
