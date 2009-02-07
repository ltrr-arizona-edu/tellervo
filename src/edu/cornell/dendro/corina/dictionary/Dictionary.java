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
		requestElement.addContent(new Element("dictionaries"));
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
		Element dataElement = root.getChild("content");
		
		if(dataElement == null) {
			System.out.println("No content element in dictionary; ignoring this.");
			return false;
		}
				
		List<?> data = dataElement.getChildren();
		Iterator<?> itr = data.iterator();
		

		// For each child element, call it's loader if it exists (ie, specimenTypeDictionaryLoader)
		while(itr.hasNext()) {
			Element child = (Element) itr.next();
			
			Field targetField;
			Field targetFieldMap; // do we also have a map?
			
			Class<? extends BasicDictionaryElement> targetClass;
			Constructor<? extends BasicDictionaryElement> targetConstructor;

			// Check to see if we have a class variable with the dictionary name
			try {
				targetField = this.getClass().getDeclaredField(child.getName());
			} catch(Exception e) {
				System.out.println("No dictionary exists for " + child.getName());
				continue;
			}

			// Check to see if we also have a map
			try {
				targetFieldMap = this.getClass().getDeclaredField(child.getName() + "Map");
			} catch(Exception e) {
				targetFieldMap = null;
			}

			/* Obtain a name for the associated class.
			 * For example,
			 *   specimenTypeDictionary -> SpecimenType
			 */
			String className = child.getName();
			if(className.endsWith("Dictionary"))
				className = className.substring(0, className.length() - 10);
			className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
			
			/*
			 * Kludge:
			 * Regions isn't a dictionary
			 * Users have plurality issues
			 */
			if(className.equalsIgnoreCase("Regions"))
				className = "SiteRegion";
			else if(className.equalsIgnoreCase("Users"))
				className = "User";

			// Find our class
			try {
				Class<?> baseClass = Class.forName("edu.cornell.dendro.corina.dictionary." + className);
				targetClass = baseClass.asSubclass(BasicDictionaryElement.class);
			} catch (Exception e) {
				System.out.println("Dictionary exists for " + className + ", but no corresponding class");
				continue;
			}
			
			// Find a constructor for our class
			try {
				targetConstructor = targetClass.getConstructor(new Class[] {String.class, String.class});
			} catch (Exception e) {
				continue;
			}
			
			// Now, finally, loop through our child elements and make new objects for each
			ArrayList<BasicDictionaryElement> newList = new ArrayList<BasicDictionaryElement>();
			List<?> elements = child.getChildren();
			try {
				for(int i = 0; i < elements.size(); i++) {
					Element e = (Element) elements.get(i);
					
					// no id? 
					// well, I guess a null ID is ok for stuff that doesn't have an ID!
					Attribute id = e.getAttribute("id");
					String idvalue = (id != null) ? id.getValue() : null;
					
					BasicDictionaryElement obj = targetConstructor.newInstance(new Object[] {idvalue, e.getText()});
					newList.add(obj);
					
					// If we have an element that can be standard or not, set it
					if(obj instanceof StockDictionaryElement) {
						Attribute standard = e.getAttribute("isStandard");
						if(standard != null) {
							((StockDictionaryElement)obj).setStandard(standard.getBooleanValue());
						}
					}
					// user is also a special case. ugh.
					else if(obj instanceof User) {
						User user = (User) obj;
						
						user.setUsername(e.getAttributeValue("username"));
						user.setFullname(e.getAttributeValue("firstName"), e.getAttributeValue("lastName"));
					}
				}
				
				targetField.set(this, newList);
				
				// do we have a map?
				if(targetFieldMap != null) {
					Map<String, BasicDictionaryElement> newMap = new HashMap<String, BasicDictionaryElement>();
					
					// add everything to the map
					// this doesn't eat that much memory because we're just reusing references
					for(BasicDictionaryElement obj : newList) {
						newMap.put(obj.getInternalRepresentation(), obj);
					}
					
					targetFieldMap.set(this, newMap);					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
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
	@SuppressWarnings("unchecked")
	public List<? extends BasicDictionaryElement> getDictionary(String dictionaryType) {
		String localName = Character.toLowerCase(dictionaryType.charAt(0)) + dictionaryType.substring(1) + "Dictionary";
		Field dField;
		List<BasicDictionaryElement> retList;
		
		/*
		 * Kludge: 
		 * regions isn't a regionsDictionary.
		 * users isn't a usersDictionary
		 */
		if(dictionaryType.equalsIgnoreCase("Regions"))
			localName = "regions";
		else if(dictionaryType.equalsIgnoreCase("Users"))
			localName = "users";
		
		try {
			dField = this.getClass().getDeclaredField(localName);
			retList = (List) dField.get(this);
			return retList;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Used to acquire a dictionary.
	 * 
	 * @param dictionaryType
	 * @return A List of dictionaryType classes
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ? extends BasicDictionaryElement> getDictionaryMap(String dictionaryType) {
		String localName = Character.toLowerCase(dictionaryType.charAt(0)) + dictionaryType.substring(1) + "DictionaryMap";
		Field dField;
		Map<String, BasicDictionaryElement> retMap;
		
		/*
		 * Kludge: 
		 * regions isn't a regionsDictionary.
		 */
		if(dictionaryType.equalsIgnoreCase("Regions"))
			localName = "regionsMap";
		else if(dictionaryType.equalsIgnoreCase("Users"))
			localName = "usersMap";
		
		try {
			dField = this.getClass().getDeclaredField(localName);
			retMap = (Map) dField.get(this);
			return retMap;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unused")
	private volatile List<SpecimenType> specimenTypeDictionary;

	@SuppressWarnings("unused")
	private volatile List<TerminalRing> terminalRingDictionary;

	@SuppressWarnings("unused")
	private volatile List<SpecimenQuality> specimenQualityDictionary;

	@SuppressWarnings("unused")
	private volatile List<Pith> pithDictionary;

	@SuppressWarnings("unused")
	private volatile List<SpecimenContinuity> specimenContinuityDictionary;

	@SuppressWarnings("unused")
	private volatile List<TreeNote> treeNoteDictionary;

	@SuppressWarnings("unused")
	private volatile List<MeasurementNote> measurementNoteDictionary;

	@SuppressWarnings("unused")
	private volatile List<ReadingNote> readingNoteDictionary;

	@SuppressWarnings("unused")
	private volatile List<SiteNote> siteNoteDictionary;

	@SuppressWarnings("unused")
	private volatile List<SiteRegion> regions;

	@SuppressWarnings("unused")
	private volatile List<Taxon> taxonDictionary;

	@SuppressWarnings("unused")
	private volatile Map<String, Taxon> taxonDictionaryMap;

	@SuppressWarnings("unused")
	private volatile List<User> users;

}