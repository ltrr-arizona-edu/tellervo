package edu.cornell.dendro.corina.wsi.corina;

import java.util.HashMap;
import java.util.List;

import org.tridas.schema.TridasGenericField;

/**
 * Class to simplify lookup of generic fields by key
 * Values are String, Integer, or Boolean.
 */

public class TridasGenericFieldMap extends HashMap<String, Object>{
	private static final long serialVersionUID = 5965496844604631100L;

	public TridasGenericFieldMap(List<TridasGenericField> genericFields) {
		super(genericFields.size());
		
		for(TridasGenericField g : genericFields) {
			if("integer".equals(g.getType())) {
				try {
					put(g.getName(), Integer.valueOf(g.getValue()));
				} catch (NumberFormatException nfe) {
					// just ignore an invalid number!
				}
			}
			else if("boolean".equals(g.getType())) {
				put(g.getName(), Boolean.valueOf(g.getValue()));
			}
			else 
				put(g.getName(), g.getValue());
		}
	}

	public Integer getInteger(String key, Integer defaultValue) {
		Integer ret = getInteger(key);
		
		return (ret != null) ? ret : defaultValue;
	}
	
	public Integer getInteger(String key) {
		Object o = get(key);

		if(o == null)
			return null;

		if(Integer.class.isInstance(o))
			return (Integer) o;
		else try {
			return Integer.parseInt(o.toString());
		} catch (NumberFormatException nfe) {
			return null;
		}
	}
	
	public Boolean getBoolean(String key) {
		Object o = get(key);

		if(o == null)
			return null;
		
		if(Boolean.class.isInstance(o))
			return (Boolean) o;
		else 
			return Boolean.valueOf(o.toString());
	}
	
	public String getString(String key) {
		Object o = get(key);
		
		if(o == null)
			return null;

		return o.toString();
	}

}
