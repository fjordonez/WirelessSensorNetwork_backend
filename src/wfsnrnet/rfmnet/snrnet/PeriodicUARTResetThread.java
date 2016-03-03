package wfsnrnet.rfmnet.snrnet;

import org.apache.log4j.Logger;

/*! Thread to periodic send reset vectors to UART nodes as they tend to stop
 * functioning possibly because the UART buffer is not cleared
 */
public class PeriodicUARTResetThread implements Runnable
{
    private static Logger logger = Logger.getLogger(PeriodicUARTResetThread.class);
    protected Thread thread;
    protected BaseNodeBase baseNodeBase;
    protected int AID;
    protected int sleepMinutes;

    public PeriodicUARTResetThread(BaseNodeBase baseNodeBase,int AID,int sleepMinutes)
    {
	this.baseNodeBase=baseNodeBase;
	this.AID=AID;
	thread=new Thread(this);
	thread.start();
    }

    public void run()
    {
	try{

	    while (true)
	    { 	    
		Thread.sleep(sleepMinutes*60*1000);
		logger.info("periodic UART reset");
		baseNodeBase.resetUART(AID);
	    }
	}
	catch(InterruptedException e)
	{
	    logger.info("PeriodicUARTResetThread Interrupted");
	}
    }
}
