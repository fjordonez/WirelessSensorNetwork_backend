package wfsnrnet.modules;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import wfsnrnet.comm.mqtt.MQTTConfiguration;
import wfsnrnet.comm.mqtt.MQTTModule;
import wfsnrnet.data.TopicList;
import wfsnrnet.rfmnet.snrnet.BaseNode;
import wfsnrnet.rfmnet.snrnet.BaseNodeBase;
import wfsnrnet.rfmnet.snrnet.BaseNodeCallBack;
import wfsnrnet.rfmnet.snrnet.message.Event;
import wfsnrnet.rfmnet.snrnet.message.RFMException;
import wfsnrnet.rfmnet.snrnet.message.RFMMessage;
import wfsnrnet.xml.parser.MQTTConfigurationParser;

public class SensorNetworkModule extends MQTTModule {
	
    private BaseNodeBase baseNode;
    private CallBack callBack;
    private TopicList topicList;
    private MQTTConfiguration SensorNetworkMQTTConfig;
    private String sensorNetworkMQTTConfigFile;
    private String commandListenerMQTTConfigFile;
    private String COMMAND_RESPONSE_TOPIC;
    private String SENSOR_EVENTS_TOPIC;
	static final SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yy HH:mm:ss:SSSS");
    
    public SensorNetworkModule(String sensorNetworkMQTTConfigFile, String commandListenerMQTTConfigFile, TopicList topicList)
    {
    	this.topicList = topicList;
    	this.COMMAND_RESPONSE_TOPIC = this.topicList.findName("commandResponse");
    	this.SENSOR_EVENTS_TOPIC = this.topicList.findName("sensorEvents");
    	this.sensorNetworkMQTTConfigFile = sensorNetworkMQTTConfigFile;
    	this.commandListenerMQTTConfigFile = commandListenerMQTTConfigFile; 	
		System.out.println("SensorNetworkModule::SensorNetworkModule Starting sensor data module");   
		System.out.println("SensorNetworkModule::SensorNetworkModule Parsing file "+this.sensorNetworkMQTTConfigFile);
		SensorNetworkMQTTConfig = MQTTConfigurationParser.parse(this.sensorNetworkMQTTConfigFile,topicList);
		System.out.println("SensorNetworkModule::SensorNetworkModule Connection parameters ->");
		System.out.println(SensorNetworkMQTTConfig.toString());
	   
    }
    
    public void start(String interfacePortName){
    	
		super.startMQTT(SensorNetworkMQTTConfig);
		
		new Thread ( new CommandListener(commandListenerMQTTConfigFile,this.topicList) ).start();
		
	    try
	    {
			callBack=new CallBack();
			baseNode=new BaseNode(callBack,interfacePortName);
			baseNode.startThread();	
			baseNode.readBaseNodeBindList();// get bind list in order reset UART data
	    }
	    catch ( RFMException e )
	    {
	    	System.out.println("SensorNetworkModule::SensorNetworkModule Failed to connect to basenode: "+e);
	    }
    }

    private void sendMessage(String topic, String message)
    {
    	//System.out.println("SensorNetworkModule::sendMessage - Recibido elemento, enviando "+message+" "+sdf.format((Calendar.getInstance()).getTime()));
    	//publishMessage(topic,topic+"_"+message+"_"+sdf.format((Calendar.getInstance()).getTime()));
    	publishMessage(topic,message+"_"+sdf.format((Calendar.getInstance()).getTime()));
    }
    
    class CommandListener extends MQTTModule implements Runnable {

    	MQTTConfiguration commandListenerMQTTConfig;

    	public CommandListener(String mqttConfigFile, TopicList topicList){
			System.out.println("CommandListener::CommandListener Parsing file "+mqttConfigFile);
			commandListenerMQTTConfig = MQTTConfigurationParser.parse(mqttConfigFile,topicList);
			System.out.println("CommandListener::CommandListener Connection parameters ->");
			System.out.println(commandListenerMQTTConfig.toString());
    	}
    	
		@Override 	
		public void run() {
			startMQTT(commandListenerMQTTConfig);

		}
		
		protected void processMessage(String message) {
			//System.out.println("CommandListener::processMessage Got message: " + message);
			String result=baseNode.commands(message);
			
			//System.out.println("CommandListener::processMessage Result: " + message);
			publishMessage(COMMAND_RESPONSE_TOPIC,result);
		}
    	
    }
    
    private class CallBack extends BaseNodeCallBack
    {
		//protected Boolean resetUART=true;
		//private Map<Integer,Integer> aidToADCThreshold = new HashMap<Integer,Integer>();
		//private Map<Integer,Timer> aidToTimer = new HashMap<Integer,Timer>();
	
		public void invalidResponse(int AID,RFMMessage send,RFMMessage response)
		{
		    super.invalidResponse(AID,send,response);
		    /*try
		    {
				TextMessage m=session.createTextMessage("");
				m.setStringProperty("sensorID",name);
				m.setStringProperty("notification",
						    "BaseNode received: INVALID_RESPONSE AID:"+
						    AID+"\n send: "+send+" response: "+response);
				MessageHelper.setTime(m);
				notificationProducer.send(m);
		    }
		    catch (JMSException e)
		    {
			logger.fatal("failed to send 'invalidResponse' notification message: "+e);
		    }*/
		}

