package edu.cornell.dendro.corina.tridasv2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cornell.dendro.corina.dictionary.Dictionary;

public class DictionaryMappings {
	private static Map<String, String> dictionaries = new HashMap<String, String>();

	public static List<?> getDictionaryList(String qname) {
		String key = dictionaries.get(qname);
		
		if(key == null)
			return null;
		
		return Dictionary.getDictionary(key);
	}
	
	public static boolean hasDictionaryMapping(String qname) {
		return dictionaries.containsKey(qname);
	}
	
	static {
		dictionaries.put("object.type", "objectTypeDictionary");
		dictionaries.put("element.type", "elementTypeDictionary");
		dictionaries.put("sample.type", "sampleTypeDictionary");
		dictionaries.put("element.taxon", "taxonDictionary");
		dictionaries.put("object.coverage.coverageTemporal", "coverageTemporalDictionary");
		dictionaries.put("object.coverage.coverageTemporalFoundation", "coverageTemporalFoundationDictionary");
		dictionaries.put("element.shape", "elementShapeDictionary");
	}
}
