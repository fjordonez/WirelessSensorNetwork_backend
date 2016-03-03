package wfsnrnet.rfmnet.snrnet;

import gnu.io.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import wfsnrnet.rfmnet.snrnet.message.*;
import wfsnrnet.xml.XMLSerial;


/*! Overloads the interface defined by BaseNodeBase to actually send
 *  messages to nodes.
 */
public class BaseNode extends BaseNodeBase implements SerialPortEventListener, MessageCallBack
{
    private static Logger logger = Logger.getLogger(BaseNode.class);

    private static final int MAX_NODES=1500;
    private static final int TIMEOUT_MS=5000;
    //private static final long MAX_MESSAGE_AGE=10*60*60*1000;
    private static final long MAX_MESSAGE_AGE=10*1000;

    protected SerialPort serialPort;
    protected InputStream inputStream;
    protected OutputStream outputStream;

    MessageQueue messageQueue=new MessageQueue();
    enum MessageResponse {OK,RETRY,REMOVE}
    MessageResponse response;
    RFMMessage lastMessageSend=null;

    /*! Constructor 
     * @param callBack object that receives all messages comming back from the BaseNode
     */    
    /*public BaseNode(BaseNodeCallBack callBack) throws RFMException
    {	
	super(callBack);
	logger.info("constructing BaseNode");
	init("/dev/ttyUSB0");
    }*/

    /*! Constructor 
     * @param callBack object that receives all messages coming back from the BaseNode
     * @param portName name of the device that represented the connection to the BaseNode, default is "/dev/ttyUSB0"
     */
    public BaseNode(BaseNodeCallBack callBack,String portName) throws RFMException
    {
	super(callBack);	
	init(portName);	
    }
    
