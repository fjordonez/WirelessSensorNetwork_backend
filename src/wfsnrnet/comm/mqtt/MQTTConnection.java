package wfsnrnet.comm.mqtt;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import wfsnrnet.util.ArrayUtils;

import com.ibm.mqtt.IMqttClient;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttNotConnectedException;
import com.ibm.mqtt.MqttPersistenceException;
import com.ibm.mqtt.MqttSimpleCallback;

public class MQTTConnection implements MqttSimpleCallback {

	// MQTT connection preferences
	public MQTTConfiguration mqttConfig;
	
    // connection to the message broker
    private IMqttClient mqttClient = null;
    
	private long mStartTime;
    
    public MQTTConnection(MQTTConfiguration configuration) throws MqttException 
    {
    	mqttConfig = configuration;
        String mqttConnSpec = "tcp://" + mqttConfig.brokerHostName() + "@" + mqttConfig.brokerPortNumber();
        try
        {       	
            // define the connection to the broker
            mqttClient = MqttClient.createMqttClient(mqttConnSpec, mqttConfig.usePersistence());
            // register this client app has being able to receive messages
            mqttClient.registerSimpleHandler(this);
        	
            if (connectToBroker())
            {          
                System.out.println(("MQTTConnection::MQTTConnection "+mqttConfig.clientId()+" connected to " + mqttConfig.brokerHostName() + " on topics [ " + ArrayUtils.toString(mqttConfig.topics()))+" ]");
                if (mqttConfig.topics().length > 0)
                	subscribeToTopic(mqttConfig.topics());

                SimpleDateFormat sdf = new SimpleDateFormat();
                System.out.println(" Date: "+sdf.format((Calendar.getInstance()).getTime()));
                
    			mStartTime = System.currentTimeMillis();	
            }
        }
        catch (MqttException e)
        {
            mqttClient = null;
            System.out.println("MQTTConnection::MQTTConnection Invalid connection parameters");
        }
    }
    
    public long startTime(){
    	return this.mStartTime;
    }
	
	/*
	 * Send a request to the message broker to be sent messages published with 
	 *  the specified topic name. Wildcards are allowed.	
	 */	
    public void subscribeToTopic(String[] topics)
    {

        if (isAlreadyConnected() == false)
        {
            // quick sanity check - don't try and subscribe if we don't have a connection
        	System.out.println("MQTTConnection::subscribeToTopic Unable to subscribe as we are not connected");
        }
        else
        {
            try
            {
                mqttClient.subscribe(topics, mqttConfig.qualitiesOfService());
            }
            catch (MqttNotConnectedException e)
            {
            	System.out.println("MQTTConnection::subscribeToTopic Subscription failed - MQTT not connected");
            }
            catch (IllegalArgumentException e)
            {
            	System.out.println("MQTTConnection::subscribeToTopic Subscription failed - illegal argument");
            }
            catch (MqttException e)
            {
            	System.out.println("MQTTConnection::subscribeToTopic Subscription failed - MQTT exception");
            }
        }
    }
	
    /*
     * Checks if the MQTT client thinks it has an active connection
     */
    private boolean isAlreadyConnected()
    {
        return ((mqttClient != null) && (mqttClient.isConnected() == true));
    }
    
    /*
     * (Re-)connect to the message broker
     */
    public boolean connectToBroker()
    {
        try
        {
            mqttClient.connect(mqttConfig.clientId(), mqttConfig.cleanStart(), mqttConfig.keepAliveSeconds());
            return true;
        }
        catch (MqttException e)
        {
            System.out.println("MQTTConnection::connectToBroker Unable to connect");
            return false;
        }
    }
    
    /*
     * Terminates a connection to the message broker.
     */
    public void disconnectFromBroker()
    {
        try
        {
            if (mqttClient != null)
            {
                mqttClient.disconnect();
            }
        }
        catch (MqttPersistenceException e)
        {
			System.out.println("MQTTConnection::disconnectFromBroker Disconnect failed - persistence exception");
        }
        finally
        {
            mqttClient = null;
        }

    }
    
	/*
	 * Sends a message to the message broker, requesting that it be published
	 *  to the specified topic.
	 */
    public boolean publishToTopic(String topicName, String message) 
	{		
        if (isAlreadyConnected() == false)	
        {
			System.out.println("MQTTConnection::publishToTopic No connection to public to");		
        	return false;
        }else
        {
	        try
	        {
	        	mqttClient.publish(topicName,message.getBytes(), mqttConfig.qualityOfService(), mqttConfig.retainedPublish());
	            return true;
	        }
	        catch (MqttException e)
	        {
	            System.out.println("MQTTConnection::publishToTopic Unable to publish message");
	            return false;
	        }
		}
	}	

	public void connectionLost() throws Exception 
	{
		System.out.println("MQTTConnection::connection Loss of connection");
		mqttClient = null;
	}

	/*
	 * Called when we receive a message from the message broker. 
	 */
	public void publishArrived(String arg0, byte[] arg1, int arg2, boolean arg3)
			throws Exception {	
	}   

}