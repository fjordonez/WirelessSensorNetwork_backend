package wfsnrnet.io.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import wfsnrnet.data.SensorData;
import wfsnrnet.data.SensorList;
import wfsnrnet.io.db.sql.DBConnection;

public class SensorsTableHandler {
	
	public static void insert(DBConnection connection, SensorData sensorData) 
	throws SQLException
	{
		connection.executeUpdate("INSERT INTO `Sensors` ( `sensorID`, `placeID` , `sensorAlias` , `hardwareID` , `sensorType` , `sensorPlace` , `inverted`) VALUES ( "+(lastSensorID(connection)+1)+", '"+sensorData.placeID()+"', "+sensorData.sensorAlias()+", '"+sensorData.hardwareID()+"', '"+sensorData.sensorType()+"', '"+sensorData.sensorPlace()+"', "+(sensorData.inverted() == true ? 1 : 0 )+" );");
	}
	
	public static int lastSensorID(DBConnection connection) 
	throws SQLException
	{
        int lastSensorID = -1; 
        Statement stmt = null;
        ResultSet rsQuery = null;
        try{
        	stmt = connection.getStatement();
    	    rsQuery = stmt.executeQuery("SELECT MAX(sensorID) FROM Sensors");
  	      if (rsQuery.first())
	    	  lastSensorID = rsQuery.getInt("MAX(sensorID)"); 
    	    
        }
        catch(SQLException e) {
            throw(e);
        }finally{
            if (rsQuery != null) {
                try {
                	rsQuery.close();
                } catch (SQLException sqlEx) { } 
                rsQuery = null;
            }
        	if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } 
                stmt = null;
            }
        }
	    return lastSensorID;
	}
	
	public static void delete(DBConnection connection) throws SQLException{
		connection.executeUpdate("DELETE FROM `Sensors`");
	}
	
	public static void insert(DBConnection connection, SensorList sensorList) 
	throws SQLException
	{
		delete(connection);
		for (int i=0; i<sensorList.size(); i++)
			insert(connection,sensorList.get(i));
	}
}
