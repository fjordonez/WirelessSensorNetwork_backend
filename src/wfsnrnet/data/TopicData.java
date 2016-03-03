package wfsnrnet.data;

public class TopicData {
	
	private String name; 
	private String alias;

	public TopicData(){
		this.name = "";
		this.alias = "";
	}
	
	public TopicData(String name, String alias){
		this.name = name;
		this.alias = alias;
	}
	
	public String name(){
		 return this.name;
	}
	
	public void setName(String name){
		 this.name = name;
	}
	
	public String alias(){
		 return this.alias;
	}
	
	public void setAlias(String alias){
		 this.alias = alias;
	}
	
	public String toString(){
		return "  Topic with alias "+alias+" and name '"+name+"'\n";
	}
}
