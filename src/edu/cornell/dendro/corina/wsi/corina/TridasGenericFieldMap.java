package edu.cornell.dendro.corina.wsi.corina;

import java.util.HashMap;
import java.util.List;

import org.tridas.schema.TridasGenericField;

/**
 * Class to simplify lookup of generic fields by key
 */

public class TridasGenericFieldMap extends HashMap<String, TridasGenericField>{
	private static final long serialVersionUID = 5965496844604631100L;

	public TridasGenericFieldMap(List<TridasGenericField> genericFields) {
		super(genericFields.size());
		
		for(TridasGenericField g : genericFields)
			put(g.getName(), g);
	}
}
