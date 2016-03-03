package wfsnrnet.rfmnet.snrnet.message;

import java.util.Vector;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

public class PowerManagement extends ConfigurationData
{
    // flags1
    public static final int ENABLE_POWER_MANAGEMENT=1<<7;
    public static final int ENABLE_QUICK_SLEEP=1<<6;
    public static final int ENABLE_LISTEN_BEFORE_SLEEP=1<<5;
    
    // flags2
    public static final int ROUTER_NOT_FIELDNODE=1<<7;
    public static final int REPORT_ON_WAKE_UP=1<<6;
    public static final int CHECKING_DURING_SLEEP=1<<5;

    public PowerManagement(Vector<Integer> v)
    {	
	set(v);
    }

    public PowerManagement(int flags1,int awakeCount,int flags2,int sleepCount)
    {
	init(flags1,awakeCount,flags2,sleepCount);
    }
    
    public PowerManagement(Node node)
    {
	int flags1=0,flags2=0;
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();	    
	    if (xmlSerial.getChild(node,"ENABLE_POWER_MANAGEMENT")!=null)
		flags1=flags1|ENABLE_POWER_MANAGEMENT;
	    if (xmlSerial.getChild(node,"ENABLE_QUICK_SLEEP")!=null)
		flags1=flags1|ENABLE_QUICK_SLEEP;
	    if (xmlSerial.getChild(node,"ENABLE_LISTEN_BEFORE_SLEEP")!=null)
		flags1=flags1|ENABLE_LISTEN_BEFORE_SLEEP;   
	    
	    if (xmlSerial.getChild(node,"ROUTER_NOT_FIELDNODE")!=null)
		flags2=flags2|ROUTER_NOT_FIELDNODE;
	    if (xmlSerial.getChild(node,"REPORT_ON_WAKE_UP")!=null)
		flags2=flags2|REPORT_ON_WAKE_UP;
	    if (xmlSerial.getChild(node,"CHECKING_DURING_SLEEP")!=null)
		flags2=flags2|CHECKING_DURING_SLEEP;
	    int sleepCount=0,awakeCount=0;
	    try
	    {
		sleepCount=Integer.parseInt(xmlSerial.getAttribute(node,"SleepCount"));
		awakeCount=Integer.parseInt(xmlSerial.getAttribute(node,"AwakeCount"));
	    }
	    catch (NumberFormatException e)
	    {
	    }
	    init(flags1,awakeCount,flags2,sleepCount);
	}	
    }
    
    private void init(int flags1,int awakeCount,int flags2,int sleepCount)
    {
	int f=awakeCount%(1<<5);
	f|=flags1;
	data.add(f);
	f=(sleepCount>>8)%(1<<5);
	f|=flags2;
	data.add(f);
	data.add(sleepCount%256);
    }

    public Node toNode(XMLSerial xmlSerial)
    {	
	Element element=xmlSerial.document().createElement("PowerManagement");
	element.setAttribute("AwakeCount",""+getAwakeCount());
	element.setAttribute("SleepCount",""+getSleepCount());
	if ((data.elementAt(0)&ENABLE_POWER_MANAGEMENT)>0)
	    element.appendChild(xmlSerial.document().createElement("ENABLE_POWER_MANAGEMENT"));
	if ((data.elementAt(0)&ENABLE_QUICK_SLEEP)>0)
	    element.appendChild(xmlSerial.document().createElement("ENABLE_QUICK_SLEEP"));
	if ((data.elementAt(0)&ENABLE_LISTEN_BEFORE_SLEEP)>0)
	    element.appendChild(xmlSerial.document().createElement("ENABLE_LISTEN_BEFORE_SLEEP"));

	if ((data.elementAt(1)&ROUTER_NOT_FIELDNODE)>0)
	    element.appendChild(xmlSerial.document().createElement("ROUTER_NOT_FIELDNODE"));
	if ((data.elementAt(1)&REPORT_ON_WAKE_UP)>0)
	    element.appendChild(xmlSerial.document().createElement("REPORT_ON_WAKE_UP"));
	if ((data.elementAt(1)&CHECKING_DURING_SLEEP)>0)
	    element.appendChild(xmlSerial.document().createElement("CHECKING_DURING_SLEEP"));
	return element;
    }
    
    public int getAwakeCount()
    {
	return data.elementAt(0)&((1<<5)-1);
    }

    public int getSleepCount()
    {
	return ((data.elementAt(1)&((1<<5)-1))<<8)+data.elementAt(2);
    }

}
