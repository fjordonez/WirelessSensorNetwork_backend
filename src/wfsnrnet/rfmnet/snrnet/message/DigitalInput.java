package wfsnrnet.rfmnet.snrnet.message;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

import java.util.Vector;

public class DigitalInput extends ConfigurationData
{
    public static final int INPUT_EVENT_MESSAGING=32;
    public static final int RESET_COUNTER=16;
    public static final int ENABLE_COUNT=8;
    public static final int HIGH_TO_LOW_ONLY=4;
    public static final int LOW_TO_HIGH_ONLY=2;
    public static final int DIGITAL_INPUT_STATE=1;

    public DigitalInput(Vector<Integer> v)
    {	
	set(v);
    }
    
    public DigitalInput(int flags,int countThreshold,int debounce)
    {
	init(flags,countThreshold,debounce);
    }

    public DigitalInput(Node node)
    {
	int flags=0;
	int countThreshold=0;
	int debounce=0;
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    if (xmlSerial.getChild(node,"INPUT_EVENT_MESSAGING")!=null)
		flags=flags|INPUT_EVENT_MESSAGING;
	    if (xmlSerial.getChild(node,"RESET_COUNTER")!=null)
		flags=flags|RESET_COUNTER;
	    if (xmlSerial.getChild(node,"ENABLE_COUNT")!=null)
		flags=flags|ENABLE_COUNT;
	    if (xmlSerial.getChild(node,"HIGH_TO_LOW_ONLY")!=null)
		flags=flags|HIGH_TO_LOW_ONLY;
	    if (xmlSerial.getChild(node,"LOW_TO_HIGH_ONLY")!=null)
		flags=flags|LOW_TO_HIGH_ONLY;
	    if (xmlSerial.getChild(node,"DIGITAL_INPUT_STATE")!=null)
		flags=flags|DIGITAL_INPUT_STATE;
	    try
	    {
		countThreshold=Integer.parseInt(xmlSerial.getAttribute(node,"CountThreshold"));
		debounce=Integer.parseInt(xmlSerial.getAttribute(node,"Debounce"));
	    }
	    catch (NumberFormatException e)
	    {
	    }
	}
	init(flags,countThreshold,debounce);
    }

    public Node toNode(XMLSerial xmlSerial)
    {	
	Element element=xmlSerial.document().createElement("DigitalInput");
	element.setAttribute("CountThreshold",""+getCountThreshold());
	element.setAttribute("Debounce",""+getDebounce());
	if ((data.elementAt(0)&INPUT_EVENT_MESSAGING)>0)
	    element.appendChild(xmlSerial.document().createElement("INPUT_EVENT_MESSAGING"));
	if ((data.elementAt(0)&RESET_COUNTER)>0)
	    element.appendChild(xmlSerial.document().createElement("RESET_COUNTER"));
	if ((data.elementAt(0)&ENABLE_COUNT)>0)
	    element.appendChild(xmlSerial.document().createElement("ENABLE_COUNT"));
	if ((data.elementAt(0)&HIGH_TO_LOW_ONLY)>0)
	    element.appendChild(xmlSerial.document().createElement("HIGH_TO_LOW_ONLY"));
	if ((data.elementAt(0)&LOW_TO_HIGH_ONLY)>0)
	    element.appendChild(xmlSerial.document().createElement("LOW_TO_HIGH_ONLY"));
	if ((data.elementAt(0)&DIGITAL_INPUT_STATE)>0)
	    element.appendChild(xmlSerial.document().createElement("DIGITAL_INPUT_STATE"));	
	return element;
    }

    private void init(int flags,int countThreshold,int debounce)
    {
	int f=Integer.parseInt("00000000",2);
	f|=flags;
	data.add(f);
	data.add(countThreshold>>16);
	data.add(countThreshold>>8);
	data.add(countThreshold);
	if (debounce<0x08)
	    debounce=0x08;
	data.add(debounce);
    }
    
    public int getCountThreshold()
    {
	int ret=0;
	if (data.size()>3)
	    ret=((data.elementAt(1)%256)<<16)+((data.elementAt(2)%256)<<8)+data.elementAt(3)%256;
	return ret;
    }

    public int getDebounce()
    {
	int ret=0;
	if (data.size()>4)
	    ret=data.elementAt(4);
	return ret;
    }

}
