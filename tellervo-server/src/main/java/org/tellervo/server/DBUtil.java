package org.tellervo.server;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestStatus;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.DateTime;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasLocationGeometry;


public class DBUtil {

	private static final Logger log = LoggerFactory.getLogger(DBUtil.class);

	
	/**
	 * Replacement for standard rs.getBoolean() function.  This one returns a Boolean rather than
	 * a boolean so null responses can be represented.
	 * 
	 * @param rs
	 * @param fieldname
	 * @return
	 * @throws SQLException
	 */
	public static Boolean getBoolean(ResultSet rs, String fieldname) throws SQLException
	{
		Boolean nValue = rs.getBoolean( fieldname );
        return rs.wasNull() ? null : nValue;	
	}
	
	/**
	 * Returns a TRiDaS PresenceAbsence value from a Postgres tri-state boolean field
	 * 
	 * @param rs
	 * @param fieldname
	 * @return
	 * @throws SQLException
	 */
	public static PresenceAbsence getPresenceAbsence(ResultSet rs, String fieldname) throws SQLException
	{
		Boolean b= getBoolean(rs, fieldname);
		
		if(b==null)
		{
	    	return PresenceAbsence.UNKNOWN;

		}
		else if(b==true)
	    {
			return PresenceAbsence.PRESENT;
	    }
	    else 
	    {
	    	return PresenceAbsence.ABSENT;
	    }
	}
	
	/**
	 * Replacement for standard rs.getInt() function.  This one returns an Integer rather than
	 * an int so null responses can be represented. 
	 * 
	 * @param rs
	 * @param fieldname
	 * @return
	 * @throws SQLException
	 */
	public static Integer getInteger(ResultSet rs, String fieldname) throws SQLException
	{
		Integer nValue = rs.getInt(fieldname);
		return rs.wasNull() ? null : nValue;
	}
	
	/**
	 * Replacement for standard rs.getDouble() function.  This one returns a Double rather than
	 * a double so null responses can be represented. 
	 * 
	 * @param rs
	 * @param fieldname
	 * @return
	 * @throws SQLException
	 */
	public static Double getDouble(ResultSet rs, String fieldname) throws SQLException
	{
		Double nValue = rs.getDouble(fieldname);
		return rs.wasNull() ? null : nValue;
	}
	
	/**
	 * Helper function for getting a double field from Postgres and returning it as a BigDecimal
	 * 
	 * @param rs
	 * @param fieldname
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal getBigDecimal(ResultSet rs, String fieldname) throws SQLException
	{
		Double nValue = rs.getDouble(fieldname);
		if(rs.wasNull())
		{
			return null;
		}
		else
		{
			return BigDecimal.valueOf(nValue);
		}
	}
	
	
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
		Timestamp timestamp = rs.getTimestamp(fieldname);
		Date date = new Date(timestamp.getTime());
		DateTime dt = new DateTime();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		dt.setValue(Main.datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
		
		return dt;
	}
	
	/**
	 * Helper function to create a controlled vocabulary object from it's attributes
	 * 
	 * @param normal
	 * @param normalId
	 * @param normalStd
	 * @return
	 */
	public static ControlledVoc getControlledVoc(String normal, String normalId, String normalStd, String lang)
	{
		ControlledVoc cv = new ControlledVoc();
		cv.setLang(lang);
		cv.setNormal(normal);
		cv.setNormalId(normalId);
		cv.setNormalStd(normalStd);
		return cv;
	}
	
	/**
	 * Convenience method for getting a date field from Postgres and returning a tridas Date
	 * 
	 * @param rs
	 * @param fieldname
	 * @return
	 * @throws SQLException
	 */
	public static org.tridas.schema.Date getDateFromDB(ResultSet rs, String fieldname) throws SQLException
	{
		Timestamp timestamp = rs.getTimestamp(fieldname);
		Date date = new Date(timestamp.getTime());
		org.tridas.schema.Date dt = new org.tridas.schema.Date();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		dt.setValue(Main.datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
		
		return dt;
	}
	
	/**
	 * Turn a standard Tellervo SQL ResultSet into a TridasLocation.  Assumes the ResultSet contains the fields:
	 *  - locationcomment
	 *  - locationprecision
	 *  - locationtype
	 *  - gml
	 *  - locationaddressline1
	 *  - locationaddressline2
	 *  - locationcityortown
	 *  - locationstateprovinceregion
	 *  - locationpostalcode
	 *  - locationcountry
	 * 
	 * @param rs
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public static TridasLocation getTridasLocationFromResultSet(ResultSet rs) throws SQLException
	{
		TridasLocation location = new TridasLocation();
		
		location.setLocationComment(rs.getString("locationcomment"));
		
		if(DBUtil.getInteger(rs, "locationprecision")!=null)
		{
			location.setLocationPrecision(rs.getInt("locationprecision")+"");
		}
		
		try{
			NormalTridasLocationType loctype = NormalTridasLocationType.fromValue(rs.getString("locationtype"));
			location.setLocationType(loctype);
		} catch (IllegalArgumentException e)
		{
			if(rs.getString("locationtype")!=null)
			{
				log.warn("Invalid location type passed by database");
			}
		}
		
		//log.debug("GML return from server: "+rs.getString("gml"));
		
		if(rs.getString("gml")!=null)
		{
			String gml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><locationGeometry xmlns=\"http://www.tridas.org/1.2.2\" "
					+ "xmlns:gml=\"http://www.opengis.net/gml\">" + rs.getString("gml") + "</locationGeometry>";

			//log.debug("GML sent to unmarshal: "+gml);
			StringReader reader = new StringReader(gml);
			try {
	
				XMLStreamReader xmler = Main.xmlInputFactory.createXMLStreamReader(reader);
	
				Unmarshaller u = Main.jaxbContext.createUnmarshaller();
	
				Object root = u.unmarshal(xmler);
	
				if (root instanceof TridasLocationGeometry) {
					TridasLocationGeometry rootElement = (TridasLocationGeometry) root;
					location.setLocationGeometry(rootElement);	
				}
			} catch (JAXBException e2) {
				
				throw new SQLException("GML returned by the server does not validate against the GML schema.");

			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		TridasAddress address = new TridasAddress();	
		address.setAddressLine1(rs.getString("locationaddressline1"));
		address.setAddressLine2(rs.getString("locationaddressline2"));
		address.setCityOrTown(rs.getString("locationcityortown"));
		address.setStateProvinceRegion(rs.getString("locationstateprovinceregion"));
		address.setPostalCode(rs.getString("locationpostalcode"));
		address.setCountry(rs.getString("locationcountry"));
		location.setAddress(address);
		
		return location;
	}
}
