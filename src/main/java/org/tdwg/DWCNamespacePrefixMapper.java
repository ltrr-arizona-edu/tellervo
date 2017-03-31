/**
 * Copyright 2010 Peter Brewer and Daniel Murphy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tdwg;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Provides user friendly namespace prefixes to in our XML
 * 
 * @author peterbrewer
 */
public class DWCNamespacePrefixMapper extends NamespacePrefixMapper {
	
	private final static String SCHEMAS_USED[][] = {
			// Order is important!
			// namespace, filename, prefix
		    // tridas prefix is important and should be left alone
			{"http://purl.org/dc/terms/", "dc.xsd", "dc"},
			{"http://rs.tdwg.org/dwc/terms/", "tdwg_dwcterms.xsd", "dwc"},
			{"http://rs.tdwg.org/dwc/xsd/simpledarwincore/", "tdwg_dwc_simple.xsd", ""},};  
	
	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		String[][] schemas = DWCNamespacePrefixMapper.SCHEMAS_USED;
		
		for (String[] schema : schemas) {
			if (schema[0].equals(namespaceUri)) {
				return schema[2];
			}
		}
		
		return suggestion;
	}
	
	/**
	 * Get the namespace URI for the TRiDaS schema elements
	 * 
	 * @return
	 */
	public static String getTridasNamespaceURI()
	{
		
		String[][] schemas = DWCNamespacePrefixMapper.SCHEMAS_USED;
		
		for (String[] schema : schemas) {
			if (schema[2].equals("tridas")) {
				return schema[0];
			}
		}
		
		return null;
	}
	
}
