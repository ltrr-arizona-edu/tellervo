package org.tellervo.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

import org.tridas.schema.DateTime;

public class DBUtil {

	/**
	 * Convenience method for getting a timestamp field from Postgres and returning it as a DateTime
	 * 
	 * @param rs
	 * @param fieldname
	 * @return
	 * @throws SQLException
	 */
	public static DateTime getDateTimeFromDB(ResultSet rs, String fieldname) throws SQLException
	{
		Timestamp timestamp = rs.getTimestamp("createdtimestamp");
		Date date = new Date(timestamp.getTime());
		DateTime dt = new DateTime();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		dt.setValue(Main.datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
		
		return dt;
	}
}
