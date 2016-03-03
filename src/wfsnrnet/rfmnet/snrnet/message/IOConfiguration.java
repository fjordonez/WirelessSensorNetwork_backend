package wfsnrnet.rfmnet.snrnet.message;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

public class IOConfiguration extends ConfigurationData
{
    public static final int CONTINUOUS_LED=1<<5;
    public static final int UART          =1<<3;
    public static final int DIGITAL_OUTPUT=1<<2;
    public static final int ANALOG_INPUT  =1<<1;
    public static final int DIGITAL_INPUT =1;

    public IOConfiguration(int flags)
    {
	data.add(flags);
    }

    public IOConfiguration(Node node)
    {
	int flags=0;
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    if (xmlSerial.getChild(node,"CONTINUOUS_LED")!=null)
		flags=flags|CONTINUOUS_LED;
	    if (xmlSerial.getChild(node,"UART")!=null)
		flags=flags|UART;
	    if (xmlSerial.getChild(node,"DIGITAL_OUTPUT")!=null)
		flags=flags|DIGITAL_OUTPUT;
	    if (xmlSerial.getChild(node,"ANALOG_INPUT")!=null)
		flags=flags|ANALOG_INPUT;
	    if (xmlSerial.getChild(node,"DIGITAL_INPUT")!=null)
		flags=flags|DIGITAL_INPUT;
	}
	data.add(flags);
    }

    public Node toNode(XMLSerial xmlSerial)
    {	
	Element element=xmlSerial.document().createElement("IOConfiguration");
	if ((data.elementAt(0)&CONTINUOUS_LED)>0)
	    element.appendChild(xmlSerial.document().createElement("CONTINUOUS_LED"));
	if ((data.elementAt(0)&UART)>0)
	    element.appendChild(xmlSerial.document().createElement("UART"));
	if ((data.elementAt(0)&DIGITAL_OUTPUT)>0)
	    element.appendChild(xmlSerial.document().createElement("DIGITAL_OUTPUT"));
	if ((data.elementAt(0)&ANALOG_INPUT)>0)
	    element.appendChild(xmlSerial.document().createElement("ANALOG_INPUT"));
	if ((data.elementAt(0)&DIGITAL_INPUT)>0)
	    element.appendChild(xmlSerial.document().createElement("DIGITAL_INPUT"));
	if (isBaseNode())
	    element.appendChild(xmlSerial.document().createElement("BASE_NODE"));
	else if (isRouter())
	    element.appendChild(xmlSerial.document().createElement("ROUTER"));
	else if (isFieldNode())
	    element.appendChild(xmlSerial.document().createElement("FIELD_NODE"));
	return element;
    }

    public boolean isBaseNode()
    {
	return ((data.elementAt(0)&(3<<6))==0);
    }

    public boolean isRouter()
    {
	return ((data.elementAt(0)&(3<<6))==1);
    }

    public boolean isFieldNode()
    {
	return ((data.elementAt(0)&(3<<6))==2);
    }

}
