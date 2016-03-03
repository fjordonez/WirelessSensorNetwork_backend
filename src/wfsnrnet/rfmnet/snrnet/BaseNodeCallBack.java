package wfsnrnet.rfmnet.snrnet;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import wfsnrnet.rfmnet.snrnet.message.*;
import wfsnrnet.xml.XMLSerial;

import java.util.Vector;


/*! Super class of objects that receive messages from the nodes. This
 *  class only prints data the incomming messages and should be
 *  overloaded to properly deal with the incomming messages.
 */
public class BaseNodeCallBack
{
    private static Logger logger = Logger.getLogger(BaseNodeCallBack.class);    
    protected BaseNodeBase baseNodeBase;
    //private Map<Integer,PeriodicUARTResetThread> aidToResetThread = new HashMap<Integer,PeriodicUARTResetThread>();    

    // helper variables to save config data
    private String xmlFile;
    protected int saveConfigAID=-1;// node of which to save config data
    protected IOConfiguration ioConfiguration;
    protected PowerManagement dynamicPowerManagement;
    protected PowerManagement staticPowerManagement;
    protected ADCInput adcInput;
    protected DigitalInput digitalInput;
    protected DigitalOutput digitalOutput;
    protected UART uart;
    protected Integer timer;
    protected Integer routers;
    protected Integer powerOn;


    /*! Sets the BaseNodeBase object to interact with. The interaction
     *  consists of resetting and reading data after receiving event.
     */
    public void setBaseNodeBase(BaseNodeBase baseNodeBase)
    {
	this.baseNodeBase=baseNodeBase;
    }

    /*! Is called when an invalid response is received from a node.
     * @param AID alias ID of the node
     * @param send the message that was send
     * @param response the message that was received
     */
    public void invalidResponse(int AID,RFMMessage send,RFMMessage response)
    {
	logger.info("INVALID_RESPONSE AID:"+AID+"\n send: "+send+" response: "+response);
    }

    /*! Is called when an event is received from a node. As a response the
     *  baseNodeBase is called to read the data associated with the
     *  event and reset the event flag.
     * @param AID alias ID of the node
     * @param event data that was received
     */
    public void event(int AID,Event event)
    {
	logger.info("event AID:"+AID+": "+event);
	if ((event.get(0)&Event.WAKE_UP_EVENT)>0)
	{
	    baseNodeBase.readDynamicPowerConfiguration(AID);// resets event
	}
	if ((event.get(0)&Event.UART_EVENT)>0)
	{
	    //baseNodeBase.readAndResetUART(AID);	    
	    baseNodeBase.readUART(AID);
	    baseNodeBase.writeUART(AID,new Vector<Integer>());// resets event
	    //baseNodeBase.resetUART(AID);// resets event using reset vector

	    /*
	    // add PeriodicUARTResetThread if there is none for current AID
	    PeriodicUARTResetThread periodicUARTResetThread=aidToResetThread.get(AID);
	    if (periodicUARTResetThread==null)
		aidToResetThread.put(AID,new PeriodicUARTResetThread(baseNodeBase,AID,5));
	    */
	}
	if ((event.get(0)&Event.ADC_EVENT)>0)
	{	    
	    baseNodeBase.readADCInput(AID); // resets event
	    baseNodeBase.writeADCInput(AID); // reset threshold flags	
	}
	if ((event.get(0)&Event.DIGITAL_INPUT_EVENT)>0)
	{	    	    
	    baseNodeBase.readDigitalInput(AID);
	    baseNodeBase.resetDigitalInput(AID);// resets event	    
	}
	/* 
	// this tells all nodes to go to sleep in 15 seconds time instead of waiting for possible auto sleep
	PowerManagement powerManagement=new PowerManagement(PowerManagement.ENABLE_POWER_MANAGEMENT,
							    0,
							    PowerManagement.REPORT_ON_WAKE_UP|
							    PowerManagement.CHECKING_DURING_SLEEP,
							    1);
	baseNodeBase.writeDynamicPowerConfiguration(AID,powerManagement); // put node back to sleep
	*/
    }

    /*! Is called when a powerOn is received from a node.
     * @param AID alias ID of the node
     * @param powerOn data that was received
     */    
    public void powerOn(int AID,PowerOn powerOn)
    {
	logger.info("powerOn AID:"+AID+": "+powerOn);
    }

    /*! Is called when a powerOn is received from a node.
     * @param AID alias ID of the node
     * @param powerOn data that was received
     */   
    public void firmware(int AID,int firmware)
    {
	logger.info("firmware AID:"+AID+": firmware:"+firmware);
    }

    /*! Is called when mode and model data is received from a node.
     * @param AID alias ID of the node
     * @param mode current mode setting
     * @param mode current model setting
     */ 
    public void modeModel(int AID,int mode,int model)
    {
	String modeString="undefined";
	String modelString="undefined";
	if (mode==0)
	    modeString="DM1810 native";
	else
	    modeString="DM1800 compatible";
	if (model==1)
	    modelString="DM1810-434";
	else if (model==2)
	    modelString="DM1810-916";
	logger.info("modeModel AID:"+AID+": mode:'"+modeString+"' model:'"+modelString+"'");	
    }

