package wfsnrnet.io.db;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wfsnrnet.data.SensorData;
import wfsnrnet.io.db.sql.DBConnection;

public class SensorDataTableHandler {
	
	public static void insert(DBConnection connection, SensorData sensorData, int value, Date date) throws SQLException
	{
	    connection.executeUpdate("INSERT INTO `SensorData` ( `sensorID` , `sensorType` , `sensorPlace` , `value` , `timestamp` , `date`) VALUES ( '"+sensorData.hardwareID()+"', '"+sensorData.sensorType()+"', '"+sensorData.sensorPlace()+"', "+value+", "+Calendar.getInstance().getTimeInMillis()+", '"+(new SimpleDateFormat("yyyy-M-d HH:mm:ss")).format(date)+"' );");
	}
	
}
