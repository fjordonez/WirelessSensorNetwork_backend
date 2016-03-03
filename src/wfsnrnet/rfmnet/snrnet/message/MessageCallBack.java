package wfsnrnet.rfmnet.snrnet.message;

public interface MessageCallBack
{
    public void parsedMessage(RFMMessage message,LocalMessageType localMessageType);

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID);

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,Event event) throws InterruptedException;

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,PowerOn powerOn);

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,
			      ApplicationCommand applicationCommand,Command command);

    public void parsedMessage(RFMMessage message,NetworkMessageType networkMessageType,int AID,
			      SystemCommand systemCommand,Command command);
}
