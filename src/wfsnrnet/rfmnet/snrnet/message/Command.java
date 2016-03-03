package wfsnrnet.rfmnet.snrnet.message;

public class Command
{
    public static final int READ_CONFIGURATION =13;
    public static final int WRITE_CONFIGURATION=5;
    public static final int READ_IO            =9;
    public static final int WRITE_IO           =1;
    public static final int EVENT_MESSAGE      =12;
    public static final int LINK_MAP           =14;

    private int v;
    public Command(int v)
    {
	this.v=v;
    }

    public int value()
    {
	return v;
    }
    
    public String toString()
    {
	String ret="Command.";
	switch(v)
	{
	case READ_CONFIGURATION:
	    ret+="READ_CONFIGURATION";
	    break;
	case WRITE_CONFIGURATION:
	    ret+="WRITE_CONFIGURATION";
	    break;
	case READ_IO:
	    ret+="READ_IO";
	    break;
	case WRITE_IO:
	    ret+="WRITE_IO";
	    break;
	case EVENT_MESSAGE:
	    ret+="EVENT_MESSAGE";
	    break;
	case LINK_MAP:
	    ret+="LINK_MAP";
	    break;
	default :
	    ret+="***Undefined***";
	    break;
	}
	return ret;
    }
}
