package wfsnrnet.comm.mqtt;

import wfsnrnet.util.ArrayUtils;

import com.ibm.mqtt.MqttPersistence;

public class MQTTConfiguration {

    // defaults - this sample uses very basic defaults for the interactions with message brokers
		// the port at which the broker is running.
		private int	MQTT_BROKER_PORT_NUM = 1883;
		
		// Let's not use the MQTT persistence.
		private MqttPersistence	MQTT_PERSISTENCE = null;   
		
		// We don't need to remember any state between the connections, so we use a clean start. 
		private boolean MQTT_CLEAN_START          = true;
	     
		// Set quality of services to 0 (at most once delivery), since we don't want push notifications 
		// arrive more than once. However, this means that some messages might get lost (delivery is not guaranteed)	
		private int[] MQTT_QUALITIES_OF_SERVICE = { 0 } ;
	
	// the IP address, where your MQTT broker is running.
	private  String MQTT_HOST = "127.0.0.1";
	
	// MQTT client ID, which is given the broker.
	// It can be used to run push notifications for multiple apps with one MQTT broker. 
	public String MQTT_CLIENT_ID = "default";
	
	// Let's set the internal keep alive for MQTT to 15 mins. I haven't tested this value much. It could probably be increased.
	private short MQTT_KEEP_ALIVE  = 60 * 15;
	
	private String[] MQTT_TOPICS = { } ;
	
	private int MQTT_QUALITY_OF_SERVICE = 0;
	
	// The broker should not retain any messages.
	private boolean MQTT_RETAINED_PUBLISH = false;
	
	public MQTTConfiguration(){
	}
	
	public int brokerPortNumber(){
		return this.MQTT_BROKER_PORT_NUM;
	}
	
	public void setBrokerPortNumber(int brokerPortNumber){
		this.MQTT_BROKER_PORT_NUM = brokerPortNumber;
	}
	
	public MqttPersistence usePersistence(){
		return this.MQTT_PERSISTENCE;
	}
	
	public boolean cleanStart(){
		return this.MQTT_CLEAN_START;
	}
	
	public void setCleanStart(boolean cleanStart){
		this.MQTT_CLEAN_START = cleanStart;
	}
	
	public int[] qualitiesOfService(){
		return this.MQTT_QUALITIES_OF_SERVICE;
	}
	
	public void setQualitiesOfService(int[] qualitiesOfService){
		this.MQTT_QUALITIES_OF_SERVICE = qualitiesOfService.clone();
	}
	
	public String brokerHostName(){
		return this.MQTT_HOST;
	}
	
	public void setBrokerHostName(String brokerHostName){
		this.MQTT_HOST = brokerHostName;
	}
	
	public String clientId(){
		return this.MQTT_CLIENT_ID;
	}
	
	public void setClientId(String clientId){
		this.MQTT_CLIENT_ID = clientId;
	}
	
	public short keepAliveSeconds(){
		return this.MQTT_KEEP_ALIVE;
	}
	
	public void setKeepAliveSeconds(short keepAliveSeconds){
		this.MQTT_KEEP_ALIVE = keepAliveSeconds;
	}
	
	public String[] topics(){
		return this.MQTT_TOPICS;
	}
	
	public void setTopics(String[] topics){
		this.MQTT_TOPICS = topics.clone();
	}
	
	public int qualityOfService(){
		return this.MQTT_QUALITY_OF_SERVICE;
	}
	
	public void setQualityOfService(int qualityOfService){
		this.MQTT_QUALITY_OF_SERVICE = qualityOfService;
	}
	
	public boolean retainedPublish(){
		return this.MQTT_RETAINED_PUBLISH;
	}
	
	public void  setRetainedPublish(boolean retainedPublish){
		this.MQTT_RETAINED_PUBLISH = retainedPublish;
	}
	
	public String toString(){
		String ret = " MQTT connection to host "+brokerHostName()+":"+brokerPortNumber()+" with id '"+clientId()+"'\n";
		ret+="  Using persistence: "+usePersistence()+"\n";
		ret+="  Clean start: "+cleanStart()+"\n";
		ret+="  Keep alive interval: "+keepAliveSeconds()+"\n";
		ret+="  Publishing retained: "+retainedPublish()+"\n";
		ret+="  Quality of service: "+qualityOfService()+"\n";
		ret+="  Topics: "+ArrayUtils.toString(topics())+"\n";
		ret+="  Qualities of service: "+ArrayUtils.toString(qualitiesOfService())+"\n";
		return ret;
	}
}
