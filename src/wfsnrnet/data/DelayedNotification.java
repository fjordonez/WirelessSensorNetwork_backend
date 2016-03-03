package wfsnrnet.data;

public class DelayedNotification {

	private long timestamp;
	private int millis;
	private SensorData sensorData;
	private int value;
	private String date;
	
	public DelayedNotification(long timestamp, int millis, SensorData sensorData, int value, String date){
		this.timestamp = timestamp;
		this.millis = millis;
		this.sensorData = sensorData;
		this.value = value;
		this.date = date;
	}
	
	public long timestamp(){
		return timestamp;
	}
	
	public int millis(){
		return millis;
	}

	public SensorData sensorData(){
		return sensorData;
	}
	
	public int value(){
		return value;
	}
	
	public String date(){
		return date;
	}
	
	public String toString(){
		String ret = "  Delayed notification with: \n";
		ret+="    TimeStamp: "+timestamp+"\n";
		ret+="    Millis: "+millis+"\n";
		ret+="    Sensor Data: \n";
		ret+= sensorData.toString(); 
		ret+="    Value: "+value+"\n";
		ret+="    Date: "+date+"\n";
		return ret;
	}
}