    /*! Is called when a hardwareId is received from a node.
     * @param AID alias ID of the node
     * @param hardwareID data that was received
     */ 
    public void hardwareID(int AID,String hardwareID)
    {
	logger.info("hardwareID AID:"+AID+": hardwareID: "+hardwareID);	
    }

    /*! Is called when a node-reset-complete signal is received from the BaseNode.     
     */ 
    public void baseNodeReset()
    {
	logger.info("baseNodeReset");
    }

    // bind
    /*! Is called when a bind configuration is received from a node.
     * @param AID alias ID of the node
     */ 
    public void bindConfiguration(int AID,int networkID,int lowerAliasID,int systemID,int systemKey)
    {
	logger.info("bindConfiguration AID:"+AID+" networkID:"+networkID+" lowerAliasID:"+lowerAliasID+" systemID:"+systemID+" systemKey:"+systemKey);
    }

    /*! Is called when the bind list of the BaseNode was received.
     * @param nrRouters number of bound routers
     * @param nrNodes number of bound routers plus field nodes
     */ 
    public void baseNodeBindList(int nrRouters,int nrNodes)
    {
	logger.info("baseNodeBindList nrRouters:"+nrRouters+" nrNodes:"+nrNodes);	
    }

    /*! Is called when a link map is received from a node.
     * @param AID alias ID of the node
     * @param routerMap1 bit map from left to right the connections to the BaseNode and routers 1 through 7
     * @param routerMap2 bit map from left to right the connections to the routers 8 through 15
     */ 
    public void linkMap(int AID,int routerMap2,int routerMap1)
    {
	logger.info("linkMap AID:"+AID+": routerMap2: "+Integer.toBinaryString(routerMap2)+" routerMap1: "+Integer.toBinaryString(routerMap1));	
    }
    
    /*! Is called when a RSSI is received from a node.
     * @param AID alias ID of the node
     * @param data RSSI data of base node followed by 15 routers
     */ 
    public void rssi(int AID,Vector<Integer> data)
    {
	String s="rssi AID:"+AID+" ";
	for (int i=0;i<data.size();i++)
	    s+="["+i+"]:"+data.get(i)+" ";
	logger.info(s);
    }

    // config
    /*! Is called when IOConfiguration is received from a node.
     * @param AID alias ID of the node
     * @param ioConfiguration data that was received
     */ 
    public void ioConfiguration(int AID,IOConfiguration ioConfiguration)
    {
	logger.info("ioConfiguration AID:"+AID+" "+ioConfiguration);
	if (AID==saveConfigAID)
	{
	    this.ioConfiguration=ioConfiguration;
	    saveConfig();
	}
    }

    /*! Is called when dynamic PowerConfiguration is received from a node.
     * @param AID alias ID of the node
     * @param powerConfiguration data that was received
     */ 
    public void dynamicPowerConfiguration(int AID,PowerManagement powerManagement)
    {
	logger.info("dynamicPowerConfiguration AID:"+AID+": dynamic "+powerManagement);
	if (AID==saveConfigAID)
	{
	    this.dynamicPowerManagement=powerManagement;
	    saveConfig();
	}
    }

    /*! Is called when static PowerConfiguration is received from a node.
     * @param AID alias ID of the node
     * @param powerConfiguration data that was received
     */ 
    public void staticPowerConfiguration(int AID,PowerManagement powerManagement)
    {
	logger.info("staticPowerConfiguration AID:"+AID+": static "+powerManagement);
	if (AID==saveConfigAID)
	{
	    this.staticPowerManagement=powerManagement;
	    saveConfig();
	}
    }

    /*! Is called when ADCInput configuration is received from a node.
     * @param AID alias ID of the node
     * @param adcInput data that was received
     */ 
    public void ADCInputConfiguration(int AID,ADCInput adcInput)
    {
	logger.info("ADCInputConfiguration AID:"+AID+" "+adcInput);
	if (AID==saveConfigAID)
	{
	    this.adcInput=adcInput;
	    saveConfig();
	}
    }

    /*! Is called when DigitalInput configuration is received from a node.
     * @param AID alias ID of the node
     * @param digitalInput data that was received
     */ 
    public void digitalInputConfiguration(int AID,DigitalInput digitalInput)
    {
	logger.info("digitalInputConfiguration AID:"+AID+" "+digitalInput);
	if (AID==saveConfigAID)
	{
	    this.digitalInput=digitalInput;
	    saveConfig();
	}
    }

    /*! Is called when DigitalOutput configuration is received from a node.
     * @param AID alias ID of the node
     * @param digitalOutput data that was received
     */ 
    public void digitalOutputConfiguration(int AID,DigitalOutput digitalOutput)
    {
	logger.info("digitalOutputConfiguration AID:"+AID+" "+digitalOutput);
	if (AID==saveConfigAID)
	{
	    this.digitalOutput=digitalOutput;
	    saveConfig();
	}
    }

