package wfsnrnet.rfmnet.snrnet;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Date;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import wfsnrnet.rfmnet.snrnet.message.*;
import wfsnrnet.xml.XMLSerial;

/*! Overloads the BaseNodeBase interface to implement a simulated
 *  BaseNode represented in XML format. It has FieldNodeSimulated
 *  objects that generate EventSimulated objects as specified in the
 *  XML file.  BaseNodeSimulated is designed for test purposes without
 *  requiring actual nodes. Currently it does not do anything with
 *  node configuration data, it only generates events, allows event
 *  data to be read, and the hardwareID to be read.
 */
public class BaseNodeSimulated extends BaseNodeBase 
{
    private static Logger logger=Logger.getLogger(BaseNodeSimulated.class);
    
    private Map<String,FieldNodeSimulated> fieldNodes=new HashMap<String,FieldNodeSimulated>();
    private Map<Integer,FieldNodeSimulated> aidFieldNodes=new HashMap<Integer,FieldNodeSimulated>();

    private List<EventSimulated> events=new LinkedList<EventSimulated>();
    private int AID=0;

    /*! Constructor
     * @param node XML data to construct the object
     * @param callBack object that receives all messages comming back from the BaseNode
     */
    public BaseNodeSimulated(Node node,BaseNodeCallBack callBack) throws RFMException
    {
	super(callBack);
	if (node!=null)
	{   	    
	    XMLSerial xmlSerial=new XMLSerial();
	    NodeList children = node.getChildNodes();
	    for (int i = 0; i < children.getLength(); i++)
	    {
		Node child=children.item(i);
		if (child.getNodeName().equals("FieldNodeSimulated"))
		{
		    logger.info("reading 'FieldNodeSimulated'");
		    FieldNodeSimulated fieldNodeSimulated=new FieldNodeSimulated(child);
		    fieldNodes.put(fieldNodeSimulated.hardwareID,fieldNodeSimulated);		    
		}
		else if (child.getNodeName().equals("EventSimulated"))
		{
		    logger.info("reading 'EventSimulated'");
		    EventSimulated eventSimulated=new EventSimulated(child);
		    events.add(eventSimulated);
		}
		else if (child.getNodeName().equals("#text"))
		{
		    
		}
		else
		    logger.error("unknown node '"+child.getNodeName()+"': "+xmlSerial.toString(child));
	    }
	    Collections.sort(events);
	}
	else
	    logger.error("no XML data");
    }

    /*! Converts an object to XML data
     * @param xmlSerial object for XML manipulation
     */
    public Node toNode(XMLSerial xmlSerial)
    {		
	Element element=xmlSerial.document().createElement("BaseNodeSimulated");	
	
	for (Iterator<String> it = fieldNodes.keySet().iterator(); it.hasNext(); )
	{
	    String hardwareID=it.next();
	    if (hardwareID!=null)
	    {
		element.appendChild(fieldNodes.get(hardwareID).toNode(xmlSerial));
	    }
	}
	for (Iterator<EventSimulated> it = events.iterator();it.hasNext(); )
	{
	    EventSimulated event=it.next();
	    if (event!=null)
	    {
		element.appendChild(event.toNode(xmlSerial));
	    }
	}
	return element;
    }

    public String toString()
    {
	XMLSerial xmlSerial=new XMLSerial();
	return xmlSerial.toString(toNode(new XMLSerial()));
    }   

    public void readHardwareID(int AID)
    {
	super.readHardwareID(AID);
	FieldNodeSimulated fieldNodeSimulated=aidFieldNodes.get(AID);
	if (fieldNodeSimulated!=null)
	    callBack.hardwareID(AID,fieldNodeSimulated.hardwareID);
    }

    public void readIOConfiguration(int AID)    
    {
	super.readIOConfiguration(AID);
	FieldNodeSimulated fieldNodeSimulated=aidFieldNodes.get(AID);
	if (fieldNodeSimulated!=null)
	    callBack.ioConfiguration(AID,fieldNodeSimulated.ioConfiguration);
    }

    public void readDigitalInput(int AID)    
    {
	super.readDigitalInput(AID);
	FieldNodeSimulated fieldNodeSimulated=aidFieldNodes.get(AID);
	if (fieldNodeSimulated!=null)
	    callBack.digitalInput(AID,
				  fieldNodeSimulated.eventSimulated.current,
				  fieldNodeSimulated.eventSimulated.count);
    }

    public void readADCInput(int AID)    
    {
	super.readADCInput(AID);
	FieldNodeSimulated fieldNodeSimulated=aidFieldNodes.get(AID);
	if (fieldNodeSimulated!=null)
	    callBack.adcInput(AID,
			      fieldNodeSimulated.eventSimulated.current,
			      fieldNodeSimulated.eventSimulated.highThreshold,
			      fieldNodeSimulated.eventSimulated.lowThreshold);
    }

    public void readUART(int AID)    
    {
	super.readUART(AID);
	FieldNodeSimulated fieldNodeSimulated=aidFieldNodes.get(AID);
	if (fieldNodeSimulated!=null)
	{
	    callBack.uartInput(AID,
			       RFMHelper.StringToVector(fieldNodeSimulated.eventSimulated.uart));
	}
    }
    
    public FieldNodeSimulated getFieldNode(EventSimulated event)
    {
	FieldNodeSimulated fieldNodeSimulated=fieldNodes.get(event.hardwareID);
	if (fieldNodeSimulated!=null && fieldNodeSimulated.AID<0)
	{
	    AID++;
	    fieldNodeSimulated.AID=AID;
	    aidFieldNodes.put(AID,fieldNodeSimulated);
	}
	return fieldNodeSimulated;
    }

    public void run()
    {	
	logger.info("producing events");
	Iterator<EventSimulated> it=events.iterator();
	if (it.hasNext())
	{	    
	    EventSimulated event=it.next();
	    Date date=new Date();	
	    long start=date.getTime();
	    while (running)
	    {
		try
		{
		    Thread.sleep(100);
		} catch(InterruptedException ie) {}
		date=new Date();
		long current=date.getTime()-start;
	    
		if (event!=null)
		{
		    if (current>event.timestamp)
		    {
			logger.info("event: "+event);
			FieldNodeSimulated fieldNodeSimulated=getFieldNode(event);
		    
			if (fieldNodeSimulated!=null)
			{
			    fieldNodeSimulated.setEventSimulated(event);
			    if (event.type.equals("DigitalInput"))
			    {
				callBack.event(fieldNodeSimulated.AID,new Event(Event.DIGITAL_INPUT_EVENT));
			    }
			    else if (event.type.equals("ADC"))
			    {
				callBack.event(fieldNodeSimulated.AID,new Event(Event.ADC_EVENT));
			    }
			    else if (event.type.equals("UART"))
			    {
				callBack.event(fieldNodeSimulated.AID,new Event(Event.UART_EVENT));
			    }
			    else if (event.type.equals("WakeUp"))
			    {
				callBack.event(fieldNodeSimulated.AID,new Event(Event.WAKE_UP_EVENT));
			    }
			}
			else
			    logger.error("no FieldNode found for event: "+event);

			if (it.hasNext())
			    event=it.next();
			else
			    running=false;
		    }
		}
	    }
	}
    }

}
