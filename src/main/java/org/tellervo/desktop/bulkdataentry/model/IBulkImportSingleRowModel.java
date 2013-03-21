/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
 * Created at Aug 1, 2010, 3:07:26 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import org.tridas.schema.TridasIdentifier;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.model.HashModel.PropertyType;

/**
 * @author daniel
 *
 */
public interface IBulkImportSingleRowModel extends ICloneable{
	public static final String IMPORTED = "Imported";
	
	public TridasIdentifier getImported();
	
	public Object setProperty(String argName, Object argProperty);
	
	public Object getProperty(String argName);
	
	public PropertyType getPropertyType(String argName);
}
