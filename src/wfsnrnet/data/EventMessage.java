package wfsnrnet.data;

import java.text.ParseException;
import java.util.Scanner;

public class EventMessage {

	private String date;
	private String type;
	private String id;
	private String value;
	
	public EventMessage(){
		this.date = "";
		this.type = "";
		this.id = "";
		this.value = "";
	}
	
	public String date(){
		return this.date;
	}
	
	public String type(){
		return this.type;
	}
	
	public String id(){
		return this.id;
	}
	
	public String value(){
		return this.value;
	}
	
	public void parse(String eventMessage) 
	throws ParseException
	{
		Scanner scanner = new Scanner(eventMessage);
	    scanner.useDelimiter("_");
	    if ( scanner.hasNext() ){    	
		      String rawMessage = scanner.next();
		      date = scanner.next();      
		      Scanner scannerMessage = new Scanner(rawMessage);
		      scannerMessage.useDelimiter(";");
		      if ( scannerMessage.hasNext() ){
			      type = scannerMessage.next();
			      id = scannerMessage.next();
			      value = scannerMessage.next();
		      } 
	    }
	    scanner.close();
	}
	
	public String toString(){
		String ret = "date: "+date+"\n";
		ret += "type: "+type+"\n";
		ret += "id: "+id+"\n";
		ret += "value: "+value+"\n";
		return ret;
	}
	
}
