package wfsnrnet.rfmnet.snrnet;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import wfsnrnet.rfmnet.snrnet.message.*;
import wfsnrnet.xml.XMLSerial;

/*! Implements a simulated FieldNode represented in XML format
 */
public class FieldNodeSimulated
{
    private static Logger logger=Logger.getLogger(FieldNodeSimulated.class);

    public String hardwareID;
    public int AID=-1;
    public IOConfiguration ioConfiguration;
    public PowerManagement dynamicPowerManagement;
    public PowerManagement staticPowerManagement;
    public DigitalInput digitalInput;
    public DigitalOutput digitalOutput;
    public ADCInput adcInput;
    public UART uart;
    public EventSimulated eventSimulated;

    /*! Constructor for test purposes
     */
    public FieldNodeSimulated()
    {
	hardwareID="BasMan";	
	ioConfiguration=new IOConfiguration(IOConfiguration.DIGITAL_OUTPUT|
					    IOConfiguration.DIGITAL_INPUT);
	dynamicPowerManagement=new PowerManagement(PowerManagement.ENABLE_POWER_MANAGEMENT|
						   PowerManagement.ENABLE_LISTEN_BEFORE_SLEEP,
						   20,
						   PowerManagement.ROUTER_NOT_FIELDNODE|
						   PowerManagement.REPORT_ON_WAKE_UP|
						   PowerManagement.CHECKING_DURING_SLEEP,
						   20);
	staticPowerManagement=new PowerManagement(PowerManagement.ENABLE_POWER_MANAGEMENT|
						  PowerManagement.ENABLE_LISTEN_BEFORE_SLEEP,
						  20,
						  PowerManagement.ROUTER_NOT_FIELDNODE|
						  PowerManagement.REPORT_ON_WAKE_UP|
						  PowerManagement.CHECKING_DURING_SLEEP,
						  20);
	digitalOutput=new DigitalOutput(DigitalOutput.TIME_MS2_2_NOT_MS134,
					31);
	digitalInput=new DigitalInput(DigitalInput.INPUT_EVENT_MESSAGING|
				      DigitalInput.HIGH_TO_LOW_ONLY,
				      10,0);
	adcInput=new ADCInput(ADCInput.THRESHOLD_EVENT_MESSAGING|
			      ADCInput.HIGH_THRESHOLD,
			      2,8);
	uart=new UART(UART.PARITY_BIT|
		      UART.EVENT_MESSAGING);
    }
    
    /*! Constructor
     * @param node XML data to construct the object
     */
    public FieldNodeSimulated(Node node)
    {		
	if (node!=null)
	{   	    
	    XMLSerial xmlSerial=new XMLSerial();
	    //logger.info(xmlSerial.toString(node));
	    hardwareID=xmlSerial.getAttribute(node,"HardwareID");
	    ioConfiguration=new IOConfiguration(xmlSerial.getChild(node,"IOConfiguration"));
	    Node n;
	    n=xmlSerial.getChild(node,"DynamicPowerManagement");
	    if (n!=null)
		dynamicPowerManagement=new PowerManagement(xmlSerial.getChild(n,"PowerManagement"));
	    n=xmlSerial.getChild(node,"StaticPowerManagement");
	    if (n!=null)
		staticPowerManagement=new PowerManagement(xmlSerial.getChild(n,"PowerManagement"));   
	    digitalOutput=new DigitalOutput(xmlSerial.getChild(node,"DigitalOutput"));
	    digitalInput=new DigitalInput(xmlSerial.getChild(node,"DigitalInput"));	    
	    adcInput=new ADCInput(xmlSerial.getChild(node,"ADCInput"));
	    uart=new UART(xmlSerial.getChild(node,"UART"));
	}
	else
	    logger.error("no XML data");
    }

    /*! Converts an object to XML data
     * @param xmlSerial object for XML manipulation
     */
    public Node toNode(XMLSerial xmlSerial)
    {		
	Element element=xmlSerial.document().createElement("FieldNodeSimulated");	
	element.setAttribute("HardwareID",hardwareID);
	if (AID>=0)
	    element.setAttribute("AID",""+AID);
	if (ioConfiguration!=null)
	    element.appendChild(ioConfiguration.toNode(xmlSerial));	
	Element e;
	if (dynamicPowerManagement!=null)
	{
	    e=xmlSerial.document().createElement("DynamicPowerManagement");
	    e.appendChild(dynamicPowerManagement.toNode(xmlSerial));
	    element.appendChild(e);
	}
	if (staticPowerManagement!=null)
	{
	    e=xmlSerial.document().createElement("StaticPowerManagement");
	    e.appendChild(staticPowerManagement.toNode(xmlSerial));
	    element.appendChild(e);
	}
	if (digitalInput!=null)
	    element.appendChild(digitalInput.toNode(xmlSerial));
	if (digitalOutput!=null)
	    element.appendChild(digitalOutput.toNode(xmlSerial));
	if (adcInput!=null)
	    element.appendChild(adcInput.toNode(xmlSerial));
	if (uart!=null)
	    element.appendChild(uart.toNode(xmlSerial));	
	return element;
    }

    public String toString()
    {
	XMLSerial xmlSerial=new XMLSerial();
	return xmlSerial.toString(toNode(xmlSerial));
    }

    public void setEventSimulated(EventSimulated eventSimulated)
    {
	this.eventSimulated=eventSimulated;
    }
}
