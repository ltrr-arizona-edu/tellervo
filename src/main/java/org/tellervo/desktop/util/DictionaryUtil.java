package org.tellervo.desktop.util;

import java.util.List;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tridas.schema.ControlledVoc;

public class DictionaryUtil {

	/**
	 * Get a Tridas ControlledVoc for the specified term name from the specified dictionary
	 *  
	 * @param name
	 * @param dictionaryName
	 * @return
	 */
	public static ControlledVoc getControlledVocForName(String name, String dictionaryName) {
		
		if(name==null || dictionaryName==null) return null;
		
		List<?> dictionary = Dictionary.getDictionary(dictionaryName);
		List<ControlledVoc> vocab = ListUtil.subListOfType(dictionary, ControlledVoc.class);
		
		for(ControlledVoc voc : vocab) {
			if(name.equalsIgnoreCase(voc.getNormal()))
				return voc;
		}
		
		for(ControlledVoc voc : vocab) {
			if(name.replace("_", " ").equalsIgnoreCase(voc.getNormal().replace("_", " ")))
				return voc;
		}
		
		return null;
	}
	
	public static ControlledVoc getControlledVocForID(String id, String dictionaryName) {
		List<?> dictionary = Dictionary.getDictionary(dictionaryName);
		List<ControlledVoc> vocab = ListUtil.subListOfType(dictionary, ControlledVoc.class);
		
		for(ControlledVoc voc : vocab) {
			if(id.equalsIgnoreCase(voc.getNormalId()))
				return voc;
		}
		
		return null;
	}
}
