package wfsnrnet.io.db.sql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ParamsDB {
	
	private String host;
	private String userDB;
	private String passDB;
	private String database;
	
	public ParamsDB(String host, String userDB, String passDB, String database){
		this.host=host;
		this.userDB=userDB;
		this.passDB=passDB;
		this.database=database;
	}
	
	public ParamsDB(String userDB, String passDB, String database){
		this.host="";
		this.userDB=userDB;
		this.passDB=passDB;
		this.database=database;
	}
	
	public ParamsDB(String userDB, String passDB){
		this.host="";
		this.userDB=userDB;
		this.passDB=passDB;
		this.database="";
	}
	
	public String host(){
		return host;
	}
	
	public static DateFormat dateFormat(){
		return new SimpleDateFormat("yyyy-M-d HH:mm:ss");
	}
	
	public String userDB(){
		return userDB;
	}
	
	public String passDB(){
		return passDB;
	}
	
	public String database(){
		return database;
	}

}
