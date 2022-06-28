/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.sample;

/**
 * This interface implements support for adding/setting resource properties
 * 
 * @author Lucas Madar
 */

public interface ResourcePropertySupport {
	/**
	 * Set a server query property for loading
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void setLoadProperty(String propertyName, Object value);

	/**
	 * Set a server query property for saving
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void setSaveProperty(String propertyName, Object value);
}
