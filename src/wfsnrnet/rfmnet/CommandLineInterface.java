package wfsnrnet.rfmnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import wfsnrnet.rfmnet.snrnet.BaseNode;
import wfsnrnet.rfmnet.snrnet.BaseNodeBase;
import wfsnrnet.rfmnet.snrnet.BaseNodeCallBack;
import wfsnrnet.rfmnet.snrnet.message.RFMException;


public class CommandLineInterface {
	private static Logger logger = Logger.getLogger(CommandLineInterface.class);
	private String interfacePortName;
	
    public CommandLineInterface(String interfacePortName)
    {
    	this.interfacePortName = interfacePortName;
    }
    
    public void start()
    {
    	try
        {	    
		    BaseNodeCallBack callBack=new BaseNodeCallBack();
		    BaseNodeBase baseNode;
			baseNode=new BaseNode(callBack,interfacePortName);	
		    baseNode.startThread();
	
		    String result=""; 
	
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in) );
		    //while (true)
		    while(!result.equals("exit"))
		    {
				try
				{
				    System.out.print("/:>");
				    String read = br.readLine();
				    result=baseNode.commands(read);
				    System.out.println("Res: \n"+result);
				}
				catch (IOException e )
				{
				    logger.error(e);
				}
		
		    }
		}
		catch ( RFMException e )
		{
		    logger.error(e);
		}
    }
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		String portName = "/dev/ttyUSB0"; //default
		if (args.length > 0){
			portName = args[0];
		}
		
		CommandLineInterface commandLine=new CommandLineInterface(portName);
		commandLine.start();
	}
}
