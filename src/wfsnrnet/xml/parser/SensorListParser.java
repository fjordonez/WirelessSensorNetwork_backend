package wfsnrnet.xml.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wfsnrnet.data.SensorData;
import wfsnrnet.data.SensorList;
import wfsnrnet.xml.XMLSerial;

public class SensorListParser {

	public static SensorList parse(String fileName){
		XMLSerial xmlSerial=new XMLSerial();
		SensorList sensorList = new SensorList();
		try{
		    Node mainNode = xmlSerial.load(fileName);
		    if (mainNode==null)
		    	System.out.println("SensorListParser::parse Unable to parse file "+fileName);
		    else{
		    	Node listNode = xmlSerial.getChild(mainNode,"SensorList");
		    	if (listNode!=null){
			    	String placeID=xmlSerial.getAttribute(listNode,"placeID");
			    	NodeList sensorsNode = listNode.getChildNodes();
			    	for (int i = 0; i < sensorsNode.getLength(); i++){
			    	    Node sensorNode = sensorsNode.item(i);
			    	    if (sensorNode.getNodeName().equals("Sensor"))
			    	    {
			    	    	SensorData sensorData = new SensorData();
			    	    	if (placeID!=null)
			    	    		sensorData.setPlaceID(placeID);
			    	    
				    		String sensorAlias = xmlSerial.getAttribute(sensorNode,"sensorAlias");
							try{
						    	if (sensorAlias!=null)
						    		sensorData.setSensorAlias(Integer.parseInt(sensorAlias));
							}catch (NumberFormatException e){
								System.out.println("SensorListParser::parse -sensorAlias- Cant parse '"+sensorAlias+"'");
							}
							
				    		String hardwareID = xmlSerial.getAttribute(sensorNode,"hardwareID");
			    	    	if (hardwareID!=null)
			    	    		sensorData.setHardwareID(hardwareID);
			    	    	
				    		String sensorPlace = xmlSerial.getAttribute(sensorNode,"sensorPlace");
			    	    	if (sensorPlace!=null)
			    	    		sensorData.setSensorPlace(sensorPlace);
			    	    	
				    		String sensorType = xmlSerial.getAttribute(sensorNode,"sensorType");
			    	    	if (sensorType!=null)
			    	    		sensorData.setSensorType(sensorType);
			    	    	
				    		String debounceTime = xmlSerial.getAttribute(sensorNode,"debounceTime");
							try{
						    	if (debounceTime!=null)
						    		sensorData.setDebouncetime(Integer.parseInt(debounceTime));
							}catch (NumberFormatException e){
								System.out.println("SensorListParser::parse -sensorAlias- Cant parse '"+sensorAlias+"'");
							}
			    	    	
					    	Node invertedNode = xmlSerial.getChild(sensorNode,"ENABLE_INVERTED");
						    if (invertedNode!=null)
						    	sensorData.setInverted(true);
						    else
						    	sensorData.setInverted(false);
						    
					    	Node intervalNode = xmlSerial.getChild(sensorNode,"ENABLE_INTERVALSENSOR");
						    if (intervalNode!=null)
						    	sensorData.setInterval(true);
						    else
						    	sensorData.setInterval(false);
						    
						    sensorList.add(sensorData);

			    	    }
			    		
			    	}
			    		
		    	}
		    }
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return sensorList;
	}
}
