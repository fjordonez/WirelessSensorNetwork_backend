package wfsnrnet.rfmnet.snrnet.message;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

public class PowerOn extends ConfigurationData
{
    public PowerOn(int d)
    {	
	data.add(d);
    }

    public PowerOn(int codeVersion,int revisionVersion)
    {
	init(codeVersion,revisionVersion);
    }
    
    public PowerOn(Node node)
    {
	int codeVersion=0;
	int revisionVersion=0;
	if (node!=null)
	{
	    XMLSerial xmlSerial=new XMLSerial();
	    try
	    {
		codeVersion=Integer.parseInt(xmlSerial.getAttribute(node,"CodeVersion"));
		revisionVersion=Integer.parseInt(xmlSerial.getAttribute(node,"RevisionVersion"));
	    }
	    catch (NumberFormatException e)
	    {
	    }
	}
	init(codeVersion,revisionVersion);
    }

    public Node toNode(XMLSerial xmlSerial)
    {	
	Element element=xmlSerial.document().createElement("PowerOn");
	element.setAttribute("CodeVersion",""+getCodeVersion());
	element.setAttribute("RevisionVersion",""+getRevisionVersion());
	return element;
    }

    public void init(int codeVersion,int revisionVersion)
    {
	data.add(codeVersion<<4+revisionVersion);
    }

    public int getCodeVersion()
    {
	return (data.elementAt(0))>>4;
    }

    public int getRevisionVersion()
    {
	return (data.elementAt(0))&15;
    }
}
