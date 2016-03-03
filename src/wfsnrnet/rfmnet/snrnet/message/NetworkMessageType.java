package wfsnrnet.rfmnet.snrnet.message;

public class NetworkMessageType
{
    public static final int FIELD_NODE_SYSTEM_CONFIGURATION=7; //Byte.parseByte("0111",2);
    public static final int BASE_NODE_SYSTEM_CONFIGURATION =15;//Byte.parseByte("1111",2);
    public static final int SYSTEM_CONGIURATION_RESPONSE   =15;//Byte.parseByte("1111",2);
    public static final int APPLICATION_IO_COMMAND         =6; //Byte.parseByte("0110",2);
    public static final int APPLICATION_IO_RESPONSE        =14;//Byte.parseByte("1110",2);
    public static final int EVENT_MESSAGE                  =15;//Byte.parseByte("1111",2);

    private int v;
    public NetworkMessageType(int v)
    {
	this.v=v;
    }

    public int value()
    {
	return v;
    }

    public String toString()
    {
	String ret="NetworkMessageType.";
	switch(v)
	{
	case FIELD_NODE_SYSTEM_CONFIGURATION:
	    ret+="FIELD_NODE_SYSTEM_CONFIGURATION";
	    break;
	    /*case BASE_NODE_SYSTEM_CONFIGURATION:
	    ret+="BASE_NODE_SYSTEM_CONFIGURATION";
	    break;
	case SYSTEM_CONGIURATION_RESPONSE:
	    ret+="SYSTEM_CONGIURATION_RESPONSE";
	    break;*/
	case APPLICATION_IO_COMMAND:
	    ret+="APPLICATION_IO_COMMAND";
	    break;
	case APPLICATION_IO_RESPONSE:
	    ret+="APPLICATION_IO_RESPONSE";
	    break;
	case EVENT_MESSAGE:
	    ret+="EVENT_MESSAGE or .SYSTEM_CONFIGURATION_RESPONSE or .BASE_NODE_SYSTEM_CONFIGURATION";
	    break;
	default :
	    ret+="***Undefined***";
	    break;
	}

	return ret;
    }
}