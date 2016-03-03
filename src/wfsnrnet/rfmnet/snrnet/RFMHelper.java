package wfsnrnet.rfmnet.snrnet;

import java.util.Vector;

public class RFMHelper
{    

    public static String VectorToString(Vector<Integer> vec)
    {
	String s="";
	for (int i=0;i<vec.size();i++)
	{
	    int x=vec.elementAt(i);
	    if (x!=10 && x!=13)
		s+=(char)x;
	}
	return s;
    }

    public static Vector<Integer> StringToVector(String str)
    {
	Vector<Integer> v=new Vector<Integer>();
	for (int i=0;i<str.length();i++)
	{	    
	    v.add((int)str.charAt(i));	    
	}
	return v;
    }

}
