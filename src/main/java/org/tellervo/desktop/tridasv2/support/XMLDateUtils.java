/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.support;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.tellervo.desktop.gui.Bug;
import org.tridas.schema.Certainty;
import org.tridas.schema.Date;
import org.tridas.schema.DateTime;


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
