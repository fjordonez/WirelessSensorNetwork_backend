package wfsnrnet.rfmnet.snrnet.message;

import java.util.Vector;
import java.util.Date;

import org.apache.log4j.Logger;

public class RFMMessage implements MessageCallBack
{
    private static Logger logger = Logger.getLogger(RFMMessage.class);
    private MessageType messageType;
    private Vector<Integer> data=new Vector<Integer>();
    private String friendlyString;

    enum MessageType {Local,Network;}
    // frame bytes
    private static final int DLE=0x10;
    private static final int STX=0x02;
    private static final int ETX=0x03;

    private int sendCount=0;
    private int maxSendCount=0;
    private long firstSendTime=0;

    /*! Constructor. Construct local message.
     * @param localMessageType type of local message
     */
    public RFMMessage(LocalMessageType localMessageType)
    {
	messageType=MessageType.Local;
	init();
	addByte(localMessageType.value());
	finish();
    }

    /*! Constructor. Construct network message.
     * @param networkMessageType type of network message
     * @param AID alias ID of fieldnode or 0 for basenode
     * @param data additional data
     */
    public RFMMessage(NetworkMessageType networkMessageType,int AID,Integer[] data)
    {
	messageType=MessageType.Network;
	init();
	addByte(networkMessageType.value()<<4);
	addByte(0);
	addByte(0);
	addByte(AID>>8);
	addByte(AID);
	addBytes(data);
	finish();
    }

    /*! Constructor. Construct ApplicationCommand network message.
     * @param networkMessageType type of network message
     * @param AID alias ID of fieldnode or 0 for basenode
     * @param applicationCommand ApplicationCommand type
     * @param command Command type
     * @param data additional data
     */
    public RFMMessage(NetworkMessageType networkMessageType,int AID,ApplicationCommand applicationCommand,Command command,Integer[] data)
    {
	messageType=MessageType.Network;
	init();
	addByte(networkMessageType.value()<<4);
	addByte(0);
	addByte(0);
	addByte(AID>>8);
	addByte(AID);
	addByte((applicationCommand.value()<<4)+command.value());
	addBytes(data);
	finish();
    }

    /*! Constructor. Construct SystemCommand network message.
     * @param networkMessageType type of network message
     * @param AID alias ID of fieldnode or 0 for basenode
     * @param systemCommand SystemCommand type
     * @param command Command type
     * @param data additional data
     */
    public RFMMessage(NetworkMessageType networkMessageType,int AID,SystemCommand systemCommand,Command command,Integer[] data)
    {
	messageType=MessageType.Network;
	init();
	addByte(networkMessageType.value()<<4);
	addByte(0);
	addByte(0);
	addByte(AID>>8);
	addByte(AID);
	addByte((systemCommand.value()<<4)+command.value());
	addBytes(data);
	finish();
    }

    /*! Constructor. Parse data to a message removing DLE escape bytes when necessary
     * @param data data received over serial port
     */
    public RFMMessage(Vector<Integer> data)
    {	
	if (data.size()==2)
	{	    
	    messageType=MessageType.Local;
	    for (int i=0;i<data.size();i++)
	    {
		addByte(data.get(i));
	    }
	}
	else
	{
	    messageType=MessageType.Network;
	    for (int i=0;i<data.size();i++)
	    {
		addByte(data.get(i));
		if (i+1<data.size()) // not last byte
		{		    
		    if ((data.get(i)==DLE && data.get(i+1)==DLE)) // discard second DLE when DLE follows DLE
			i++;
		}
	    }
	}	
    }

    /*! Compares this message with another
     * @param m message to compare this message to
     * @return true if messages are equal, false otherwise
     */
    public boolean equals(RFMMessage m)
    {
	boolean ret=true;
	if (data.size()==m.data.size())
	{
	    for (int i=0; i<data.size(); i++)
	    {
		if (!data.get(i).equals(m.data.get(i)))
		{
		    ret=false;
		    break;
		}
	    }
	}
	else
	    ret=false;	
	return ret;
    }

