package edu.cornell.dendro.corina.tridasv2.support;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.tridas.schema.Certainty;
import org.tridas.schema.Date;
import org.tridas.schema.DateTime;

import edu.cornell.dendro.corina.gui.Bug;

public class XMLDateUtils {
	private XMLDateUtils() {
	}

	/**
	 * Create a DateTime with the given certainty
	 * 
	 * @param date
	 * @param certainty
	 * @return a DateTime populated with the java date
	 */
	public static DateTime toDateTime(java.util.Date date, Certainty certainty) {		
		XMLGregorianCalendar xmlcal = toXMLGregorianCalendar(date);
		
		DateTime dateTime = new DateTime();
		dateTime.setValue(xmlcal);
		dateTime.setCertainty(certainty);
		
		return dateTime;
	}
	
	/**
	 * Create a DateTime with the given certainty
	 * 
	 * @param date
	 * @param certainty
	 * @return a Date populated with the java date
	 */
	public static Date toDate(java.util.Date date, Certainty certainty) {		
		XMLGregorianCalendar xmlcal = toXMLGregorianCalendar(date);
		
		// remove the time and timezone
		xmlcal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		xmlcal.setTime(DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
		
		Date tdate = new Date();
		tdate.setValue(xmlcal);
		tdate.setCertainty(certainty);
		
		return tdate;
	}

	
	public static XMLGregorianCalendar toXMLGregorianCalendar(java.util.Date date) {
		// create a new Gregorian calendar
		GregorianCalendar calendar = new GregorianCalendar();
		// set it to this date
		calendar.setTime(date);
		
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			new Bug(e);
			return null;
		}
	}
}
