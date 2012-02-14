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
package org.tellervo.desktop.wsi.corina;

import org.tellervo.desktop.schema.CorinaRequestType;


/**
 * A resource that wraps a "native" corina type
 * 
 * @author Lucas Madar
 */
public abstract class CorinaAssociatedResource<T> extends CorinaResource {

	/**
	 * @param resourceName
	 * @param queryType
	 */
	public CorinaAssociatedResource(String resourceName,
			CorinaRequestType queryType) {
		super(resourceName, queryType);
	}

	/**
	 * @param resourceName
	 * @param queryType
	 * @param badCredentialsBehavior
	 */
	public CorinaAssociatedResource(String resourceName,
			CorinaRequestType queryType,
			BadCredentialsBehavior badCredentialsBehavior) {
		super(resourceName, queryType, badCredentialsBehavior);
	}
	
	/** The associated result from querying this object */
	private T associatedResult;

	/**
	 * Set the type associated with this result
	 * @param associatedResult
	 */
	protected void setAssociatedResult(T associatedResult) {
		this.associatedResult = associatedResult;
	}
	
	/**
	 * Get the type associated with this result
	 * @return
	 */
	public T getAssociatedResult() {
		if(associatedResult == null)
			throw new IllegalStateException("getAssociatedResult() has a null result");
		
		return associatedResult;
	}
}
