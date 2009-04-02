package edu.cornell.dendro.corina.dictionary;

import edu.cornell.dendro.corina.webdbi.*;

import org.jdom.*;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.*;

/*
 * The dictionary contains information used to populate various drop-down boxes in Corina.
 * 
 * We want to have the dictionary available immediately upon loading, 
 * but we want to be able to update it in the background.
 * 
 * Here's how we look for our dictionary file:
 * 1. Does dictionaries.xml exist in our user's corina data folder?
 *    Yes? Load it, skip to step 3.
 * 2. Load dictionaries.xml from our corina_resources jar.
 * 3. Start a thread to update the dictionary from our web service.
 */

public class Dictionary extends CachedResource {
	public Dictionary() {
		super("dictionaries");		
	}
	
	// Nothing fancy here, just use the defaults
	@Override
	protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) {
		requestElement.addContent(new CorinaElement("dictionaries"));
		return requestElement;
	}
	
	@Override
	protected void queryFailed(Exception e) {
		System.out.println("Could not load dictionaries:");
		e.printStackTrace();
	}
	
	@Override
	protected boolean processQueryResult(Document doc) {
		Element root = doc.getRootElement();
		Element dataElement = root.getChild("content", CorinaXML.CORINA_NS);
		
		if(dataElement == null) {
			System.out.println("No content element in dictionary; ignoring this.");
			return false;
		}
				
		List<Element> data = dataElement.getChildren();
		Iterator<Element> itr = data.iterator();		

		while(itr.hasNext()) {
			Element child = itr.next();
			DictionaryDescription desc = dictionaryMap.get(child.getName());
			
			if(desc == null) {
				System.out.println("No dictionary exists for " + child.getName());
				continue;
			}
			
			Collection<DictionaryElement> objList = null;
			Map<String, DictionaryElement> idMap = null;
			Map<String, DictionaryElement> nameMap = null;
			
			if(desc.isIdMapped)
				idMap = new HashMap<String, DictionaryElement>();
			if(desc.isValueMapped)
				nameMap = new TreeMap<String, DictionaryElement>();

			// sadly, we're just a lowly list
			if(!(desc.isIdMapped || desc.isValueMapped))
				objList = new ArrayList<DictionaryElement>();
			
			List<Element> contents = child.getChildren();
			Constructor<? extends DictionaryElement> targetConstructor;
			
			// Find a constructor for our class
			try {
				targetConstructor = desc.objClass.getConstructor(new Class[] { Element.class });
				
				for(Element e : contents) {
					DictionaryElement element = targetConstructor.newInstance(new Object[] { e });
					
					if(objList != null)
						objList.add(element);
					
					if(idMap != null)
						idMap.put(element.getId(), element);

					if(nameMap != null)
						nameMap.put(element.getValue(), element);
				}
			} catch (Exception e) {
				continue;
			}
			
		}

		return true;
	}
	
	/**
	 * Used to acquire a dictionary.
	 * 
	 * @param dictionaryType
	 * @return A List of dictionaryType classes
	 */
	public Collection<? extends DictionaryElement> getDictionary(String dictionaryType) {
		DictionaryDescription desc = dictionaryMap.get(dictionaryType);

		if(desc == null)
			return null;
		
		return Collections.unmodifiableCollection(desc.getDictionary());
	}

	/**
	 * Used to acquire a dictionary.
	 * 
	 * @param dictionaryType
	 * @return A List of dictionaryType classes
	 */
	public Map<String, ? extends DictionaryElement> getDictionaryIdMap(String dictionaryType) {
		DictionaryDescription desc = dictionaryMap.get(dictionaryType);

		if(desc == null || !desc.isIdMapped)
			return null;
		
		return Collections.unmodifiableMap(desc.idMap);
	}

	/**
	 * Used to acquire a dictionary.
	 * 
	 * @param dictionaryType
	 * @return A List of dictionaryType classes
	 */
	public Map<String, ? extends DictionaryElement> getDictionaryNameMap(String dictionaryType) {
		DictionaryDescription desc = dictionaryMap.get(dictionaryType);

		if(desc == null || !desc.isValueMapped)
			return null;
		
		return Collections.unmodifiableMap(desc.nameMap);
	}


	private static class DictionaryDescription {
		public String name;
		public Class<? extends DictionaryElement> objClass;
		public boolean isIdMapped;
		public boolean isValueMapped;
		
		public Map<String, DictionaryElement> idMap;
		public Map<String, DictionaryElement> nameMap;
		public Collection<DictionaryElement> elementList;
		
		public Collection<DictionaryElement> getDictionary() {
			if(isValueMapped)
				return nameMap.values();
			else if(isIdMapped)
				return idMap.values();
			else
				return elementList;
		}
		
		public DictionaryDescription(String name, Class<? extends DictionaryElement> objClass, boolean isIdMapped, boolean isValueMapped) {
			this.name = name;
			this.objClass = objClass;
			this.isIdMapped = isIdMapped;
			this.isValueMapped = isValueMapped;
		}
	}
	
	private static HashMap<String, DictionaryDescription> dictionaryMap = new HashMap<String, DictionaryDescription>();
	private static void addDictionary(DictionaryDescription desc) {
		dictionaryMap.put(desc.name, desc);
	}
		
	static {
		addDictionary(new DictionaryDescription("securityUserDictionary", User.class, true, true));
		addDictionary(new DictionaryDescription("taxonDictionary", Taxon.class, true, true));
	}
}
