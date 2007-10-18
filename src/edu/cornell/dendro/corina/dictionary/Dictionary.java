package edu.cornell.dendro.corina.dictionary;

import edu.cornell.dendro.corina.SampleEvent;
import edu.cornell.dendro.corina.SampleListener;
import edu.cornell.dendro.corina.webdbi.*;

import org.jdom.*;

import java.lang.reflect.Method;
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
		
		specimenTypeList = new ArrayList();
	}
	
	public void loadDocument(Document doc) {
		Element root = doc.getRootElement();
		Element dataElement = root.getChild("data");
		
		if(dataElement == null) {
			System.out.println("No data element in dictionary; ignoring this.");
			return;
		}
		
		// Let our parent cache this for next time
		super.loadDocument(doc);
		
		List data = dataElement.getChildren();
		Iterator itr = data.iterator();
		

		// For each child element, call it's loader if it exists (ie, specimenTypeDictionaryLoader)
		while(itr.hasNext()) {
			Element child = (Element) itr.next();
			
			try {
				Method m = Dictionary.class.getMethod(child.getName() + "Loader" , new Class[] { Element.class });
				m.invoke(this, new Object[] { child });
			} catch (Exception e) {
				System.out.println("No Dictionary Loader for " + child.getName());
			}
		}
	}
	
	public void specimenTypeDictionaryLoader(Element root) {
		List elements = root.getChildren();
		ArrayList l = new ArrayList();
		
		for(int i = 0; i < elements.size(); i++) {
			Element e = (Element) elements.get(i);
			
			l.add(new SpecimenType(e.getAttributeValue("id"), e.getText()));
		}
		
		synchronized(specimenTypeList) {
			specimenTypeList = l;
		}
	}
	
	
	private List specimenTypeList;
}