    public boolean invalidResponse()
    {
	boolean ret=false;
	/*
	if (messageType==MessageType.Network && 
	    getSystemCommand().value()==SystemCommand.INVALID_RESPONSE)
	{
	    logger.error("classified as invalid response");
	    ret=true;
	}
	*/
	return ret;
    }

    /*
    public boolean validAnswer(RFMMessage answer)
    {
	boolean ret=true;
	if (messageType==MessageType.Network)
	{
	    if (answer.messageType==MessageType.Network &&
		getAID()!=answer.getAID())
		ret=false;
	}
	else
	{
	    if (answer.messageType!=MessageType.Local)
		ret=false;
	}
	    
	return ret;
    }
    */

    /*! Add the counter that counts how many times the message was
     *  attempted to be send and records the time it was first send.
     */
    public void addSendCount()
    {
	if (sendCount==0)
	{
	    Date date=new Date();
	    firstSendTime=date.getTime();
	}
	sendCount+=1;
    }

    public int getSendCount()
    { return sendCount;}
    
    public void setMaxSendCount(int maxSendCount)
    { this.maxSendCount=maxSendCount;}

    public int getMaxSendCount()
    { return maxSendCount;}

    public long getFirstSendTime()
    { return firstSendTime;}

    public boolean validSendCount()
    {
	boolean ret=true;
	if (maxSendCount>0 && sendCount>maxSendCount)
	    ret=false;
	return ret;
    }

    public MessageType getType()
    {
	return messageType;
    }

    public LocalMessageType getLocalMessageType()
    {
	return new LocalMessageType(data.elementAt(1));
    }

    public NetworkMessageType getNetworkMessageType()
    {
	return new NetworkMessageType((data.elementAt(2))>>4);
    }

    public ApplicationCommand getApplicationCommand()
    {
	return new ApplicationCommand((data.elementAt(7))>>4);
    }

    public SystemCommand getSystemCommand()
    {
	return new SystemCommand((data.elementAt(7))>>4);
    }

    public Command getCommand()
    {
	return new Command((data.elementAt(7))&15);
    }

    public boolean isEvent()
    {
	return (data.size()>7 && (data.elementAt(2)==0xff || data.elementAt(2)==0xf7) && (data.elementAt(7)&15)==0x0a);
    }

    public Event getEvent()
    {
	return new Event(data.elementAt(7));
    }

    public boolean isPowerOn()
    {
	return (data.size()>7 && data.elementAt(2)==0xff && (data.elementAt(7)&15)==0x0b);
    }

    public PowerOn getPowerOn()
    {
	return new PowerOn(data.elementAt(8));
    }

    public IOConfiguration getIOConfiguration()
    {
	return new IOConfiguration(data.elementAt(8));
    }

    public PowerManagement getPowerManagement()
    {
	return new PowerManagement(getRange(8,10));
    }
    
    public DigitalInput getDigitalInput()
    {
	return new DigitalInput(getRange(8,12));
    }
    
    public DigitalOutput getDigitalOutput()
    {
	return new DigitalOutput(getRange(8,9));
    }
    
    public ADCInput getADCInput()
    {
	return new ADCInput(getRange(8,10));
    }

    public UART getUART()
    {
	return new UART(getRange(8,9));
    }

    public int getAID()
    {
	return ((int)data.elementAt(5)<<8)+((int)data.elementAt(6));
    }

    public int get(int i) // index as in documentation
    {
	return data.elementAt(i+1);
    }

    public int size()
    {
	return data.size();
    }

    public String toStringBytes()
    {
	String string="";
	// bytes
	boolean first=true;
	for (int i = 0; i < data.size(); i++)
	{
	    if (!first)
		string+=",";
	    String h=Integer.toHexString(data.elementAt(i)%256);
	    if (h.length()==1)
		string+="0";
	    string+=h;
	    first=false;
	}
	return string;
    }

    public String toString()
    {
	String string=toStringBytes()+"\n";
	friendlyString="";
	parseMessage(this);
	string+=friendlyString;
	return string;
    }

