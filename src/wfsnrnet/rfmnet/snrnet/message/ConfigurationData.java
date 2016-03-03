package wfsnrnet.rfmnet.snrnet.message;

import java.util.Vector;

import org.w3c.dom.*;

import wfsnrnet.xml.XMLSerial;

public class ConfigurationData
{
    protected Vector<Integer> data;

    public ConfigurationData()
    {
	data=new Vector<Integer>();
    }

    public void set(Vector<Integer> v)
    {
	data=v;
    }

    public Integer get(int i)
    {
	return data.elementAt(i);
    }

    public Integer[] toIntArray()
    {	
	return data.toArray(new Integer[data.size()]);
    }

    public Node toNode(XMLSerial xmlSerial)
    {
	return null;
    }

    public String toString()
    {
	String string="";
	// bytes
	boolean first=true;
	for (int i = 0; i < data.size(); i++)
	{
	    if (!first)
		string+=",";
	    String h=Integer.toBinaryString(data.elementAt(i)%256);
	    for (int j=0;j<8-h.length();j++)
		string+="0";
	    string+=h;
	    first=false;
	}
	string+="\n";
	XMLSerial xmlSerial=new XMLSerial();
	string+=xmlSerial.toString(toNode(xmlSerial));
	return string;
    }
}
