package wfsnrnet.io.db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnection {
	
	/*private Connection con = null;
    private ResultSet result; 

	public Connection getConnection(){
		return con;
	}
	
	public Connection getConnection(String host, String userDB, String passDB, String database) throws Exception {
		try {
			// Load the MySQL driver
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + host + ":3306/" + database;
			con = DriverManager.getConnection(url, userDB, passDB);	
			return con;
		} catch (java.sql.SQLException e) {
			throw (e);
		}
	} 
	
	public Connection getConnection(ParamsDB paramsDB) throws Exception
	{
		String host = paramsDB.host();
		if (host.equals(""))
			host = "localhost";
		String database = paramsDB.database();
		if (database.equals(""))
			database = "mysql";
		
		try {
				return getConnection(host,paramsDB.userDB(),paramsDB.passDB(),database);
			} catch (java.sql.SQLException e) {
				throw (e);
			}
	} 	

	public void executeUpdate(String query){
        //Create Statement object
        Statement stmt;
        //It's an UPDATE, INSERT, or DELETE statement
        //Use the"executeUpdaye" function and return a null result
        try{
            //Execute Query
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        }
        catch(Exception x) {
            x.printStackTrace();
        }
	}
	
	public ResultSet executeQuery(String query){
        //Create Statement object
        Statement stmt;
        //Use the "executeQuery" function because we have to retrieve data
        //Return the data as a ResultSet
        try{
            //Execute Query
            stmt = con.createStatement();
            result = stmt.executeQuery(query);
        }
        catch(Exception x) {
            x.printStackTrace();
        }

        return result;
	}
	
    public boolean closeConnection(){
        try{
        	con.close();
            return true;
        }
        catch (Exception x) {
             x.printStackTrace();
             return false;
        }
    }*/
	
	private Connection con = null;
	
	public DBConnection(ParamsDB paramsDB) throws Exception
	{
		String host = paramsDB.host();
		if (host.equals(""))
			host = "localhost";
		String database = paramsDB.database();
		if (database.equals(""))
			database = "mysql";
		
		try {
			// Load the MySQL driver
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + host + ":3306/" + database;
			con = DriverManager.getConnection(url, paramsDB.userDB(), paramsDB.passDB());	
		} catch (java.sql.SQLException e) {
			throw (e);
		} catch (java.lang.ClassNotFoundException e) {
			throw (e);
		}
	}
	
	public DBConnection(String host, String userDB, String passDB, String database) throws SQLException, ClassNotFoundException {
		try {
			// Load the MySQL driver
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + host + ":3306/" + database;
			con = DriverManager.getConnection(url, userDB, passDB);	
		} catch (java.sql.SQLException e) {
			throw (e);
		} catch (java.lang.ClassNotFoundException e) {
			throw (e);
		}
	} 
	
	
	public void executeUpdate(String query) throws SQLException{
       Statement stmt = null;
        try{
        	stmt = con.createStatement();
            stmt.executeUpdate(query);
        }
        catch(java.sql.SQLException e) {
        	throw (e);
        }finally{
          	if (stmt != null) {
                try {
                    stmt.close();
                } catch (java.sql.SQLException e) { throw (e); } 
                stmt = null;
            }
        }
	}
	
	public Statement getStatement() throws SQLException{
		try{
			return con.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
		}catch(java.sql.SQLException e) {
        	throw (e);
        }
	}
	
    public boolean closeConnection(){
        try{
            if (con != null) {
                try {
                	con.close();
                } catch (Exception e) { } // ignore

                con = null;
            }
            return true;
        }
        catch (Exception x) {
             x.printStackTrace();
             return false;
        }
    }

}
