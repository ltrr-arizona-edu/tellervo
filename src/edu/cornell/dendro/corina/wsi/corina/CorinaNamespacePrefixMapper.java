package edu.cornell.dendro.corina.wsi.corina;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Prefix our generated marshalled namespaces with something we're familiar with
 * 
 * @author Lucas Madar
 */

public class CorinaNamespacePrefixMapper extends NamespacePrefixMapper {

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		String[][] schemas = CorinaSchema.getSchemas();
		
		for(String[] schema : schemas) {
			if(schema[0].equals(namespaceUri))
				return schema[2];
		}
		
		return suggestion;
	}

}
