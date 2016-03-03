package wfsnrnet.rfmnet.snrnet;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import wfsnrnet.rfmnet.snrnet.message.*;
import wfsnrnet.xml.XMLSerial;

import java.util.Vector;

/*! Defines the interface to operate the BaseNode of the \htmlonly <a
 *  href="../doc/DM1810-Pro-Kit-User-Guide-Rev-1.pdf">DM1810 MESH
 *  Network</a> \endhtmlonly to send commands to other nodes. This
 *  class only prints information to screen and is overloaded by
 *  BaseNode to send messages to the nodes. The messages are
 *  described in \htmlonly <a
 *  href="../doc/AN1810-1-24.pdf">AN1810-1-24.pdf</a> \endhtmlonly
 */
public class BaseNodeBase implements Runnable
{
    private static Logger logger = Logger.getLogger(BaseNodeBase.class);
    
    protected Thread thread;
    protected boolean running=true;
    protected BaseNodeCallBack callBack;

    /*! Constructor 
     * @param callBack object that receives all messages comming back from the BaseNode
     */
    public BaseNodeBase(BaseNodeCallBack callBack)
    {
	this.callBack=callBack;
	callBack.setBaseNodeBase(this);
    }

    /*! Read firmware of the node 
     * @param AID alias ID of the node
     */
    public void readFirmware(int AID)
    {
	logger.info("readFirmware AID:"+AID);
    }

    /*! Read mode and model of the node 
     * @param AID alias ID of the node
     */
    public void readModeModel(int AID)
    {
	logger.info("readModeModel AID:"+AID);
    }

    /*! Write mode of the node 
     * @param AID alias ID of the node
     */
    public void writeMode(int AID,int dim1800Compatible)
    {
	logger.info("readModeModel AID:"+AID+" "+dim1800Compatible);
    }

    /*! Read hardwareID of the node 
     * @param AID alias ID of the node
     */
    public void readHardwareID(int AID)
    {
	logger.info("readHardwareID AID:"+AID);
    }

    // bind
    /*! Read bind configuration of the node 
     * @param AID alias ID of the node
     */
    public void readBindConfiguration(int AID)
    {
	logger.info("readBindConfiguration AID:"+AID);
    }
    /*! Write bind configuration of the node 
     * @param AID alias ID of the node
     */
    public void writeBindConfiguration(int AID)
    {
	logger.info("writeBindConfiguration AID:"+AID);
    }
    /*! Read bind list of BaseNode
     */
    public void readBaseNodeBindList()	
    {
	logger.info("readBaseNodeBindList");
    }
    /*! Resets bind list of BaseNode
     */
    public void writeBaseNodeBindList() // resets bind list
    {
	logger.info("writeBaseNodeBindList");
    }

    //link
    /*! Read link map of node
     * @param AID alias ID of the node
     */
    public void readLinkMap(int AID)
    {
	logger.info("readLinkMap AID:"+AID);
    }
    /*! Reset link map of node
     * @param AID alias ID of the node
     */
    public void resetLinkMap(int AID)
    {
	logger.info("resetLinkMap AID:"+AID);
    }

    /*! Read RSSI of node
     * @param AID alias ID of the node
     */
    public void readRSSI(int AID)
    {
	logger.info("readRSSI AID:"+AID);
    }

