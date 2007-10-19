package edu.cornell.dendro.corina.dictionary;

import edu.cornell.dendro.corina.SampleEvent;
import edu.cornell.dendro.corina.SampleListener;
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
	
	public boolean loadDocument(Document doc) {
		Element root = doc.getRootElement();
		Element dataElement = root.getChild("data");
		
		if(dataElement == null) {
			System.out.println("No data element in dictionary; ignoring this.");
			return false;
		}
				
		List data = dataElement.getChildren();
		Iterator itr = data.iterator();
		

		// For each child element, call it's loader if it exists (ie, specimenTypeDictionaryLoader)
		while(itr.hasNext()) {
			Element child = (Element) itr.next();
			
			Field targetField;
			Class targetClass;
			Constructor targetConstructor;

			// Check to see if we have a class variable with the dictionary name
			try {
				targetField = this.getClass().getDeclaredField(child.getName());
			} catch(Exception e) {
				System.out.println("No dictionary exists for " + child.getName());
				continue;
			}
			
			/* Obtain a name for the associated class.
			 * For example,
			 *   specimenTypeDictionary -> SpecimenType
			 */
			String className = child.getName();
			if(className.endsWith("Dictionary"))
				className = className.substring(0, className.length() - 10);
			className = Character.toUpperCase(className.charAt(0)) + className.substring(1);

			// Find our class
			try {
				targetClass = Class.forName("edu.cornell.dendro.corina.dictionary." + className);
			} catch (Exception e) {
				System.out.println("Dictionary exists for " + child.getName() + ", but no corresponding class");
				continue;
			}
			
			// Find a constructor for our class
			try {
				targetConstructor = targetClass.getConstructor(new Class[] {String.class, String.class});
			} catch (Exception e) {
				continue;
			}
			
			// Now, finally, loop through our child elements and make new objects for each
			ArrayList l = new ArrayList();
			List elements = child.getChildren();
			try {
				for(int i = 0; i < elements.size(); i++) {
					Element e = (Element) elements.get(i);
					
					// no id? drop it.
					Attribute id = e.getAttribute("id");
					if(id == null)
						continue;
					
					Object obj = targetConstructor.newInstance(new Object[] {id.getValue(), e.getText()});
					l.add(obj);
					
					// If we have an element that can be standard or not, set it
					if(obj instanceof StockDictionaryElement) {
						Attribute standard = e.getAttribute("isStandard");
						if(standard != null) {
							((StockDictionaryElement)obj).setStandard(standard.getBooleanValue());
						}
					}
				}
				
				targetField.set(this, l);
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
	public List getDictionary(String dictionaryType) {
		String localName = Character.toLowerCase(dictionaryType.charAt(0)) + dictionaryType.substring(1) + "Dictionary";
		Field dField;
		
		try {
			dField = this.getClass().getDeclaredField(localName);
			return (List) dField.get(this);
		} catch (Exception e) {
			return null;
		}
	}
	
	private Integer assDictionary;
	
	private volatile List specimenTypeDictionary;
	private volatile List terminalRingDictionary;
	private volatile List specimenQualityDictionary;
	private volatile List pithDictionary;
	private volatile List specimenContinuityDictionary;
	private volatile List treeNoteDictionary;
	private volatile List measurementNoteDictionary;
	private volatile List readingNoteDictionary;
	private volatile List siteNoteDictionary;
}