		@Override 
		public void event(int AID,Event event)
		{
			if ((event.get(0)&Event.WAKE_UP_EVENT)>0)
			{
			    baseNodeBase.readDynamicPowerConfiguration(AID);// resets event
			}
			if ((event.get(0)&Event.UART_EVENT)>0)
			{   
			    baseNodeBase.readUART(AID);
			    baseNodeBase.writeUART(AID,new Vector<Integer>());// resets event
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
			    //sendMessage(SENSOR_EVENTS_TOPIC,"digitalEvent;"+AID);
			}
		}


		public void baseNodeBindList(int nrRouters,int nrNodes)
		{
		    super.baseNodeBindList(nrRouters,nrNodes);	    
		    /*if (resetUART) // try to reset UART data on all fieldNodes, otherwise these nodes give no data
		    {
			resetUART=false;
			for (int i=1;i<=nrNodes;i++)
			{
			    logger.info("trying UART reset on fieldnode: "+i);
			    RFMMessage message=new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
							      i,
							      new ApplicationCommand(ApplicationCommand.UART_BUFFER),
							      new Command(Command.WRITE_IO),
							      new Integer[]{});
			    message.setMaxSendCount(3);
			    baseNodeBase.send(message);
			}
		    }*/
		}

		public void hardwareID(int AID,String hardwareID)
		{
		    super.hardwareID(AID,hardwareID);
		    //messageSender.setHardwareID(AID,hardwareID);
		}
    
		// data
		public void digitalInput(int AID,int current,int count)
		{
		    super.digitalInput(AID,current,count);
		    /*try
		    {
			TextMessage m=session.createTextMessage("");
			MessageHelper.setTime(m);
			m.setIntProperty("value",current);
			m.setIntProperty("count",count);		
			messageSender.addMessage(new NonConfigMessage(name,AID,SensorType.DIGITAL,m));
		    }
		    catch (JMSException e)
		    {
			logger.error("failed to process DIGITAL SensorEvent message: "+e);
		    }*/
		    
		    //System.out.println("SensorDataHandler::digitalInput "+AID+" "+current);
		    sendMessage(SENSOR_EVENTS_TOPIC,"digitalInput;"+AID+";"+current);
		}    

		/*public void adcInput(int AID,int current,int highThreshold,int lowThreshold)
		{
		    super.adcInput(AID,current,highThreshold,lowThreshold);
		    try
		    {
			if (highThreshold>0 || lowThreshold>0) // if we received a threshold violation
			{
			    if (highThreshold==lowThreshold)
				logger.error("receiving both high and low ADC thresholds, high takes precedance");
			    Integer thresholdNew=highThreshold;
			    Integer thresholdLast=aidToADCThreshold.get(AID);
			    if (thresholdLast==null || thresholdNew!=thresholdLast) // if threshold is different than last
			    {
				aidToADCThreshold.put(AID,thresholdNew);
				TextMessage m=session.createTextMessage("");
				MessageHelper.setTime(m);
				m.setIntProperty("value",thresholdNew);
				messageSender.addMessage(new NonConfigMessage(name,AID,SensorType.ADC,m));
			    }
			}
		    }
		    catch (JMSException e)
		    {
			logger.error("failed to process ADC SensorEvent message: "+e);
		    }
		}*/

		/*! Task that is scheduled to send periodic UART reset messages
		 */
		/*class WriteUARTTask extends TimerTask 
		{
		    private int AID;
		    public WriteUARTTask(int AID)
		    {
			this.AID=AID;
		    }
	
		    public void run()
		    {
			logger.info("reset UART node AID:"+AID+" on timer expired");
			RFMMessage message=new RFMMessage(new NetworkMessageType(NetworkMessageType.APPLICATION_IO_COMMAND),
							  AID,
							  new ApplicationCommand(ApplicationCommand.UART_BUFFER ),
							  new Command(Command.WRITE_IO),
							  new Integer[]{0x00,0x6e,0x29,0x6d,0x36,0x4a,0x7c,0x4e,0x6b});
			message.setMaxSendCount(2);
			baseNode.send(message);
		    }
		}*/


		/*public void uartInput(int AID,Vector<Integer> current)
		{
		    super.uartInput(AID,current);
		    
		    // use timer to send a UART reset when nothing is heard from an AID for more than X seconds 
		    Timer timer=aidToTimer.get(AID);
		    if (timer!=null)
		    {
			logger.info("delay timer for UART node reset AID:"+AID);
			timer.cancel(); // cancel current timer as we heard from this AID
		    }
		    timer=new Timer(); // (re)create timer for UART reset
		    timer.schedule(new WriteUARTTask(AID),90*1000,90*1000);
		    aidToTimer.put(AID,timer);
	
		    String s=RFMHelper.VectorToString(current);	  
		    logger.info("string: "+s);
		    try
		    {
			TextMessage m=session.createTextMessage("");
			MessageHelper.setTime(m);
			String[] split=s.split(":");
			logger.info("split[0]: "+split[0]);
			if (split[0].equals("BP"))
			{
			    m.setIntProperty("value",Integer.parseInt(split[1]));
			    messageSender.addMessage(new NonConfigMessage(name,AID,SensorType.BP,m));
			}
			else if (split[0].equals("HR"))
			{
			    String[] values=split[1].split(",");
			    m.setIntProperty("hartRate",Integer.parseInt(values[0]));
			    m.setIntProperty("quality",Integer.parseInt(values[1]));
			    m.setIntProperty("timeMeasured",Integer.parseInt(values[2]));
			    messageSender.addMessage(new NonConfigMessage(name,AID,SensorType.HR,m));
			}
			else 
			{
			    logger.error("unhandled UART message: "+s);
			}
		    }
		    catch (JMSException e)
		    {
			logger.error("failed to process UART SensorEvent message: "+e);
		    }
		}*/
    }
    
}
