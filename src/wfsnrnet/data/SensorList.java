package wfsnrnet.data;

import java.util.ArrayList;
import java.util.List;

public class SensorList {
	
	List<SensorData> list;
	
	public SensorList(){
		list = new ArrayList<SensorData>();
	}
	
	public void add(SensorData e){
		list.add(e);
	}
	
	public int size(){
		return list.size();
	}
	
	public SensorData get(int index){
		return list.get(index);
	}
	
	public SensorData find(int sensorAlias){
		for (SensorData e : list)
			if (e.sensorAlias() == sensorAlias)
				return e;
		return null;
	}
	
	public SensorData find(String hardwareID){
		for (SensorData e : list)
			if (e.hardwareID().equals(hardwareID))
				return e;
		return null;
	}
	
	public String toString(){
		String ret = " List with "+size()+" element(s) \n";
		for (SensorData e : list)
			ret += e.toString();
		return ret;
	}
	
}
