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

import java.util.ArrayList;

import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIParam;
import org.tellervo.schema.WSISearchParams;

public class SearchParameters extends WSISearchParams {	
	private static final long serialVersionUID = 1L;
	
	private String asText;
	private boolean hasConstraints;
	private boolean hasAllConstraint;
	
	public SearchParameters(SearchReturnObject returnObjectType) {
		setReturnObject(returnObjectType);
		asText = returnObjectType + ": ";
		
		hasConstraints = hasAllConstraint = false;
	}
		
	/**
	 * Set to true to return all objects of type 'returnObject'
	 * Useful to search for all sites, for instance
	 * @param all
	 */
	public void addSearchForAll() {
		if(hasConstraints || hasAllConstraint)
			throw new IllegalArgumentException("Cannot have both search constraints and all constraint/multiple all constraints");
		this.setAll(new All());
		asText += "[all]";
		hasAllConstraint = true;
	}
	
	public void addSearchConstraint(SearchParameterName name, SearchOperator comparison, String value) {
		if(hasAllConstraint)
			throw new IllegalArgumentException("Cannot have all constraint AND search constraints");

		WSIParam param = new WSIParam();
		
		param.setName(name);
		param.setOperator(comparison);
		param.setValue(value);
		
		// add this parameter
		getParams().add(param);
	
		
		if(hasConstraints)
			asText += ",";
		asText += name.value() + comparison.toString() + value;

		hasConstraints = true;
	}
			
	public String toString() {
		return asText;
	}
}
