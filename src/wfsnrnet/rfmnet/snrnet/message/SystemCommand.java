package wfsnrnet.rfmnet.snrnet.message;

public class SystemCommand
{
    public static final int OVERALL_NETWORK_CONFIGURATION=1;//Byte.parseByte("0001",2);
    public static final int OVERALL_IO_CONFIGURATION     =2;//Byte.parseByte("0010",2);
    public static final int BIND_CONFIGURATION           =3;//Byte.parseByte("0011",2);
    public static final int BASE_STATION_BIND_LIST       =4;//Byte.parseByte("0100",2);
    public static final int DIGITAL_INPUT_CONFIGURATION  =5;//Byte.parseByte("0101",2);
    public static final int ADC_CONFIGURATION            =6;//Byte.parseByte("0110",2);
    public static final int DIGITAL_OUTPUT_CONFIGURATION =7;//Byte.parseByte("0111",2);
    public static final int UART_CONFIGURATION           =8;//Byte.parseByte("1000",2);
    public static final int TIMER_CONFIGURATION          =9;//Byte.parseByte("1001",2);
    public static final int SYSTEM_LIST                  =10;//Byte.parseByte("1010",2);
    public static final int USER_A_MESSAGE_STRING        =11;//Byte.parseByte("1011",2);
    public static final int USER_B_MESSAGE_STRING        =12;//Byte.parseByte("1100",2);
    public static final int NODE_CONFIGURATION           =13;//Byte.parseByte("1101",2);
    public static final int FIRMWARE_VERSION             =14;//Byte.parseByte("1110",2);
    public static final int BIT32_HARDWARE_ID            =15;//Byte.parseByte("1111",2);
    public static final int VALID_RESPONSE               =0;//Byte.parseByte("0000",2);
    public static final int INVALID_RESPONSE             =4;//Byte.parseByte("0100",2);

    private int v;
    public SystemCommand(int v)
    {
	this.v=v;
    }

    public int value()
    {
	return v;
    }

    public String toString()
    {
	String ret="SystemCommand.";
	switch(v)
	{
	case OVERALL_NETWORK_CONFIGURATION:
	    ret+="OVERALL_NETWORK_CONFIGURATION";
	    break;
	case OVERALL_IO_CONFIGURATION:
	    ret+="OVERALL_IO_CONFIGURATION";
	    break;
	case BIND_CONFIGURATION:
	    ret+="BIND_CONFIGURATION";
	    break;
	case BASE_STATION_BIND_LIST:
	    ret+="BASE_STATION_BIND_LIST or .INVALID_RESPONSE";
	    break;
	case DIGITAL_INPUT_CONFIGURATION:
	    ret+="DIGITAL_INPUT_CONFIGURATION";
	    break;
	case ADC_CONFIGURATION:
	    ret+="ADC_CONFIGURATION";
	    break;
	case DIGITAL_OUTPUT_CONFIGURATION:
	    ret+="DIGITAL_OUTPUT_CONFIGURATION";
	    break;
	case UART_CONFIGURATION:
	    ret+="UART_CONFIGURATION";
	    break;
	case TIMER_CONFIGURATION:
	    ret+="TIMER_CONFIGURATION";
	    break;
	case SYSTEM_LIST:
	    ret+="SYSTEM_LIST";
	    break;
	case USER_A_MESSAGE_STRING:
	    ret+="USER_A_MESSAGE_STRING";
	    break;
	case USER_B_MESSAGE_STRING:
	    ret+="USER_B_MESSAGE_STRING";
	    break;
	case NODE_CONFIGURATION:
	    ret+="NODE_CONFIGURATION";
	    break;
	case FIRMWARE_VERSION:
	    ret+="FIRMWARE_VERSION";
	    break;
	case BIT32_HARDWARE_ID:
	    ret+="BIT32_HARDWARE_ID";
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
