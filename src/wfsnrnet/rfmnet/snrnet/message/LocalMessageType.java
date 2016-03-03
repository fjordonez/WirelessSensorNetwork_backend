package wfsnrnet.rfmnet.snrnet.message;

public class LocalMessageType
{
    public static final int BASE_RESET_COMMAND   =0x04;
    public static final int BASE_AUTOSEND_COMMAND=0x16;
    public static final int COMMAND_ACCEPTED     =0x30;
    public static final int BASE_RESET_COMPLETE  =0x04;
    public static final int COMMAND_NO_OP        =0x31;
    public static final int RESPONSE_TIMEOUT     =0x32;
    public static final int NETWORK_TIMEOUT      =0x33;
    public static final int RESPONSE_ERROR       =0x34;
    public static final int SERIAL_UART_ERROR    =0x35;
    public static final int INVALID_LENGTH       =0x36;
    public static final int INVALID_DLE_SEQUENCE =0x37;

    private int v;
    public LocalMessageType(int v)
    {
	this.v=v;
    }

    public int value()
    {
	return v;
    }

    public String toString()
    {
	String ret="LocalMessageType.";
	switch(v)
	{
	case BASE_RESET_COMMAND:
	    ret+="BASE_RESET_COMMAND";
	    break;
	case BASE_AUTOSEND_COMMAND:
	    ret+="BASE_AUTOSEND_COMMAND";
	    break;
	case COMMAND_ACCEPTED:
	    ret+="COMMAND_ACCEPTED";
	    break;
	    /*case BASE_RESET_COMPLETE:
	    ret+="BASE_RESET_COMPLETE";
	    break;*/
	case COMMAND_NO_OP:
	    ret+="COMMAND_NO_OP";
	    break;
	case RESPONSE_TIMEOUT:
	    ret+="RESPONSE_TIMEOUT";
	    break;
	case NETWORK_TIMEOUT:
	    ret+="NETWORK_TIMEOUT";
	    break;
	case RESPONSE_ERROR:
	    ret+="RESPONSE_ERROR";
	    break;
	case SERIAL_UART_ERROR:
	    ret+="SERIAL_UART_ERROR";
	    break;
	case INVALID_LENGTH:
	    ret+="INVALID_LENGTH";
	    break;
	case INVALID_DLE_SEQUENCE:
	    ret+="INVALID_DLE_SEQUENCE";
	    break;
	default :
	    ret+="***Undefined***";
	    break;
	}
	return ret;
    }

}
