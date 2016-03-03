package wfsnrnet.rfmnet.snrnet.message;

public class RFMException extends Exception 
{

	private static final long serialVersionUID = 1L;

	public RFMException() {}
    
    public RFMException(String msg) 
    {
	super(msg);
    }
}
