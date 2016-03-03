package wfsnrnet.rfmnet.snrnet.message;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

public class ADCInput extends ConfigurationData
{
    private static Logger logger = Logger.getLogger(ADCInput.class);
    public static final int THRESHOLD_EVENT_MESSAGING=32;
    public static final int READ_RSSI=16;
    public static final int DIGITAL_INPUT_STATE=8;
    public static final int HIGH_THRESHOLD=4;
    public static final int LOW_THRESHOLD=2;
    public static final int ENABLE_10BIT_RESOLUTION=1;

    public ADCInput(Vector<Integer> v)
    {
	set(v);
    }

    public ADCInput(int flags,int lowThreshold,int highThreshold)
    {
	init(flags,lowThreshold,highThreshold);
    }
    
    public ADCInput(Node node)
    {	
	int flags=0;
	int lowThreshold=0;
	int highThreshold=0;
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    if (xmlSerial.getChild(node,"THRESHOLD_EVENT_MESSAGING")!=null)
		flags=flags|THRESHOLD_EVENT_MESSAGING;
	    if (xmlSerial.getChild(node,"READ_RSSI")!=null)
		flags=flags|READ_RSSI;
	    if (xmlSerial.getChild(node,"DIGITAL_INPUT_STATE")!=null)
		flags=flags|DIGITAL_INPUT_STATE;
	    if (xmlSerial.getChild(node,"HIGH_THRESHOLD")!=null)
		flags=flags|HIGH_THRESHOLD;
	    if (xmlSerial.getChild(node,"LOW_THRESHOLD")!=null)
		flags=flags|LOW_THRESHOLD;
	    if (xmlSerial.getChild(node,"ENABLE_10BIT_RESOLUTION")!=null)
		flags=flags|ENABLE_10BIT_RESOLUTION;
	    try
	    {
		highThreshold=Integer.parseInt(xmlSerial.getAttribute(node,"HighThreshold"));
		lowThreshold=Integer.parseInt(xmlSerial.getAttribute(node,"LowThreshold"));		
	    }
	    catch (NumberFormatException e)
	    {
	    }
	}
	init(flags,lowThreshold,highThreshold);
    }

    public Node toNode(XMLSerial xmlSerial)
    {		
	Element element=xmlSerial.document().createElement("ADCInput");
	element.setAttribute("HighThreshold",""+getHighThreshold());
	element.setAttribute("LowThreshold",""+getLowThreshold());	
	if ((data.elementAt(0)&THRESHOLD_EVENT_MESSAGING)>0)
	    element.appendChild(xmlSerial.document().createElement("THRESHOLD_EVENT_MESSAGING"));
	if ((data.elementAt(0)&READ_RSSI)>0)
	    element.appendChild(xmlSerial.document().createElement("READ_RSSI"));
	if ((data.elementAt(0)&DIGITAL_INPUT_STATE)>0)
	    element.appendChild(xmlSerial.document().createElement("DIGITAL_INPUT_STATE"));
	if ((data.elementAt(0)&HIGH_THRESHOLD)>0)
	    element.appendChild(xmlSerial.document().createElement("HIGH_THRESHOLD"));
	if ((data.elementAt(0)&LOW_THRESHOLD)>0)
	    element.appendChild(xmlSerial.document().createElement("LOW_THRESHOLD"));
	if ((data.elementAt(0)&ENABLE_10BIT_RESOLUTION)>0)
	    element.appendChild(xmlSerial.document().createElement("ENABLE_10BIT_RESOLUTION"));
	return element;
    }

    private void init(int flags,int lowThreshold,int highThreshold)
    {
	if ((flags&ENABLE_10BIT_RESOLUTION)>0)
	{
	    logger.error("Current the ENABLE_10BIT_RESOLUTION option is not supported as an ADC event "+
			 "does not indicate if the node is in 8 or 10 bit configuration");
	    flags=flags^ENABLE_10BIT_RESOLUTION;
	}
	int f=Integer.parseInt("01000000",2);
	f|=flags;
	data.add(f);
	data.add(highThreshold);
	data.add(lowThreshold);	
    }

    public int getHighThreshold()
    {
	int ret=0;
	if (data.size()>1)
	    ret=data.elementAt(1)%256;
	return ret;
    }

    public int getLowThreshold()
    {
	int ret=0;
	if (data.size()>2)
	    ret=data.elementAt(2)%256;
	return ret;
    }    
}
