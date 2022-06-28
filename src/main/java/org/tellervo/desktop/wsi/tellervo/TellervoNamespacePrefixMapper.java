/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.wsi.tellervo;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Prefix our generated marshalled namespaces with something we're familiar with
 * 
 * @author Lucas Madar
 */

public class TellervoNamespacePrefixMapper extends NamespacePrefixMapper {

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		String[][] schemas = TellervoSchema.getSchemas();
		
		for(String[] schema : schemas) {
			if(schema[0].equals(namespaceUri))
				return schema[2];
		}
		
		return suggestion;
	}

}
