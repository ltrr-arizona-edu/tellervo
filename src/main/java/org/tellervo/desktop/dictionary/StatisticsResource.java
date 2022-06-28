/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.dictionary;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRequest.Statistics;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.schema.WSIRequest.Dictionaries;
import org.tellervo.schema.WSIStatistic;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceCacher;

import com.dmurph.mvc.model.MVCArrayList;


public class StatisticsResource extends TellervoResource {

	public static final String DICTIONARY_REGISTERED = "DICTIONARY_DICTIONARY_REGISTERED";
	private final static Logger log = LoggerFactory.getLogger(StatisticsResource.class);

	private Map statsMap = new HashMap();
	
	public StatisticsResource() {
		super("statistics", TellervoRequestType.READ);
		
		// load my cache and unload on a successful remote load
		new TellervoResourceCacher(this, true).load();
		
		this.query();
	}
	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setStatistics(new Statistics());
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		List<Object> content = object.getContent().getSqlsAndProjectsAndObjects();

		for(Object o : content) {
			XmlRootElement dict;
			XmlType type;
			
			// ignore things without an xml root element
			if((dict = o.getClass().getAnnotation(XmlRootElement.class)) == null )
			{
				log.error("Ignoring statistics entry with no xml root element");
				continue;
			}
			
			if((type = o.getClass().getAnnotation(XmlType.class)) == null)
			{
				log.error("Ignoring statistics entry with no xml type");
				continue;

			}
							
			String fieldName = type.propOrder()[0];
			String fieldAccessor = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			Method method;
			
			try {
				method = o.getClass().getMethod(fieldAccessor, (Class<?>[]) null);
			} catch (Exception ex) {
				log.error("Statistics " + dict.name() + "/" + fieldName + " is unreadable: " + ex.getMessage());
				continue;
			}

			// Statistics isn't a list??
			if(!method.getReturnType().isAssignableFrom(List.class)) {
				log.error("Statistics " + dict.name() + "/" + fieldName + " is not a list (??)");
				continue;				
			}

			List<?> statsList;
			try {
				// we know this cast will work
				statsList = (List<?>) method.invoke(o, (Object[]) null);
			} catch (Exception ex) {
				log.error("Statistics " + dict.name() + "/" + fieldName + " error: " + ex.getMessage());
				continue;
			}
			
			// empty dictionary?
			if(statsList == null) {
				log.error("Statistics " + dict.name() + "/" + fieldName + " is null");
				continue;				
			}
			
			for(Object s : statsList)
			{
				WSIStatistic stat = (WSIStatistic) s;
				
				statsMap.put(stat.getKey(), stat.getValue());
			}
				
		}
		
		return true;
	}

	
	/**
	 * Retrieve a map of statistics
	 * 
	 */
	public Map getStats() {
		return statsMap;
	}
		
	public Object getValue(String key)
	{
		return statsMap.get(key);
	}
	
	public Integer getValueAsInteger(String key)
	{
		Object val = statsMap.get(key);
		
		try{
			Integer num = Integer.valueOf((String)val);
			return num;
		} catch (Exception e)
		{
			log.debug("Unable to parse statistic value to integer");
		}
		
		return null;
	}
	
	public Double getValueAsDouble(String key)
	{
		Object val = statsMap.get(key);
		
		try{
			Double num = Double.valueOf((String)val);
			return num;
		} catch (Exception e)
		{
			log.debug("Unable to parse statistic value to double");
		}
		
		return null;
	}
	
}
