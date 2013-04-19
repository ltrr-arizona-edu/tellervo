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
 * Created on Jul 17, 2010, 1:35:12 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Daniel Murphy
 *
 */
public class ObjectTableModel extends AbstractBulkImportTableModel {
	private static final long serialVersionUID = 2L;

	public ObjectTableModel(ObjectModel argModel){
		super(argModel);
	}
	
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel#getColumnClass(java.lang.String)
	 */
	@Override
	public Class<?> getColumnClass(String argColumn) {
		// for combo box stuff
		if(argColumn.equals(SingleObjectModel.TYPE)){
			return WSIObjectTypeDictionary.class;
		}else if(argColumn.equals(SingleObjectModel.IMPORTED)){
			return Boolean.class;
		}else if(argColumn.equals(SingleObjectModel.LATITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleObjectModel.LONGITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleObjectModel.LOCATION_PRECISION)){
			return Double.class;
		}else if(argColumn.equals(SingleObjectModel.WAYPOINT)){
			return GPXWaypoint.class;
		}else if(argColumn.equals(SingleObjectModel.PARENT_OBJECT)){
			return TridasObject.class;
		}
		return null;
	}

	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel#setValueAt(java.lang.Object, java.lang.String, org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel, int)
	 */
	@Override
	public void setValueAt(Object argAValue, String argColumn, IBulkImportSingleRowModel argModel, int argRowIndex) {
		// TODO tie this into a command, only commands can modify the model!
		
		// If it's a waypoint set the lat and long
		if(argColumn.equals(SingleObjectModel.WAYPOINT))
		{		
			GPXWaypoint wp = (GPXWaypoint) argAValue;
			argModel.setProperty(SingleObjectModel.LATITUDE, wp.getLatitude());
			argModel.setProperty(SingleObjectModel.LONGITUDE, wp.getLongitude());
		}
		// If it's lat/long data, remove the waypoint
		if(argColumn.equals(SingleObjectModel.LATITUDE) || argColumn.equals(SingleObjectModel.LONGITUDE)){
			if(argAValue!= null && argAValue instanceof Double)	argModel.setProperty(SingleObjectModel.WAYPOINT, null);
		}
						
		argModel.setProperty(argColumn, argAValue);
	}
}
