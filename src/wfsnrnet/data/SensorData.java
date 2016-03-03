package wfsnrnet.data;

public class SensorData {

	private String placeID; 
	private int sensorAlias;
	private String hardwareID;
	private String sensorPlace;
	private boolean inverted;
	private boolean interval;
	private SensorType sensorType;
	private int debounceTime;

	public SensorData(){
		this.placeID = "";
		this.sensorAlias = -1;
		this.hardwareID = "";
		this.sensorPlace = "";
		this.inverted = false;
		this.interval = false;
		this.sensorType = SensorType.Novalue;
		this.debounceTime = 0;
	}
	
	public SensorData(int sensorAlias, String hardwareID, String sensorPlace){
		this.placeID = "";
		this.sensorAlias = sensorAlias;
		this.hardwareID = hardwareID;
		this.sensorPlace = sensorPlace;
		this.inverted = false;
		this.interval = false;
		this.sensorType = SensorType.Novalue;
		this.debounceTime = 0;
	}
	
	public SensorData(String placeID){
		this.placeID = placeID;
		this.sensorAlias = -1;
		this.hardwareID = "";
		this.sensorPlace = "";
		this.inverted = false;
		this.interval = false;		
		this.sensorType = SensorType.Novalue;
		this.debounceTime = 0;
	}
	
	public SensorData(String placeID, int sensorAlias, String hardwareID, String sensorPlace){
		this.placeID = placeID;
		this.sensorAlias = sensorAlias;
		this.hardwareID = hardwareID;
		this.sensorPlace = sensorPlace;
		this.inverted = false;
		this.interval = false;		
		this.sensorType = SensorType.Novalue;
		this.debounceTime = 0;
	}
	
	public String placeID(){
		 return this.placeID;
	}
	
	public void setPlaceID(String placeID){
		 this.placeID = placeID;
	}
	
	public int sensorAlias(){
		 return this.sensorAlias;
	}
	
	public void setSensorAlias(int sensorAlias){
		 this.sensorAlias = sensorAlias;
	}
	
	public String hardwareID(){
		 return this.hardwareID;
	}
	
	public void setHardwareID(String hardwareID){
		 this.hardwareID = hardwareID;
	}
	
	public String sensorPlace(){
		 return this.sensorPlace;
	}
	
	public void setSensorPlace(String sensorPlace){
		 this.sensorPlace = sensorPlace;
	}
	
	public boolean inverted(){
		return this.inverted;
	}
	
	public void setInverted(boolean inverted){
		this.inverted = inverted;
	}
	
	public boolean interval(){
		return this.interval;
	}
	
	public void setInterval(boolean interval){
		this.interval = interval;
	}
	
	public SensorType sensorType(){
		 return this.sensorType;
	}
	
	public void setDebouncetime(int debounceTime){
		this.debounceTime = debounceTime;
	}
	
	public int debounceTime(){
		return debounceTime;
	}
	
	public void setSensorType(String sensorType){
		if (sensorType.equals(""))
			this.sensorType = SensorType.Novalue;
		else
			if (SensorType.toType(sensorType) == SensorType.Error){
				throw new IllegalArgumentException("SensorData::setSensorType Unknow type of sensor: "+sensorType);
			}else
				this.sensorType = SensorType.toType(sensorType);
	}
	
	public String toString(){
		String ret = "  Sensor with alias "+sensorAlias+" and hardware Id '"+hardwareID+"'\n";
		ret+="    Place: "+placeID+"\n";
		ret+="    Sensor place: "+sensorPlace+"\n";
		ret+="    Inverted: "+inverted+"\n";
		ret+="    Type: "+sensorType+"\n";
		return ret;
	}
	
	public enum SensorType
	{
		KitchenHeat, Toilet, BathroomDoor, Kitchen, KitchenStorage, Outside, SleepDoor, Sleep, Bathroom, Novalue, Error;

	    public static SensorType toType( String type)
	    {
	        try {
	            return valueOf(type);
	        } 
	        catch (Exception e) {
	            return Error;
	        }
	    }   
	}
}
