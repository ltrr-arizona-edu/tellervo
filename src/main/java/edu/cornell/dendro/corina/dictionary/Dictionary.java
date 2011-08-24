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
package edu.cornell.dendro.corina.dictionary;

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

import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.schema.WSIRequest.Dictionaries;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceCacher;

public class Dictionary extends CorinaResource {

	public static final String DICTIONARY_REGISTERED = "DICTIONARY_DICTIONARY_REGISTERED";
	private final static Logger log = LoggerFactory.getLogger(Dictionary.class);

	public Dictionary() {
		super("dictionaries", CorinaRequestType.READ);
		
		// load my cache and unload on a successful remote load
		new CorinaResourceCacher(this, true).load();
	}
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setDictionaries(new Dictionaries());
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		List<Object> content = object.getContent().getSqlsAndObjectsAndElements();

		for(Object o : content) {
			XmlRootElement dict;
			XmlType type;
			
			// ignore things without an xml root element
			if((dict = o.getClass().getAnnotation(XmlRootElement.class)) == null )
			{
				log.error("Ignoring dictionary entry with no xml root element");
				continue;
			}
			
			if((type = o.getClass().getAnnotation(XmlType.class)) == null)
			{
				log.error("Ignoring dictionary entry with no xml type");
				continue;

			}
				

			// Dictionary has more than one element per entry??
			if(type.propOrder().length != 1) {
				log.error("Ignoring dictionary "+ dict.name(), 
						" with "+ type.propOrder().length + " properties.");
				continue;
			}
			
			String fieldName = type.propOrder()[0];
			String fieldAccessor = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			Method method;
			
			try {
				method = o.getClass().getMethod(fieldAccessor, (Class<?>[]) null);
			} catch (Exception ex) {
				log.error("Dictionary " + dict.name() + "/" + fieldName + " is unreadable: " + ex.getMessage());
				continue;
			}

			// dictionary isn't a list??
			if(!method.getReturnType().isAssignableFrom(List.class)) {
				log.error("Dictionary " + dict.name() + "/" + fieldName + " is not a list (??)");
				continue;				
			}

			List<?> dictionaryList;
			try {
				// we know this cast will work
				dictionaryList = (List<?>) method.invoke(o, (Object[]) null);
			} catch (Exception ex) {
				log.error("Dictionary " + dict.name() + "/" + fieldName + " error: " + ex.getMessage());
				continue;
			}
			
			// empty dictionary?
			if(dictionaryList == null) {
				log.error("Dictionary " + dict.name() + "/" + fieldName + " is null");
				continue;				
			}
			
			log.debug("Registering dictionary "+dict.name()+" with "+dictionaryList.size()+ " entries");
			
			
			registerDictionary(dict.name(), dictionaryList);
		}
		
		return true;
	}
	
	private static final Map<String, MVCArrayList<?>> dictionaries = new HashMap<String, MVCArrayList<?>>();
	
	@SuppressWarnings("unchecked")
	public static void registerDictionary(String dictionaryName, List<?> dictionary) {
		MVCArrayList list;
		if(dictionaries.containsKey(dictionaryName)){
			list = dictionaries.get(dictionaryName);
		}else{
			list = new MVCArrayList();
			dictionaries.put(dictionaryName, list);
		}
		list.clear();
		list.addAll(dictionary);
		
		log.info("Registering dictionary: " + dictionaryName);
		DictionaryRegisteredEvent event = new DictionaryRegisteredEvent(dictionaryName, list);
		event.dispatch();
	}
	
	/**
	 * Retrieve a dictionary
	 * 
	 * @param dictionaryName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<?> getDictionary(String dictionaryName) {
		MVCArrayList<?> dictionary = dictionaries.get(dictionaryName);

		return (dictionary == null) ? Collections.unmodifiableList(new MVCArrayList()) : Collections.unmodifiableList(dictionary);
	}
	
	@SuppressWarnings("unchecked")
	public static MVCArrayList getMutableDictionary(String dictionaryName){
		MVCArrayList<?> dictionary = dictionaries.get(dictionaryName);

		return (dictionary == null) ? new MVCArrayList() : dictionary;
	}
	
	
	
	/**
	 * Retrieve a dictionary
	 * 
	 * @param dictionaryName
	 * @return
	 */
	public static ArrayList<?> getDictionaryAsArrayList(String dictionaryName) {
		ArrayList<?> dictionary = (ArrayList<?>) dictionaries.get(dictionaryName);

		return dictionary;
	}
	
}
