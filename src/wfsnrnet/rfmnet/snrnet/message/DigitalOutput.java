package wfsnrnet.rfmnet.snrnet.message;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

import java.util.Vector;

public class DigitalOutput extends ConfigurationData
{
    //
    public static final int DIGITAL_INPUT_STATE=1<<4;
    public static final int POWER_UP_STATE=1<<3;
    public static final int TIME_MS2_2_NOT_MS134=1<<2;
    //
    public static final int DIGITAL_OUTPUT_STATE=1;

    public DigitalOutput(Vector<Integer> v)
    {	
	set(v);
    }

    public DigitalOutput(int flags,int timeMultiplier)
    {
	init(flags,timeMultiplier);
    }

    public DigitalOutput(Node node)
    {
	int flags=0;
	int timeMultiplier=0;
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    if (xmlSerial.getChild(node,"DIGITAL_INPUT_STATE")!=null)
		flags=flags|DIGITAL_INPUT_STATE;
	    if (xmlSerial.getChild(node,"POWER_UP_STATE")!=null)
		flags=flags|POWER_UP_STATE;
	    if (xmlSerial.getChild(node,"TIME_MS2_2_NOT_MS134")!=null)
		flags=flags|TIME_MS2_2_NOT_MS134;
	    if (xmlSerial.getChild(node,"DIGITAL_OUTPUT_STATE")!=null)
		flags=flags|DIGITAL_OUTPUT_STATE;
	    try
	    {
		timeMultiplier=Integer.parseInt(xmlSerial.getAttribute(node,"TimeMultiplier"));		
	    }
	    catch (NumberFormatException e)
	    {
	    }
	}
	init(flags,timeMultiplier);
    }

    public Node toNode(XMLSerial xmlSerial)
    {	
	Element element=xmlSerial.document().createElement("DigitalOutput");
	element.setAttribute("TimeMultiplier",""+getTimeMultiplier());
	if ((data.elementAt(0)&DIGITAL_INPUT_STATE)>0)
	    element.appendChild(xmlSerial.document().createElement("DIGITAL_INPUT_STATE"));
	if ((data.elementAt(0)&POWER_UP_STATE)>0)
	    element.appendChild(xmlSerial.document().createElement("POWER_UP_STATE"));
	if ((data.elementAt(0)&TIME_MS2_2_NOT_MS134)>0)
	    element.appendChild(xmlSerial.document().createElement("TIME_MS2_2_NOT_MS134"));	
	if ((data.elementAt(0)&DIGITAL_OUTPUT_STATE)>0)
	    element.appendChild(xmlSerial.document().createElement("DIGITAL_OUTPUT_STATE"));	
	return element;
    }

    private void init(int flags,int timeMultiplier)
    {
	int f=Integer.parseInt("10000000",2);
	f|=flags;
	data.add(f);
	data.add(timeMultiplier);
    }
    
    public int getTimeMultiplier()
    {
	int ret=0;
	if (data.size()>1)
	    ret=data.elementAt(1)%256;
	return ret;
    }
      
}