    public void readFirmware(int AID)
    {
	super.readFirmware(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.FIRMWARE_VERSION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    public void readModeModel(int AID)
    {
	super.readModeModel(AID);
	if (AID==0)
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.OVERALL_NETWORK_CONFIGURATION),
				new Command(Command.READ_CONFIGURATION),
				new Integer[]{}));
	}
	else
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.OVERALL_NETWORK_CONFIGURATION),
				new Command(Command.READ_CONFIGURATION),
				new Integer[]{}));
	}
    }

    public void writeMode(int AID,int dim1800Compatible)
    {
	super.writeMode(AID,dim1800Compatible);
	if (AID==0)
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.OVERALL_NETWORK_CONFIGURATION),
				new Command(Command.WRITE_CONFIGURATION),
				new Integer[]{dim1800Compatible<<7}));
	}
	else
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.OVERALL_NETWORK_CONFIGURATION),
				new Command(Command.WRITE_CONFIGURATION),
				new Integer[]{dim1800Compatible<<7}));
	}
    }

    public void readHardwareID(int AID)
    {
	super.readHardwareID(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.BIT32_HARDWARE_ID),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    // bind
    public void readBindConfiguration(int AID)
    {
	super.readBindConfiguration(AID);
	if (AID==0)
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.BIND_CONFIGURATION),
				new Command(Command.READ_CONFIGURATION),
				new Integer[]{}));
	}
	else
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.BIND_CONFIGURATION),
				new Command(Command.READ_CONFIGURATION),
				new Integer[]{}));
	}
    }

    public void writeBindConfiguration(int AID) // resets bind configuration
    {
	super.writeBindConfiguration(AID);
	if (AID==0)
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.BIND_CONFIGURATION),
				new Command(Command.WRITE_CONFIGURATION),
				new Integer[]{0,0,0,0}));
	}
	else
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.BIND_CONFIGURATION),
				new Command(Command.WRITE_CONFIGURATION),
				new Integer[]{0,0,0,0}));
	}
    }

    public void readBaseNodeBindList()
    {
	super.readBaseNodeBindList();
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
			    0,
			    new SystemCommand(SystemCommand.BASE_STATION_BIND_LIST),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{0,0}));
    }
    
    public void writeBaseNodeBindList() // resets bind list
    {
	super.writeBaseNodeBindList();
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
			    0,
			    new SystemCommand(SystemCommand.BASE_STATION_BIND_LIST),
			    new Command(Command.WRITE_CONFIGURATION),
			    new Integer[]{0,0}));
    }
    
    // link
    public void readLinkMap(int AID)
    {
	super.readLinkMap(AID);
	if (AID==0)
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.BIT32_HARDWARE_ID),
				new Command(Command.LINK_MAP),
				new Integer[]{}));
	}
	else
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.BIT32_HARDWARE_ID),
				new Command(Command.LINK_MAP),
				new Integer[]{}));
	}
    }
    
    public void resetLinkMap(int AID)
    {
	super.resetLinkMap(AID);
	if (AID==0)
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.DIGITAL_OUTPUT_CONFIGURATION),
				new Command(Command.LINK_MAP),
				new Integer[]{}));
	}
	else
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.DIGITAL_OUTPUT_CONFIGURATION),
				new Command(Command.LINK_MAP),
				new Integer[]{}));
	}
    }

    public void readRSSI(int AID)
    {
	super.readRSSI(AID);
	if (AID==0)
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.OVERALL_IO_CONFIGURATION),
				new Command(Command.READ_IO),
				new Integer[]{}));
	}
	else
	{
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,
				new SystemCommand(SystemCommand.OVERALL_IO_CONFIGURATION),
				new Command(Command.READ_IO),
				new Integer[]{}));
	}
    }

    // config
    public void resetNode(int AID) // reset node after reconfigure for configuration to take effect
    {
	super.resetNode(AID);
	if (AID==0)
	    send(new RFMMessage(new LocalMessageType(LocalMessageType.BASE_RESET_COMMAND)));
	else
	    send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
				AID,			    
				new Integer[]{0xf8}));
    }

    public void readIOConfiguration(int AID)
    {
	super.readIOConfiguration(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.NODE_CONFIGURATION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    public void writeIOConfiguration(int AID,IOConfiguration ioConfiguration)
    {
	super.writeIOConfiguration(AID,ioConfiguration);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.NODE_CONFIGURATION),
			    new Command(Command.WRITE_CONFIGURATION),
			    ioConfiguration.toIntArray()));
    }

    public void readDynamicPowerConfiguration(int AID)
    {
	super.readDynamicPowerConfiguration(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.DYN_POWER_MANAGEMENT),
			    new Command(Command.READ_IO),
			    new Integer[]{}));
    }

    public void writeDynamicPowerConfiguration(int AID,PowerManagement powerManagement)
    {
	super.writeDynamicPowerConfiguration(AID,powerManagement);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.DYN_POWER_MANAGEMENT),
			    new Command(Command.WRITE_IO),
			    powerManagement.toIntArray()));
    }

    public void readStaticPowerConfiguration(int AID)
    {
	super.readStaticPowerConfiguration(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.OVERALL_IO_CONFIGURATION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    public void writeStaticPowerConfiguration(int AID,PowerManagement powerManagement)
    {
	super.writeStaticPowerConfiguration(AID,powerManagement);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.OVERALL_IO_CONFIGURATION),
			    new Command(Command.WRITE_CONFIGURATION),
			    powerManagement.toIntArray()));
    }

    
    public void readDigitalInputConfiguration(int AID)
    {
	super.readDigitalInputConfiguration(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.DIGITAL_INPUT_CONFIGURATION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    public void writeDigitalInputConfiguration(int AID,DigitalInput digitalInput)
    {
	super.writeDigitalInputConfiguration(AID,digitalInput);		
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.DIGITAL_INPUT_CONFIGURATION),
			    new Command(Command.WRITE_CONFIGURATION),
			    digitalInput.toIntArray()));
    }

    public void readDigitalOutputConfiguration(int AID)
    {
	super.readDigitalOutputConfiguration(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.DIGITAL_OUTPUT_CONFIGURATION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    public void writeDigitalOutputConfiguration(int AID,DigitalOutput digitalOutput)
    {
	super.writeDigitalOutputConfiguration(AID,digitalOutput);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.DIGITAL_OUTPUT_CONFIGURATION),
			    new Command(Command.WRITE_CONFIGURATION),
			    digitalOutput.toIntArray()));
    }

    public void readADCInputConfiguration(int AID)
    {
	super.readADCInputConfiguration(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.ADC_CONFIGURATION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    public void writeADCInputConfiguration(int AID,ADCInput adcInput)
    {
	super.writeADCInputConfiguration(AID,adcInput);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.ADC_CONFIGURATION),
			    new Command(Command.WRITE_CONFIGURATION),
			    adcInput.toIntArray()));
    }

    public void readUARTInputConfiguration(int AID)
    {
	super.readUARTInputConfiguration(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.UART_CONFIGURATION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }

    public void writeUARTInputConfiguration(int AID,UART uart)
    {
	super.writeUARTInputConfiguration(AID,uart);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.UART_CONFIGURATION),
			    new Command(Command.WRITE_CONFIGURATION),
			    uart.toIntArray()));	
    }

    public void readTimer(int AID)    
    {
	super.readTimer(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.TIMER_CONFIGURATION),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }
    
    public void writeTimer(int AID,int time)
    {
	super.writeTimer(AID,time);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.TIMER_CONFIGURATION),
			    new Command(Command.WRITE_CONFIGURATION),
			    new Integer[]{time,0}));
    }
    
    public void readEventRouting(int AID)    
    {
	super.readEventRouting(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.SYSTEM_LIST),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }
    
    public void writeEventRouting(int AID,int routers)
    {
	super.writeEventRouting(AID,routers);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.SYSTEM_LIST),
			    new Command(Command.WRITE_CONFIGURATION),
			    new Integer[]{routers}));
    }
    
    public void readPowerOnNotification(int AID)    
    {
	super.readPowerOnNotification(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.BASE_STATION_BIND_LIST),
			    new Command(Command.READ_CONFIGURATION),
			    new Integer[]{}));
    }
    
    public void writePowerOnNotification(int AID,boolean powerOn)
    {
	super.writePowerOnNotification(AID,powerOn);
	int p=0;
	if (!powerOn)
	    p=8;
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
			    AID,
			    new SystemCommand(SystemCommand.BASE_STATION_BIND_LIST),
			    new Command(Command.WRITE_CONFIGURATION),
			    new Integer[]{p,0}));
    }

    public void config(int AID,Node node)
    {	
	super.config(AID,node);
	if (node!=null)
	{   	    
	    XMLSerial xmlSerial=new XMLSerial();
	    IOConfiguration ioConfiguration;
	    PowerManagement powerManagement;
	    DigitalOutput digitalOutput;
	    DigitalInput digitalInput;
	    ADCInput adcInput;
	    UART uart;
	    Node n;
	    n=xmlSerial.getChild(node,"IOConfiguration");
	    if (n!=null)
	    {
		ioConfiguration=new IOConfiguration(n);		
		writeIOConfiguration(AID,ioConfiguration);
	    }		    
	    n=xmlSerial.getChild(node,"StaticPowerManagement");
	    if (n!=null)
	    {		
		Node n2=xmlSerial.getChild(n,"PowerManagement");
		if (n2!=null)
		{
		    powerManagement=new PowerManagement(n2);
		    writeStaticPowerConfiguration(AID,powerManagement);
		}
	    }
	    n=xmlSerial.getChild(node,"DigitalOutput");
	    if (n!=null)
	    {
		digitalOutput=new DigitalOutput(n);
		writeDigitalOutputConfiguration(AID,digitalOutput);
	    }
	    n=xmlSerial.getChild(node,"DigitalInput");
	    if (n!=null)
	    {
		digitalInput=new DigitalInput(n);
		writeDigitalInputConfiguration(AID,digitalInput);		
	    }
	    n=xmlSerial.getChild(node,"ADCInput");
	    if (n!=null)
	    {
		adcInput=new ADCInput(n);
		writeADCInputConfiguration(AID,adcInput);
	    }
	    n=xmlSerial.getChild(node,"UART");
	    if (n!=null)
	    {
		uart=new UART(n);
		writeUARTInputConfiguration(AID,uart);
	    }
	    n=xmlSerial.getChild(node,"ADC_TIMER");
	    if (n!=null)
	    {
		String s=xmlSerial.getAttribute(n,"timer");
		try
		{
		    int time=Integer.parseInt(s);
		    writeTimer(AID,time);
		}
		catch (NumberFormatException e)
		{
		    logger.error("ADC_TIMER: cant parse '"+s+"'");
		}		
	    }
	    n=xmlSerial.getChild(node,"EVENT_ROUTING");
	    if (n!=null)
	    {
		String s=xmlSerial.getAttribute(n,"routers");
		try
		{
		    int routers=Integer.parseInt(s);
		    writeEventRouting(AID,routers);
		}
		catch (NumberFormatException e)
		{
		    logger.error("EVENT_ROUTING: cant parse '"+s+"'");
		}		
	    }
	    n=xmlSerial.getChild(node,"POWER_ON_NOTIFICATION");
	    if (n!=null)
	    {
		String s=xmlSerial.getAttribute(n,"powerOn");
		try
		{
		    int powerOn=Integer.parseInt(s);
		    writePowerOnNotification(AID,powerOn>0);
		}
		catch (NumberFormatException e)
		{
		    logger.error("POWER_ON_NOTIFICATION: cant parse '"+s+"'");
		}		
	    }
	    resetNode(AID); // reset Node
	    n=xmlSerial.getChild(node,"DynamicPowerManagement");
	    if (n!=null)
	    {
		Node n2=xmlSerial.getChild(n,"PowerManagement");
		if (n2!=null)
		{
		    powerManagement=new PowerManagement(n2);
		    writeDynamicPowerConfiguration(AID,powerManagement);
		}
	    }
	}
	else
	    logger.error("no XML data");
    }

    // io
    public void readDigitalInput(int AID)
    {
	super.readDigitalInput(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.DIGITAL_INPUT),
			    new Command(Command.READ_IO),
			    new Integer[]{}));
    }
    
    public void readDigitalOutput(int AID)
    {
	super.readDigitalOutput(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.DIGITAL_OUTPUT),
			    new Command(Command.READ_IO),
			    new Integer[]{}));
    }

    public void readADCInput(int AID)
    {
	super.readADCInput(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.ADC_8BIT_RESOLUTION),
			    new Command(Command.READ_IO),
			    new Integer[]{}));
    }    

    public void writeADCInput(int AID)
    {
	super.writeADCInput(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.ADC_8BIT_RESOLUTION),
			    new Command(Command.WRITE_IO),
			    new Integer[]{}));
    }

    public void readUART(int AID)
    {
	super.readUART(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.UART_BUFFER ),
			    new Command(Command.READ_IO),
			    new Integer[]{}));
    }

    public void resetUART(int AID)
    {
        super.resetUART(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.UART_BUFFER ),
			    new Command(Command.WRITE_IO),
			    new Integer[]{0x00,0x6e,0x29,0x6d,0x36,0x4a,0x7c,0x4e,0x6b}));
    }

    public void writeUART(int AID,Vector<Integer> current)
    {	
	super.writeUART(AID,current);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.UART_BUFFER),
			    new Command(Command.WRITE_IO),
			    current.toArray(new Integer[]{})));
    }    
    
    // reset
    public void resetDigitalInput(int AID)
    {
	super.resetDigitalInput(AID);
	send(new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
			    AID,
			    new ApplicationCommand(ApplicationCommand.DIGITAL_INPUT),
			    new Command(Command.WRITE_IO ),
			    new Integer[]{}));
    }

    private void init(String portName) throws RFMException
    {
	logger.info("connecting port: "+portName);
	try
        {	    
	    CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	    portIdentifier.isCurrentlyOwned();
	    try
	    {
		CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
		if ( commPort instanceof SerialPort )
		{
		    serialPort = (SerialPort) commPort;
		    serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
		    inputStream = serialPort.getInputStream();
		    outputStream= serialPort.getOutputStream();
		    serialPort.addEventListener(this);
		    serialPort.notifyOnDataAvailable(true);

		}
	    }
	    catch ( Exception e )
	    {
		throw new RFMException("Failed to open port");
	    }
	}
	catch (NoSuchPortException e)
	{
	    throw new RFMException("No such port found");
	}
    }

    public void send(RFMMessage message)
    {
	messageQueue.add(message);
    }

    private void sendMessage(RFMMessage message) throws Exception
    {
	logger.info("sending message: "+message);
	lastMessageSend=message;
	message.addSendCount();
	byte[] bytes=message.toByteArrayWithEscapeBytes();
	Vector<Integer> data=new Vector<Integer>();
	for (int i=0;i<bytes.length;i++)
	    data.add((Integer)(int)bytes[i]&0xFF);
	logger.info("sending bytes  : "+toString(data));	
	outputStream.write(bytes);
    }

    private String toString(Vector<Integer> data)
    {
	String s=new String();
	for (int i=0;i<data.size();i++)
	{
	    if (i>0)
		s+=",";
	    String h=Integer.toHexString(data.get(i));
	    if (h.length()==1)
		s+="0";
	    s+=h;
	}
	return s;
    }

    public void run()
    {
	//Date date=new Date();
	logger.info("thread started");
	while (running)
	{
	    try
	    {			
		for (int i=0;i<30;i++) // no-priority sleep loop
		{
		    if (messageQueue.hasPriority()) // break sleep when a priority node is set
		    {
			if (i>0) logger.info("no priority sleep interrupted");
			break;
		    }
		    if (i==0) logger.info("no priority sleep");
		    try{ Thread.currentThread().sleep(100);}
		    catch(InterruptedException ie) { }		    
		}
		logger.info("wait for messageQueue");	
		RFMMessage message=messageQueue.get(); // block wait for message in queue	
		try
		{
		    sendMessage(message);
		    logger.info("wait for response");
		    switch (waitForResponds())
		    {
		    case OK:
			logger.info("response positive");
			break;
		    case RETRY:
			long age=(new Date().getTime())-message.getFirstSendTime();
			logger.info("message age is:"+age+" (MAX_MESSAGE_AGE "+MAX_MESSAGE_AGE+")");
			if (age<MAX_MESSAGE_AGE)
			{
			    if (message.validSendCount())
			    {
				logger.info("response negative, return message back to queue (sendCount:"+message.getSendCount()+")");
				messageQueue.returnMessage(message);
			    }
			    else
			    {
				logger.info("response negative, remove message sendCount exceeded max: "+message.getSendCount());
			    }
			}
			else
			{
			    logger.info("response negative, remove message because it is older than: "+age);
			}
			
			break;
		    case REMOVE:
			logger.info("response negative, remove message");
			break;
		    }
		}
		catch ( Exception e )
		{
		    logger.info("failed to send message");
		}
	    }
	    catch (InterruptedException e)
	    {
		logger.info("thread interrupted");
	    } 
	}
	logger.info("thread ending");
    }

    private MessageResponse waitForResponds()
    {
	response=MessageResponse.RETRY;
	try
	{
	    Date date1=new Date();
	    synchronized (this) {wait(TIMEOUT_MS);}
	    Date date2=new Date();
	    if (date2.getTime()-date1.getTime()>TIMEOUT_MS)
		logger.info("timeout: no message received in "+TIMEOUT_MS+"ms");
	}
	catch (InterruptedException e)
	{
	    logger.info("wait for response interrupted");
	}
	return response;
    }

    /*! Callback function that is called when a message is received from
     *  the BaseNode on the serial port. The message then parsed.
     * @param event the incomming messages
     */
    public void serialEvent(SerialPortEvent event) 
    {
	switch (event.getEventType()) 
	{
	case SerialPortEvent.BI:
	    logger.info("serialEvent: Break interrupt");
	    break;
	case SerialPortEvent.OE:
	    logger.info("serialEvent: Overrun error");
	    break;
	case SerialPortEvent.FE:
	    logger.info("serialEvent: Framing error");
	    break;
	case SerialPortEvent.PE:
	    logger.info("serialEvent: Parity error");
	    break;
	case SerialPortEvent.CD:
	    logger.info("serialEvent: Carrier detect");
	    break;
	case SerialPortEvent.CTS:
	    logger.info("serialEvent: Clear to send");
	    break;
	case SerialPortEvent.DSR:
	    logger.info("serialEvent: Data set ready");
	    break;
	case SerialPortEvent.RI:
	    logger.info("serialEvent: Ring indicator");
	    break;
	case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	    logger.info("serialEvent: Output buffer is empty");
	    break;
	case SerialPortEvent.DATA_AVAILABLE:
	    logger.info("serialEvent: Data available at the serial port");
	    Vector<Integer> data=new Vector<Integer>();
	    try 
	    {
		while (inputStream.available() > 0)
		{
		    data.add(inputStream.read());
		}
		logger.info("receiving bytes  : "+toString(data));
		RFMMessage message=new RFMMessage(data);
		logger.info("receiving message: "+message);
		if (message.invalidResponse())
		{
		    callBack.invalidResponse(message.getAID(),lastMessageSend,message);
		    response=MessageResponse.REMOVE;
		    synchronized (this) {notify();} // invalidates lastMessageSend
		}
		else
		{
		    message.parseMessage(this); // calls back on parsedMessage methods below
		}
	    } 
	    catch (Exception e) 
	    { 
		logger.fatal("Error when handling incomming message: "+e);
	    }
	    break;
	}
    }
    
    /*! Receives messages parsed as local message
     * @param message the messages
     * @param localMessageType type of local message
     */
    public void parsedMessage(RFMMessage message,LocalMessageType localMessageType)
    {
	switch (localMessageType.value())
	{
	case LocalMessageType.BASE_RESET_COMPLETE:
	    callBack.baseNodeReset();
	    break;
	case LocalMessageType.COMMAND_ACCEPTED:
	    response=MessageResponse.OK;
	    synchronized (this) {notify();}
	    break;
	case LocalMessageType.COMMAND_NO_OP:
	    response=MessageResponse.REMOVE;
	    synchronized (this) {notify();}
	    break;
	case LocalMessageType.RESPONSE_TIMEOUT:
	case LocalMessageType.NETWORK_TIMEOUT:
	case LocalMessageType.RESPONSE_ERROR:
	case LocalMessageType.SERIAL_UART_ERROR:
	case LocalMessageType.INVALID_LENGTH:
	case LocalMessageType.INVALID_DLE_SEQUENCE:
	    response=MessageResponse.RETRY;
	    synchronized (this) {notify();}
	    break;
	}
    }

    /*! Receives messages parsed as network message
     * @param message the messages
     * @param networkMessageType type of local message
     * @param AID alias ID of node that send the message
     */
    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID)
    {
	logger.info("UNHANDLED message: "+message);
    }

    /*! Receives messages parsed as Event network message and send it to the callback object
     * @param message the messages
     * @param networkMessageType type of local message
     * @param AID alias ID of node that send the message
     * @param event Event data
     */
    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,Event event)
    {			
	callBack.event(AID,event);
	messageQueue.addPriority(message.getAID()); // sets priority for messages of this AID
    }

    /*! Receives messages parsed as PowerOn network message and send it to the callback object
     * @param message the messages
     * @param networkMessageType type of local message
     * @param AID alias ID of node that send the message
     * @param powerOn powerOn data
     */
    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,PowerOn powerOn)
    {
	callBack.powerOn(AID,powerOn);
	messageQueue.addPriority(message.getAID()); // sets priority for messages of this AID
    }

     /*! Receives messages parsed as ApplicationCommand message and send it to the callback object
     * @param message the messages
     * @param networkMessageType type of local message
     * @param AID alias ID of node that send the message
     * @param applicationCommand ApplicationCommand data
     * @param command Command data
     */
    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,
			      ApplicationCommand applicationCommand,Command command)
    {
	if (message.getCommand().value()==Command.READ_CONFIGURATION |
	    message.getCommand().value()==Command.READ_IO)
	{
	    switch (lastMessageSend.getApplicationCommand().value())
	    {
	    case ApplicationCommand.DYN_POWER_MANAGEMENT:
		callBack.dynamicPowerConfiguration(message.getAID(),message.getPowerManagement());
		break;
	    case ApplicationCommand.DIGITAL_INPUT:
		int current=message.get(7)&0x01;
		int count=(message.get(8)<<16)+(message.get(9)<<8)+(message.get(10));		
		callBack.digitalInput(message.getAID(),current,count);
		break;
	    case ApplicationCommand.DIGITAL_OUTPUT:			
		callBack.digitalOutput(message.getAID(),message.get(8)&1,message.get(9));
		break;
		/*
	    case ApplicationCommand.ADC_10BIT_RESOLUTION:		
		Integer high=((message.get(7)&ADCInput.HIGH_THRESHOLD)>0)?1:0;
		Integer low=((message.get(7)&ADCInput.LOW_THRESHOLD)>0)?1:0;
		callBack.adcInput(message.getAID(),(message.get(8)<<8)+message.get(9),high,low);		
		break;
		*/
	    case ApplicationCommand.ADC_8BIT_RESOLUTION:		
		Integer high=((message.get(7)&ADCInput.HIGH_THRESHOLD)>0)?1:0;
		Integer low=((message.get(7)&ADCInput.LOW_THRESHOLD)>0)?1:0;
		callBack.adcInput(message.getAID(),message.get(8),high,low);
		break;
	    case ApplicationCommand.UART_BUFFER:		
		callBack.uartInput(message.getAID(),message.getRange(8,message.size()-4));
		break;
	    default:
		logger.info("UNHANDLED READ_IO ApplicationCommand: "+message);
	    }
	}
	else if (message.getCommand().value()==Command.WRITE_IO)
	{
	    switch (message.getApplicationCommand().value())
	    {
	    case ApplicationCommand.VALID_RESPONSE:
	    case ApplicationCommand.UART_BUFFER:
		switch (lastMessageSend.getApplicationCommand().value())
		{
		case ApplicationCommand.DIGITAL_INPUT:
		    logger.info("DIGITAL_INPUT write command accepted");
		    break;
		case ApplicationCommand.ADC_8BIT_RESOLUTION:
		    logger.info("ADC_8BIT_RESOLUTION write command accepted");
		    break;
		case ApplicationCommand.UART_BUFFER:
		    callBack.uartOutput(message.getAID(),message.getRange(8,message.size()-4));
		    break;
		case ApplicationCommand.DYN_POWER_MANAGEMENT:
		    logger.info("DYN_POWER_MANAGEMENT write command accepted");
		    break;
		default:
		    logger.info("UNHANDLED WRTIE_IO ApplicationCommand: "+message);
		}
		break;	    
	    default:
		logger.info("UNHANDLED ApplicationCommand REPLY: "+message.getApplicationCommand().toString());	  	    
	    }  
	}
	else
	    logger.info("UNHANDLED ApplicationCommand message: "+message);
	response=MessageResponse.OK;
	synchronized (this) {notify();} // invalidates lastMessageSend
    }

    /*! Receives messages parsed as SystemCommand message and send it to the callback object
     * @param message the messages
     * @param networkMessageType type of local message
     * @param AID alias ID of node that send the message
     * @param systemCommand SystemCommand data
     * @param command Command data
     */
    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,
			      SystemCommand systemCommand,Command command)
    {
	if (message.getCommand().value()==Command.READ_CONFIGURATION |
	    message.getCommand().value()==Command.READ_IO)
	{
	    switch (lastMessageSend.getSystemCommand().value())
	    {
	    case SystemCommand.FIRMWARE_VERSION:		
		callBack.firmware(message.getAID(),(message.get(7)>>4)*10+(message.get(7)&15));
		break;
	    case SystemCommand.OVERALL_NETWORK_CONFIGURATION:
		callBack.modeModel(message.getAID(),message.get(7)&128,message.get(7)&7);
		break;
	    case SystemCommand.BIT32_HARDWARE_ID:
		callBack.hardwareID(message.getAID(),Integer.toHexString((message.get(7)<<16)+(message.get(8)<<8)+message.get(9)));
		break;
	    case SystemCommand.BIND_CONFIGURATION:
		callBack.bindConfiguration(message.getAID(),message.get(7),message.get(8),message.get(9),message.get(10));
		break;
	    case SystemCommand.BASE_STATION_BIND_LIST:	
		if (message.getAID()==0)
		    callBack.baseNodeBindList((message.get(7)&0xf0)>>4,((message.get(7)&0x03)<<8)+message.get(8));
		else
		    callBack.powerOnNotification(message.getAID(),message.get(7)==0);
		break;
	    case SystemCommand.NODE_CONFIGURATION:
		callBack.ioConfiguration(message.getAID(),message.getIOConfiguration());
		break;
	    case SystemCommand.OVERALL_IO_CONFIGURATION:
		if (message.getCommand().value()==Command.READ_IO) // RSSI
		{
		    Vector<Integer> data=new Vector<Integer>();
		    for (int i=7;i<message.size()-4;i++)
			data.add(message.get(i));
		    callBack.rssi(message.getAID(),data);
		}
		else // staticPowerConfiguration
		    callBack.staticPowerConfiguration(message.getAID(),message.getPowerManagement());
		break;
	    case SystemCommand.DIGITAL_INPUT_CONFIGURATION:
		callBack.digitalInputConfiguration(message.getAID(),message.getDigitalInput());
		break;
	    case SystemCommand.DIGITAL_OUTPUT_CONFIGURATION:
		callBack.digitalOutputConfiguration(message.getAID(),message.getDigitalOutput());
		break;
	    case SystemCommand.ADC_CONFIGURATION:
		callBack.ADCInputConfiguration(message.getAID(),message.getADCInput());
		break;
	    case SystemCommand.UART_CONFIGURATION:
		callBack.UARTConfiguration(message.getAID(),message.getUART());
		break;
	    case SystemCommand.TIMER_CONFIGURATION:
		callBack.timer(message.getAID(),message.get(7));
		break;
	    case SystemCommand.SYSTEM_LIST:
		callBack.eventRouting(message.getAID(),message.get(7));
		break;	    	
	    default:
		logger.info("UNHANDLED SystemCommand: "+message);
	    }
	}
	else if (message.getCommand().value()==Command.LINK_MAP)
	{
	    if (message.size()>11)
		callBack.linkMap(message.getAID(),message.get(7),message.get(8));
	    else
		logger.info("link map reset accepted by: "+message.getAID());
	}
	else if (message.getCommand().value()==Command.WRITE_CONFIGURATION)
	{
	    logger.info("Configuration command accepted");
	}
	else if (message.getCommand().value()==0x08)
	{
	    logger.info("FieldNode reset accepted");
	}
	else
	    logger.info("UNHANDLED SystemCommand message: "+message);
	response=MessageResponse.OK;
	synchronized (this) {notify();} // invalidates lastMessageSend
    }

    public void discoverNodes()
    {
	for (int i=1;i<MAX_NODES;i++)
	{
	    RFMMessage message=new RFMMessage(new NetworkMessageType(NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION),
					      i,
					      new SystemCommand(SystemCommand.BIT32_HARDWARE_ID),
					      new Command(Command.READ_CONFIGURATION),
					      new Integer[]{});
	    message.setMaxSendCount(1); // only send ones
	    send(message);
	}
    }

}
