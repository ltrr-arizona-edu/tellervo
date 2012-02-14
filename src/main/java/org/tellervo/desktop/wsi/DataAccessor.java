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
package org.tellervo.desktop.wsi;

import java.io.IOException;

/**
 * Defines a generic way for accessing a data resource
 * given a resource.
 * 
 * INTYPE: The resource we are receiving
 * OUTTYPE: The resource we are sending
 */

public interface DataAccessor<INTYPE, OUTTYPE> {
	
	/**
	 * Specify the object to use to access the object we will receive
	 * 
	 * @param reqObj
	 */
	public void setRequestObject(OUTTYPE reqObj);
	
	/**
	 * Acquire the input object. Must be called after setRequestObject!
	 * 
	 * @return The object we were trying to access
	 * @throws IOException
	 */
	public INTYPE query() throws IOException;
	
	/**
	 * Execute the query, discarding any result.
	 * 
	 * @throws IOException
	 */
	public void execute() throws IOException;
}