    /*! Is called when UART configuration is received from a node.
     * @param AID alias ID of the node
     * @param uart data that was received 
     */ 
    public void UARTConfiguration(int AID,UART uart)
    {
	logger.info("UARTConfiguration AID:"+AID+" "+uart);
	if (AID==saveConfigAID)
	{
	    this.uart=uart;
	    saveConfig();
	}
    }

    /*! Is called when timer data is received from a node at which ADC events are generated
     * @param AID alias ID of the node
     * @param time in multiple of 15 seconds
     */ 
    public void timer(int AID,int time)
    {
	logger.info("timer AID:"+AID+" "+time);
	if (AID==saveConfigAID)
	{
	    this.timer=time;
	    saveConfig();
	}
    }
  
    /*! Is called when the number of expected routers is received
     * @param AID alias ID of the node
     * @param routers number of expected routers
     */ 
    public void eventRouting(int AID,int routers)
    {
	logger.info("eventRouting AID:"+AID+" "+routers);
	if (AID==saveConfigAID)
	{
	    this.routers=routers;
	    saveConfig();
	}
    }
    
    /*! Is called when power on notification data is received
     * @param AID alias ID of the node
     * @param powerOn flag that indicates the notification status
     */ 
    public void powerOnNotification(int AID,boolean powerOn)
    {
	logger.info("powerOnNotification AID:"+AID+" "+powerOn);
	if (AID==saveConfigAID)
	{
	    this.powerOn=powerOn?1:0;
	    saveConfig();
	}
    }

    // data
    /*! Is called when digital input data is received from a node.
     * @param AID alias ID of the node
     * @param current the current value
     * @param count the current count
     */ 
    public void digitalInput(int AID,int current,int count)
    {
	logger.info("digitalInput AID:"+AID+" digitalInput: "+current+" count: "+count);
    }    

    /*! Is called when digital output data is received from a node.
     * @param AID alias ID of the node
     * @param current the current value
     * @param remainingPulseDuration see documentation
     */ 
    public void digitalOutput(int AID,int current,int remainingPulseDuration)
    {
	logger.info("digitalOutput AID:"+AID+" digitalInput: "+current+" remainingPulseDuration: "+remainingPulseDuration);
    }

    /*! Is called when ADC input data is received from a node.
     * @param AID alias ID of the node
     * @param current the current value
     * @param highThreshold is 1 if the high threshold is violated otherwise 0
     * @param lowThreshold is 1 if the low threshold is violated otherwise 0
     */ 
    public void adcInput(int AID,int current,int highThreshold,int lowThreshold)
    {
	logger.info("adcInput AID:"+AID+" adc: "+current+" highThreshold:"+highThreshold+" lowThreshold:"+lowThreshold);
    }

    /*! Helper function to convert a vector of integers to a comma
     *  seperated string of the integers values followed by a string
     *  in which the integers are interpreted as ascii values.
     * @param current vector of integers
     * @return resulting String
     */ 
    public static String uartToString(Vector<Integer> current)
    {
	String s="";
	boolean first=true;
	for (int i=0;i<current.size();i++)
	{
	    if (!first)
		s+=",";
	    first=false;
	    s+=current.elementAt(i);
	}
	s+=" chars:";
	for (int i=0;i<current.size();i++)
	{
	    int c=(int)(current.elementAt(i));
	    s+=(char)c;
	}
	return s;
    }

    /*! Is called when UART input data is received from a node.
     * @param AID alias ID of the node
     * @param current data that was received 
     */ 
    public void uartInput(int AID,Vector<Integer> current)
    {	
	logger.info("uartInput AID:"+AID+" uart: "+uartToString(current));
    }

    /*! Is called when UART output data is received from a node.
     * @param AID alias ID of the node
     * @param current data that was received 
     */ 
    public void uartOutput(int AID,Vector<Integer> current)
    {	
	logger.info("uartOutput AID:"+AID+" uart: "+uartToString(current));
    }
    
    /*! Tells this object to save all config data from AID to filename
     * @param AID alias ID of the node
     * @param filename file to save to
     */ 
    public synchronized void saveConfig(int AID,String filename)
    {
	xmlFile=filename;
	saveConfigAID=AID;
    }

    /*! Helper function that actually saves config data to file
     */
    private void saveConfig()
    {
	if (xmlFile!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    Element element=xmlSerial.document().createElement("FieldNode");
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
	    if (timer!=null)
	    {
		e=xmlSerial.document().createElement("ADC_TIMER");
		e.setAttribute("timer",""+timer);
		element.appendChild(e);
	    }
	    if (routers!=null)
	    {
		e=xmlSerial.document().createElement("EVENT_ROUTING");
		e.setAttribute("routers",""+routers);
		element.appendChild(e);
	    }
	    if (powerOn!=null)
	    {
		e=xmlSerial.document().createElement("POWER_ON_NOTIFICATION");
		e.setAttribute("powerOn",""+powerOn);
		element.appendChild(e);
	    }
	    xmlSerial.save(element,xmlFile);
	}
    }
    
}
