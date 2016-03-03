package wfsnrnet.io.db;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wfsnrnet.io.db.sql.DBConnection;

public class SensorEventsTableHandler {
	
	public static void insert(DBConnection connection, String sensorID, int value, Date date) throws SQLException
	{
	    //connection.executeUpdate("INSERT INTO `Events` ( `sensorID`, `value` , `timestamp` , `date`) VALUES ( '"+sensorID+"', "+value+", "+(new Date()).getTime()+", '"+(new SimpleDateFormat("yyyy-M-d HH:mm:ss")).format(date)+"' );");
	    connection.executeUpdate("INSERT INTO `SensorEvents` ( `sensorID`, `value` , `timestamp` , `date`) VALUES ( '"+sensorID+"', "+value+", "+Calendar.getInstance().getTimeInMillis()+", '"+(new SimpleDateFormat("yyyy-M-d HH:mm:ss")).format(date)+"' );");

	}
	
}