    /*! Convert message to byte array thereby adding DLE escape bytes when necessary
     */
    public byte[] toByteArrayWithEscapeBytes()
    {	
	Vector<Byte> bytes=new Vector<Byte>();
	for (int i = 0; i < data.size(); i++)
	{
	    byte b=(byte)(data.elementAt(i)%256);
	    bytes.add(b);
	    if (b==DLE)
	    {
		if (i>1 && i<data.size()-2)
		{
		    bytes.add(b); // add DLE escape byte
		}
	    }
	}
	byte[] b=new byte[bytes.size()];
	for (int i = 0; i < bytes.size(); i++) // conert to byte array
	{
	    b[i]=bytes.get(i);
	}	
	return b;
    }

    public void parseMessage(MessageCallBack callBack)
    {
	try 
	{
	    if (messageType==MessageType.Local)
	    {
		callBack.parsedMessage(this,getLocalMessageType());
	    }
	    else
	    {
		NetworkMessageType networkMessageType=getNetworkMessageType();
		if (isEvent())
		{
		    callBack.parsedMessage(this,networkMessageType,getAID(),getEvent());
		}		
		else if (isPowerOn())
		{   		    
		    callBack.parsedMessage(this,networkMessageType,getAID(),getPowerOn());
		}		
		else if (networkMessageType.value()==NetworkMessageType.APPLICATION_IO_COMMAND ||
			 networkMessageType.value()==NetworkMessageType.APPLICATION_IO_RESPONSE)
		{
		    callBack.parsedMessage(this,networkMessageType,getAID(),getApplicationCommand(),getCommand());
		} 
		else if (networkMessageType.value()==NetworkMessageType.FIELD_NODE_SYSTEM_CONFIGURATION ||
			 networkMessageType.value()==NetworkMessageType.BASE_NODE_SYSTEM_CONFIGURATION)
		{
		    callBack.parsedMessage(this,networkMessageType,getAID(),getSystemCommand(),getCommand());
		}
		else
		{
		    callBack.parsedMessage(this,networkMessageType,getAID());
		}		
	    }
	} 
	catch (Exception e) 
	{ 
	    logger.fatal("Error when parsing message: "+e);
	}
    }

    public void parsedMessage(RFMMessage message,LocalMessageType localMessageType)
    {
	friendlyString+=localMessageType+"\n";
    }

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID)
    {
	friendlyString+=networkMessageType+"\n"+"AID: "+AID+"\n";
    }

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,Event event)
    {
	friendlyString+=networkMessageType+"\n"+"AID: "+AID+"\n"+event;
    }
    
    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,PowerOn powerOn)
    {
	friendlyString+=networkMessageType+"\n"+"AID: "+AID+"\n"+powerOn;
    }

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,
			      ApplicationCommand applicationCommand,Command command)
    {
	friendlyString+=networkMessageType+"\n"+"AID: "+AID+"\n"+applicationCommand+"\n"+command+"\n";
    }

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,
			      SystemCommand systemCommand,Command command)
    {
	friendlyString+=networkMessageType+"\n"+"AID: "+AID+"\n"+systemCommand+"\n"+command+"\n";
    }

    public Vector<Integer> getRange(int s,int e)
    {
	Vector<Integer> subVector=new Vector<Integer>();
	for (int i=s;i<=e;i++)
	{
	    subVector.add(data.elementAt(i));
	}
	return subVector;
    }

    // ------------------ private --------------------
    
    private void init()
    {
	addByte(DLE);
	if (messageType==MessageType.Network)
	    addByte(STX);
    }

    private RFMMessage finish()
    {
	if (messageType==MessageType.Network)
	{
	    addChecksum();
	    addByte(DLE);
	    addByte(ETX);
	}
	//logger.info("created message: "+this);
	return this;
    }

    private void addChecksum()
    {
	Integer checksum=0;
	for (int i = 2; i < data.size(); i++)
	{
	    checksum+=data.elementAt(i);
	    checksum%=256;
	}
	addByte(checksum);
    }

    private void addBytes(Integer[] b)
    {
	for (int i = 0; i < b.length; i++)
	    addByte(b[i]);
    }
  
    private void addByte(Integer b)
    {
	data.add(b%256);	
    }
}
