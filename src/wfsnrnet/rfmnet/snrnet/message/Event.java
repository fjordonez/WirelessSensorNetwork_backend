package wfsnrnet.rfmnet.snrnet.message;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

public class Event extends ConfigurationData
{
    public static final int WAKE_UP_EVENT=1<<7;
    public static final int UART_EVENT=1<<6;
    public static final int ADC_EVENT=1<<5;
    public static final int DIGITAL_INPUT_EVENT=1<<4;

    public Event(int flags)
    {
	init(flags);
    }
    
    public Event(Node node)
    {
	int flags=0;
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    if (xmlSerial.getChild(node,"WAKE_UP_EVENT")!=null)
		flags=flags|WAKE_UP_EVENT;
	    if (xmlSerial.getChild(node,"UART_EVENT")!=null)
		flags=flags|UART_EVENT;
	    if (xmlSerial.getChild(node,"ADC_EVENT")!=null)
		flags=flags|ADC_EVENT;
	    if (xmlSerial.getChild(node,"DIGITAL_INPUT_EVENT")!=null)
		flags=flags|DIGITAL_INPUT_EVENT;
	}
	init(flags);
    }

    public Node toNode(XMLSerial xmlSerial)
    {	
	Element element=xmlSerial.document().createElement("Event");
	if ((data.elementAt(0)&WAKE_UP_EVENT)>0)
	    element.appendChild(xmlSerial.document().createElement("WAKE_UP_EVENT"));
	if ((data.elementAt(0)&UART_EVENT)>0)
	    element.appendChild(xmlSerial.document().createElement("UART_EVENT"));
	if ((data.elementAt(0)&ADC_EVENT)>0)
	    element.appendChild(xmlSerial.document().createElement("ADC_EVENT"));
	if ((data.elementAt(0)&DIGITAL_INPUT_EVENT)>0)
	    element.appendChild(xmlSerial.document().createElement("DIGITAL_INPUT_EVENT"));
	return element;
    }

    public void init(int flags)
    {
	data.add(flags);
    }
}