    // config
    /*! Resets the node. This is required for a new configuration to take effect.
     * @param AID alias ID of the node
     */
    public void resetNode(int AID)
    {
	logger.info("resetNode AID:"+AID);
    }
    /*! Reads IOConfiguration of the node
     * @param AID alias ID of the node
     */
    public void readIOConfiguration(int AID)    
    {
	logger.info("readIOConfiguration AID:"+AID);
    }
    /*! Writes IOConfiguration of the node
     * @param AID alias ID of the node
     * @param ioConfiguration the new configuration
     */
    public void writeIOConfiguration(int AID,IOConfiguration ioConfiguration)    
    {
	logger.info("writeIOConfiguration AID:"+AID+" "+ioConfiguration);
    }
    /*! Reads the dynamic PowerConfiguration of the node
     * @param AID alias ID of the node
     */
    public void readDynamicPowerConfiguration(int AID)    
    {
	logger.info("readDynamicPowerConfiguration AID:"+AID);
    }
    /*! Writes the dynamic PowerConfiguration of the node
     * @param AID alias ID of the node 
     * @param powerManagement the new configuration
     */
    public void writeDynamicPowerConfiguration(int AID,PowerManagement powerManagement)
    {
	logger.info("writeDynamicPowerConfiguration AID:"+AID+" "+powerManagement);
    }
    /*! Reads the static PowerConfiguration of the node
     * @param AID alias ID of the node
     */
    public void readStaticPowerConfiguration(int AID)    
    {
	logger.info("readStaticPowerConfiguration AID:"+AID);
    }
     /*! Writes the static PowerConfiguration of the node
     * @param AID alias ID of the node
     * @param powerManagement the new configuration
     */
    public void writeStaticPowerConfiguration(int AID,PowerManagement powerManagement)    
    {
	logger.info("writeStaticPowerConfiguration AID:"+AID+" "+powerManagement);
    }
    /*! Reads the DigitalInput configuration of the node
     * @param AID alias ID of the node
     */
    public void readDigitalInputConfiguration(int AID)    
    {
	logger.info("readDigitalInputConfiguration AID:"+AID);
    }
    /*! Writes the DigitalInput configuration of the node
     * @param AID alias ID of the node
     * @param digitalInput the new configuration
     */
    public void writeDigitalInputConfiguration(int AID,DigitalInput digitalInput)    
    {
	logger.info("writeDigitalInputConfiguration AID:"+AID+" "+digitalInput);
    }
    /*! Reads the DigitalOutput configuration of the node
     * @param AID alias ID of the node
     */
    public void readDigitalOutputConfiguration(int AID)    
    {
	logger.info("readDigitalOutputConfiguration AID:"+AID);
    }
     /*! Writes the DigitalOutput configuration of the node
     * @param AID alias ID of the node
     * @param digitalOutput the new configuration
     */
    public void writeDigitalOutputConfiguration(int AID,DigitalOutput digitalOutput)    
    {
	logger.info("writeDigitalOutputConfiguration AID:"+AID+" "+digitalOutput);
    }
    /*! Reads the ADCInput configuration of the node
     * @param AID alias ID of the node
     */
    public void readADCInputConfiguration(int AID)    
    {
	logger.info("readADCInputConfiguration AID:"+AID);
    }
    /*! Writes the ADCInput configuration of the node
     * @param AID alias ID of the node
     * @param adcInput the new configuration
     */
    public void writeADCInputConfiguration(int AID,ADCInput adcInput)    
    {
	logger.info("writeADCInputConfiguration AID:"+AID+" "+adcInput);
    }
    /*! Reads the UART configuration of the node
     * @param AID alias ID of the node
     */
    public void readUARTInputConfiguration(int AID)    
    {
	logger.info("readUARTInputConfiguration AID:"+AID);
    }
    /*! Writes the UART configuration of the node
     * @param AID alias ID of the node
     * @param uart the new configuration
     */
    public void writeUARTInputConfiguration(int AID,UART uart)
    {
	logger.info("writeUARTInputConfiguration AID:"+AID+" "+uart);
    }
    /*! Reads the timer of the node in multiple of 15 seconds at which ADC events are generated
     * @param AID alias ID of the node
     */
    public void readTimer(int AID)    
    {
	logger.info("readTimer AID:"+AID);
    }
    /*! Writes the timer of the node in multiple of 15 seconds at which ADC events are generated
     * @param AID alias ID of the node
     * @param timer time in multiple of 15 seconds
     */
    public void writeTimer(int AID,int time)
    {
	logger.info("writeWriteTimer AID:"+AID+" "+time);
    }
    /*! Reads the number of expected routers
     * @param AID alias ID of the node
     */
    public void readEventRouting(int AID)    
    {
	logger.info("readEventRouting AID:"+AID);
    }
    /*! Writes the number of expected routers
     * @param AID alias ID of the node
     * @param routers number of expected routers
     */
    public void writeEventRouting(int AID,int routers)
    {
	logger.info("writeEventRouting AID:"+AID+" "+routers);
    }
    /*! Reads if the power on notification is enabled
     * @param AID alias ID of the node
     */
    public void readPowerOnNotification(int AID)    
    {
	logger.info("readPowerOnNotification AID:"+AID);
    }
    /*! Writes that power on notification is enabled
     * @param AID alias ID of the node
     * @param powerOn notification enabled flag
     */
    public void writePowerOnNotification(int AID,boolean powerOn)
    {
	logger.info("writePowerOnNotification AID:"+AID+" "+powerOn);
    }
    /*! Writes configuration as specified in node to the node
     * @param AID alias ID of the node
     * @param node xml data specifing the configuration
     */
    public void config(int AID,Node node)
    {
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    logger.info("config AID:"+AID+" node:"+xmlSerial.toString(node));
	}
	else
	    logger.error("no XML data");
    }

    // io
    /*! Reads the current DigitalInput value of the node
     * @param AID alias ID of the node
     */
    public void readDigitalInput(int AID)
    {
	logger.info("readDigitalInput AID:"+AID);
    }
    /*! Reads the current DigitalOutput value of the node
     * @param AID alias ID of the node
     */
    public void readDigitalOutput(int AID)
    {
	logger.info("readDigitalOutput AID:"+AID);
    }
    /*! Reads the current ADCInput value of the node
     * @param AID alias ID of the node
     */
    public void readADCInput(int AID)
    {
	logger.info("readADCInput AID:"+AID);
    }
    /*! Resets the current ADCInput value of the node
     * @param AID alias ID of the node
     */
    public void writeADCInput(int AID)
    {
	logger.info("writeADCInput AID:"+AID);
    }
    /*! Reads the current UART value of the node
     * @param AID alias ID of the node
     */
    public void readUART(int AID)
    {
	logger.info("readUART AID:"+AID);
    }

    /*! Sends the UART reset vector which clears the buffer
     * @param AID alias ID of the node
     * @param current the new value specified as a vector of integers
     */
    public void resetUART(int AID)
    {
	logger.info("resetUART AID:"+AID);
    }
    
    /*! Writes the current UART value of the node
     * @param AID alias ID of the node
     * @param current the new value specified as a vector of integers
     */
    public void writeUART(int AID,Vector<Integer> current)
    {	
	String s="uartInput ";
	boolean first=true;
	for (int i=0;i<current.size();i++)
	{
	    if (!first)
		s+=", ";
	    first=false;
	    s+=current.elementAt(i);
	}
	logger.info("writeUART AID:"+AID+" uart: "+s);
    }    

    // reset
    /*! Resets the current DigitalInput value of the node
     * @param AID alias ID of the node
     */
    public void resetDigitalInput(int AID)
    {
	logger.info("resetDigitalInput AID:"+AID);
    }

    // ---------------------------
    
    /*! Starts the event loop of BaseNodeBase or subclasses in a
     *  seperate thread as opposed to run() that starts it in the
     *  current thread
     */
    public void startThread()
    {
	thread=new Thread(this);
	thread.start();
    }

    /*! Schedules a message to be send to a node
     */
    public void send(RFMMessage message)
    {
    }

    /*! Start running the event loop for sending and receiving
     *  messages from nodes
     */
    public void run()
    {	
    }

    /*! Parses user readable commands in string format and send them
     *  to nodes. Use command "-h" to get a list of commands.
     * @return String with feedback on commands
     */
    public String commands(String commands)
    {
	String[] com=split(commands);	
	String ret="";
	XMLSerial xmlSerial=new XMLSerial();

	for (int comIndex=0;comIndex<com.length;comIndex++)
	{
	    if (com[comIndex].equals(""))
	    {
		break;
	    } 
	    else if (com[comIndex].equals("-h"))
	    {
		ret+="usage: \n"+
		    "options:\n"+		    
		    "  -resetbind                             : resets bind list of base node\n"+
		    "  -readbind                              : reads bind list of base node\n"+
		    "  -reset AID                             : resets node 'AID'\n"+	    
		    "  -config AID <xmlfile>                  : set config of Node 'AID' to that in 'xmlfile'\n"+
		    "  -saveconfig AID <xmlfile>              : save config of Node 'AID' to 'xmlfile'\n"+	    	    
		    "  -r AID <read-property>                 : read property of node 'AID'\n"+
		    "  -w AID <write-property> <parameter>    : write property of node 'AID'\n"+
		    "  -exit								  : exit command UI\n"+
		    "read-properties:\n"+	    
		    " digitalInput    : \n"+
		    " ADC             : \n"+
		    " UART            : \n"+
		    " bindList        : \n"+	    
		    " hardwareID      : \n"+
		    " firmware        : \n"+
		    " modemodel       : \n"+
		    " linkMap         : \n"+
		    " rssi            : \n"+
		    "write-properties:\n"+	    
		    " ADC             : \n"+
		    " modemodel       : \n"+
		    " UART            : \n"+
		    " linkMap         : ";
	    }
	    else if (com[comIndex].equals("-resetbind"))
	    {
		writeBindConfiguration(0);
		writeBaseNodeBindList();
		resetNode(0);
	    }   
	    else if (com[comIndex].equals("-readbind"))
	    {
		readBaseNodeBindList();		
	    }	    
	    else if (com[comIndex].equals("-reset"))
	    {
		try
		{		    
		    int AID=Integer.parseInt(com[++comIndex]);
		    resetNode(AID);
		} 
		catch (NumberFormatException e)
		{
		    ret+="cannot parse AID: '"+com[comIndex]+"'\n";
		}
	    }
	    else if (com[comIndex].equals("-config"))
	    {
		try
		{		    
		    int AID=Integer.parseInt(com[++comIndex]);
		    String filename=com[++comIndex];
		    Node node=xmlSerial.load(filename);
		    if (node!=null)
			config(AID,xmlSerial.getChild(node,"FieldNode"));		    
		    else
			ret+="cannot read file: '"+filename+"'\n";
		} 
		catch (NumberFormatException e)
		{
		    ret+="cannot parse AID: '"+com[comIndex]+"'\n";
		}
	    }
	    else if (com[comIndex].equals("-saveconfig"))
	    {
		try
		{		    
		    int AID=Integer.parseInt(com[++comIndex]);
		    String filename=com[++comIndex];
		    callBack.saveConfig(AID,filename); // tells callback to save all config data received to file		    
		    readIOConfiguration(AID);
		    readDynamicPowerConfiguration(AID);
		    readStaticPowerConfiguration(AID);
		    readDigitalInputConfiguration(AID);
		    readDigitalOutputConfiguration(AID);
		    readADCInputConfiguration(AID);
		    readUARTInputConfiguration(AID);
		    readTimer(AID);
		    readEventRouting(AID);
		    readPowerOnNotification(AID);
		} 
		catch (NumberFormatException e)
		{
		    ret+="cannot parse AID: '"+com[comIndex]+"'\n";
		}
	    }
	    else if (com[comIndex].equals("-r"))
	    {
		try
		{
			logger.info("Command r");
			if ((com[comIndex+1]!=null) && (com[comIndex+2]!=null)){
				logger.info("No son null");
			    int AID=Integer.parseInt(com[++comIndex]);
			    comIndex++;
			    if (com[comIndex].equals("digitalInput"))
				readDigitalInput(AID);
			    else if (com[comIndex].equals("ADC"))
				readADCInput(AID);
			    else if (com[comIndex].equals("UART"))
				readUART(AID);
			    else if (com[comIndex].equals("bindList"))
				readBindConfiguration(AID);
			    else if (com[comIndex].equals("hardwareID"))
				readHardwareID(AID);
			    else if (com[comIndex].equals("firmware"))
				readFirmware(AID);
			    else if (com[comIndex].equals("modemodel"))
				readModeModel(AID);
			    else if (com[comIndex].equals("linkMap"))
				readLinkMap(AID);
			    else if (com[comIndex].equals("rssi"))
				readRSSI(AID);
			    else
				ret+="unkown read-properties: '"+com[comIndex]+"'\n";
			}else{
				ret+="-r invalid number of parameters '\n";
			}

		} 
		catch (NumberFormatException e)
		{
		    ret+="cannot parse AID: '"+com[comIndex]+"'\n";
		}
	    }
	    else if (com[comIndex].equals("-w"))
	    {
		try
		{
		    int AID=Integer.parseInt(com[++comIndex]);
		    comIndex++;
		    if (com[comIndex].equals("modemodel"))
			writeMode(AID,Integer.parseInt(com[++comIndex]));
		    else if (com[comIndex].equals("UART"))
			writeUART(AID,new Vector<Integer>());
		    else if (com[comIndex].equals("ADC"))
		    resetDigitalInput(AID);// resets event	 
		    else if (com[comIndex].equals("linkMap"))
			resetLinkMap(AID);
		    else 
			ret+="unkown write-properties: '"+com[comIndex]+"'\n";
		} 
		catch (NumberFormatException e)
		{
		    ret+="cannot parse AID: '"+com[comIndex]+"'\n";
		}
	    }
	    else if (com[comIndex].equals("-exit"))
	    {
		try
		{
		    return "exit";
		} 
		catch (Exception e){}
	    }	    
	    else
	    {
		ret+="unkown command '"+com[comIndex]+"'\n";
	    }	
	}
	return ret;
    }

    private String[] split(String commands)
    {	
	commands=commands.trim();
	String commandsOld;
	do // remove double whitespaces
	{   commandsOld=new String(commands);
	    commands = commands.replaceAll("  "," ");
	}
	while (!commandsOld.equals(commands));
	String[] split = commands.split(" ");
	return split;
    }

}
