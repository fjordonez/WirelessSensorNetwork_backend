package wfsnrnet.rfmnet.snrnet;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import wfsnrnet.rfmnet.snrnet.message.*;
import wfsnrnet.xml.XMLSerial;


/*! Implements a simulated Event represented in XML format
 */
public class EventSimulated implements Comparable
{
    private static Logger logger = Logger.getLogger(EventSimulated.class);
    public String hardwareID;
    public String type;
    public Long timestamp;
    public Integer current;
    public Integer highThreshold;
    public Integer lowThreshold;
    public Integer count=0;    
    public String uart=null;

    /*! Constructor
     * @param node XML data to construct the object
     */
    public EventSimulated(Node node) throws RFMException
    {
	if (node!=null)
	{   	    
	    XMLSerial xmlSerial=new XMLSerial();
	    type=xmlSerial.getAttribute(node,"Type");
	    hardwareID=xmlSerial.getAttribute(node,"HardwareID");
	    try
	    {
		timestamp=Long.parseLong(xmlSerial.getAttribute(node,"Timestamp"));		
	    }
	    catch (NumberFormatException e)
	    {
	    }
	    if (hardwareID==null)
		logger.error("Could not find 'HardwareID' in: "+xmlSerial.toString(node));
	    if (type==null)
		logger.error("Could not find 'Type' in: "+xmlSerial.toString(node));
	    if (timestamp==null)
		logger.error("Could not find 'TimeStamp' in: "+xmlSerial.toString(node));

	    if (type.equals("DigitalInput"))
	    {
		current=Integer.parseInt(xmlSerial.getAttribute(node,"Current",true));
		count=Integer.parseInt(xmlSerial.getAttribute(node,"Count",true));
	    }
	    else if (type.equals("ADC"))
	    {
		current=Integer.parseInt(xmlSerial.getAttribute(node,"Current",true));
		highThreshold=Integer.parseInt(xmlSerial.getAttribute(node,"HighThreshold",true));
		lowThreshold=Integer.parseInt(xmlSerial.getAttribute(node,"LowThreshold",true));
	    }
	    else if (type.equals("UART"))
	    {
		uart=xmlSerial.getAttribute(node,"UART",true);
	    }
	    else if (type.equals("WakeUp"))
	    {		
	    }
	    else
		logger.error("Unknown type '"+type+"' in: "+xmlSerial.toString(node));
	}
	else
	    logger.error("no XML data");
    }

    /*! Converts an object to XML data
     * @param xmlSerial object for XML manipulation
     */
    public Node toNode(XMLSerial xmlSerial)
    {		
	Element element=xmlSerial.document().createElement("EventSimulated");
	element.setAttribute("Type",type);
	element.setAttribute("HardwareID",hardwareID);
	element.setAttribute("Timestamp",""+timestamp);
	if (current!=null)
	    element.setAttribute("Current",""+current);
	if (count!=null)
	    element.setAttribute("Count",""+count);
	if (highThreshold!=null)
	    element.setAttribute("HighThreshold",""+highThreshold);
	if (lowThreshold!=null)
	    element.setAttribute("LowThreshold",""+lowThreshold);
	if (uart!=null)
	    element.setAttribute("Count",uart);	
	return element;
    }

    public String toString()
    {
	XMLSerial xmlSerial=new XMLSerial();
	return xmlSerial.toString(toNode(xmlSerial));
    }

    public int compareTo(Object o)
    {
	EventSimulated eventSimulated=(EventSimulated)o;
	return (int)(timestamp-eventSimulated.timestamp);
    }

}
