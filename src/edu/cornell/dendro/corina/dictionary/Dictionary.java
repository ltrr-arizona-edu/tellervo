package edu.cornell.dendro.corina.dictionary;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.schema.WSIRequest.Dictionaries;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceCacher;

public class Dictionary extends CorinaResource {

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
			if((dict = o.getClass().getAnnotation(XmlRootElement.class)) == null ||
			   (type = o.getClass().getAnnotation(XmlType.class)) == null)
				continue;

			// Dictionary has more than one element per entry??
			if(type.propOrder().length != 1) {
				System.err.println("Ignoring dictionary " + dict.name() + 
						" with " + type.propOrder().length + " properties.");
				continue;
			}
			
			String fieldName = type.propOrder()[0];
			String fieldAccessor = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			Method method;
			
			try {
				method = o.getClass().getMethod(fieldAccessor, (Class<?>[]) null);
			} catch (Exception ex) {
				System.err.println("Dictionary " + dict.name() + "/" + fieldName + " is unreadable: " + ex.getMessage());
				continue;
			}

			// dictionary isn't a list??
			if(!method.getReturnType().isAssignableFrom(List.class)) {
				System.err.println("Dictionary " + dict.name() + "/" + fieldName + " is not a list (??)");
				continue;				
			}

			List<?> dictionaryList;
			try {
				// we know this cast will work
				dictionaryList = (List<?>) method.invoke(o, (Object[]) null);
			} catch (Exception ex) {
				System.err.println("Dictionary " + dict.name() + "/" + fieldName + " error: " + ex.getMessage());
				continue;
			}
			
			// empty dictionary?
			if(dictionaryList == null) {
				System.err.println("Dictionary " + dict.name() + "/" + fieldName + " is null");
				continue;				
			}
			
			
			registerDictionary(dict.name(), dictionaryList);
		}
		
		return true;
	}
	
	private static final Map<String, MVCArrayList<?>> dictionaries = new HashMap<String, MVCArrayList<?>>();
	
	@SuppressWarnings({"rawtypes", "unchecked"})
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
		
		System.out.println("Registering dictionary: " + dictionaryName);
	}
	
	/**
	 * Retrieve a dictionary
	 * 
	 * @param dictionaryName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<?> getDictionary(String dictionaryName) {
		MVCArrayList<?> dictionary = dictionaries.get(dictionaryName);

		return (dictionary == null) ? Collections.unmodifiableList(new MVCArrayList()) : Collections.unmodifiableList(dictionary);
	}
	
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
