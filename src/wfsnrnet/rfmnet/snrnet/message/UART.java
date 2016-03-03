package wfsnrnet.rfmnet.snrnet.message;

import java.util.Vector;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

public class UART extends ConfigurationData
{
    public static final int EVENT_MESSAGING=32;
    public static final int BIT7_DATA=16;
    //
    public static final int PARITY_BIT=4;
    public static final int KBS2_4_NOT_KBS9_6=2;
    public static final int HALF_KBS=1;

    public UART(Vector<Integer> v)
    {	
	set(v);
    }

    public UART(int flags)
    {
	init(flags);
    }

    public UART(Node node)
    {
	int flags=0;	
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    if (xmlSerial.getChild(node,"EVENT_MESSAGING")!=null)
		flags=flags|EVENT_MESSAGING;
	    if (xmlSerial.getChild(node,"BIT7_DATA")!=null)
		flags=flags|BIT7_DATA;
	    if (xmlSerial.getChild(node,"PARITY_BIT")!=null)
		flags=flags|PARITY_BIT;
	    if (xmlSerial.getChild(node,"KBS2_4_NOT_KBS9_6")!=null)
		flags=flags|KBS2_4_NOT_KBS9_6;
	    if (xmlSerial.getChild(node,"HALF_KBS")!=null)
		flags=flags|HALF_KBS;	    
	}
	init(flags);
    }

    public Node toNode(XMLSerial xmlSerial)
    {	
	Element element=xmlSerial.document().createElement("UART");	
	if ((data.elementAt(0)&EVENT_MESSAGING)>0)
	    element.appendChild(xmlSerial.document().createElement("EVENT_MESSAGING"));
	if ((data.elementAt(0)&BIT7_DATA)>0)
	    element.appendChild(xmlSerial.document().createElement("BIT7_DATA"));
	if ((data.elementAt(0)&PARITY_BIT)>0)
	    element.appendChild(xmlSerial.document().createElement("PARITY_BIT"));
	if ((data.elementAt(0)&KBS2_4_NOT_KBS9_6)>0)
	    element.appendChild(xmlSerial.document().createElement("KBS2_4_NOT_KBS9_6"));
	if ((data.elementAt(0)&HALF_KBS)>0)
	    element.appendChild(xmlSerial.document().createElement("HALF_KBS"));	
	return element;
    }

    private void init(int flags)
    {
	int f=Integer.parseInt("11000000",2);
	f|=flags;
	data.add(f);
	data.add(0);
    }    
}
