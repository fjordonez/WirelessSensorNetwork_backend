package wfsnrnet.rfmnet.snrnet;

import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import wfsnrnet.rfmnet.snrnet.message.RFMMessage;

public class MessageQueue
{
    private static Logger logger = Logger.getLogger(MessageQueue.class);

    List<RFMMessage> messages = new LinkedList<RFMMessage>(); // all message
    List<Integer> priority = new LinkedList<Integer>();
    
    public MessageQueue()
    {	
    }

    public synchronized void add(RFMMessage message) // add at end of list
    {
	addHelp(message,messages.size());
    }

    public synchronized void addFront(RFMMessage message) // add at start of list
    {
	addHelp(message,0);	
    }

    public synchronized void addHelp(RFMMessage message,int index)
    {
	boolean found=false;
	ListIterator<RFMMessage> i = messages.listIterator();
	while (i.hasNext())
	{
	    RFMMessage m=i.next();
	    if (m.equals(message))
	    {
		found=true;
		break;
	    }	    
	}	
	if (found)
	{
	    logger.info("not adding duplicate message");
	}
	else
	{
	    messages.add(index,message);
	    logger.info("add message, size: "+messages.size()+" data:"+message.toStringBytes());
	    notify();
	}
    } 

    // add at start of list and make sure all messages witch the same AID (different than '0') are
    // moved above it so message order is preserved
    public synchronized void returnMessage(RFMMessage message)
    {
	int aid=message.getAID();
	if (message.getSendCount()%3==0) // remove priority when message could not be send for 5 times
	    removePriority(aid);
	List<RFMMessage> removed=new LinkedList<RFMMessage>();
	if (aid!=0)
	{
	    // remove all message with same AID
	    ListIterator< RFMMessage > i = messages.listIterator();	    
	    while (i.hasNext())
	    {
		RFMMessage m=i.next();
		if (m.getAID()==aid)
		{
		    i.remove();
		    removed.add(m);
		}
	    }
	}
	messages.add(message);
	// add all message with same AID that were removed above 
	ListIterator< RFMMessage > i=removed.listIterator();
	while(i.hasNext())
	{
	    messages.add(i.next());
	}
	
	logger.info("returning message to queue, size: "+messages.size());
	notify();
    }

    public synchronized RFMMessage get() throws InterruptedException
    {
	RFMMessage ret=null;
	
	logger.info("size: "+messages.size());
	if (messages.size()==0)
	{
	    logger.info("waiting for message");
	    wait();
	}
	if (priority.size()==0) // get first message
	{
	    logger.info("get first message");
	    ret=messages.remove(0);
	}
	else // get first message of AID prio
	{
	    logger.info(priorityQueueToString());
	    boolean found=false;
	    Integer prio=priority.get(0);	   
	    logger.info("get first prioritized message for: "+prio);
	    ListIterator< RFMMessage > iterator = messages.listIterator();
	    while ( iterator.hasNext() ) 
	    {
		ret = iterator.next();               
		if (ret.getAID()==prio)
		{
		    iterator.remove();
		    found=true;
		    break;
		}
	    }
	    if (!found)
	    {
		logger.info("no prioritized message found for: "+prio);
		removeCurrentPriority(); // no message found with this AID
		ret=get();
	    }
	}
	return ret;
    }

    public synchronized void addPriority(Integer p)
    {
	boolean found=false;
	ListIterator<Integer> j = priority.listIterator(priority.size());
	while (j.hasPrevious())
	{
	    if (j.previous()==p)
		found=true;
	}
	if (!found)
	{
	    logger.info("add priority: "+p);		
	    priority.add(p);
	}
    }
    
    private synchronized void removePriority(Integer p)
    {
	logger.info("remove priority: "+p);
	ListIterator<Integer> j = priority.listIterator(priority.size());
	while (j.hasPrevious())
	{
	    if (j.previous()==p)
		j.remove();
	}
    }

    private synchronized void removeCurrentPriority()
    {
	priority.remove(0);
    }

    // returns if some node has priority
    public synchronized boolean hasPriority()
    {
	if (priority.size()>0)
	    return true;
	else
	    return false;
    }

    public synchronized String toString()
    {
	return messageQueueToString()+priorityQueueToString();
    }

    public synchronized String messageQueueToString()
    {
	String ret="Message Queue:\n";
	ListIterator<RFMMessage> i = messages.listIterator(messages.size());
	while (i.hasPrevious())
	{
	    RFMMessage m=i.previous();
	    ret+=m;
	}	
	return ret;
    }

    public synchronized String priorityQueueToString()
    {
	String ret="priorities: ";
	ListIterator<Integer> j = priority.listIterator();
	while (j.hasNext())
	{
	    Integer in=j.next();
	    ret+=in+",";
	}
	ret+="\n";
	return ret;
    }

}
