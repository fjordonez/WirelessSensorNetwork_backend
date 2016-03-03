package wfsnrnet.rfmnet.snrnet.message;

public class ApplicationCommand
{
    public static final int DIGITAL_INPUT       =1;//Byte.parseByte("0001",2);
    public static final int ADC_8BIT_RESOLUTION =2;//Byte.parseByte("0010",2);
    public static final int DIGITAL_OUTPUT      =3;//Byte.parseByte("0011",2);
    public static final int UART_BUFFER         =4;//Byte.parseByte("0100",2);
    public static final int ADC_10BIT_RESOLUTION=10;//Byte.parseByte("1010",2);
    public static final int EVENT_FLAGS         =14;//Byte.parseByte("1110",2);
    public static final int DYN_POWER_MANAGEMENT=15;//Byte.parseByte("1111",2);
    public static final int VALID_RESPONSE      =0;
    public static final int INVALID_RESPONSE    =4;

    private int v;
    public ApplicationCommand(int v)
    {
	this.v=v;
    }

    public int value()
    {
	return v;
    }

    public String toString()
    {
	String ret="ApplicationCommand.";
	switch(v)
	{
	case DIGITAL_INPUT:
	    ret+="DIGITAL_INPUT";
	    break;
	case ADC_8BIT_RESOLUTION:
	    ret+="ADC_8BIT_RESOLUTION";
	    break;
	case DIGITAL_OUTPUT:
	    ret+="DIGITAL_OUTPUT";
	    break;
	case UART_BUFFER:
	    ret+="UART_BUFFER or .INVALID_RESPONSE";
	    break;
	case ADC_10BIT_RESOLUTION:
	    ret+="ADC_10BIT_RESOLUTION";
	    break;
	case EVENT_FLAGS:
	    ret+="EVENT_FLAGS";
	    break;
	case DYN_POWER_MANAGEMENT:
	    ret+="DYN_POWER_MANAGEMENT";
	    break;
	case VALID_RESPONSE:
	    ret+="VALID_RESPONSE";
	    break;
	default :
	    ret+="***Undefined***";
	    break;
	}
	return ret;
    }


}