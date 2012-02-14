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
package org.tellervo.desktop.remarks;

import javax.swing.Icon;

import org.tridas.schema.TridasValue;

public interface Remark {
	/** Get a display name for this remark */
	public String getDisplayName();
	
	/** Apply a remark to the given value */
	public void applyRemark(TridasValue value);

	/** Remove a remark from the given tridas value */
	public void removeRemark(TridasValue value);
	
	/** Disable a remark on the given tridas value */
	public void overrideRemark(TridasValue value);
	
	/**
	 * @param value
	 * @return true if the remark is set on this value
	 */
	public boolean isRemarkSet(TridasValue value);
	
	/**
	 * @param value
	 * @return true if the remark was inherited
	 */
	public boolean isRemarkInherited(TridasValue value);
	
	/**
	 * @return an associated Icon, or null
	 */
	public Icon getIcon();
}
