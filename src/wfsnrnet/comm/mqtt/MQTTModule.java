package wfsnrnet.comm.mqtt;

import com.ibm.mqtt.MqttException;

public class MQTTModule {
	
	private MQTTClient mqttClient;
	
	protected synchronized void startMQTT(MQTTConfiguration mqttConfig) {		
		System.out.println("MQTTModule::start Connecting to broker...");
		try {
			mqttClient = new MQTTClient(mqttConfig);
		} catch (MqttException e) {
			System.out.println("MQTTModule::connectMqtt Exception: " + (e.getMessage() != null ? e.getMessage() : "NULL"));
		}
	}
	
	protected synchronized void endMQTT() 
	{	
		mqttClient.disconnectFromBroker();
	}
	
	protected void processMessage(String message) {
		System.out.println("MQTTModule::processMessage Got message: " + message);
	}
	
	protected void publishMessage(String topicName, String message) {
		mqttClient.publishToTopic(topicName, message);
	}

	private class MQTTClient extends MQTTConnection {
		
		public MQTTClient(MQTTConfiguration configuration)
				throws MqttException 
		{
			super(configuration);
		}

		/*
		 * Called when we receive a message from the message broker. 
		 */
		public void publishArrived(String topicName, byte[] payload, int qos, boolean retained) 
		{
			String s = new String(payload);
			processMessage(s);	
		}

	}
}
