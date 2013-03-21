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
 * Created on Jul 16, 2010, 6:44:22 PM
 */
package org.tellervo.desktop.bulkdataentry.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("unchecked")
public class ObjectModel extends HashModel implements IBulkImportSectionModel{
	private static final long serialVersionUID = 1L;
	
	public ObjectModel(){
		registerProperty(ROWS, PropertyType.FINAL, new MVCArrayList<SingleObjectModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel());
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new ObjectTableModel(this));
		registerProperty(IMPORTED_LIST, PropertyType.FINAL, new MVCArrayList<TridasObjectEx>());
		registerProperty(WAYPOINT_LIST, PropertyType.FINAL, new MVCArrayList<GPXWaypoint>());

	}
	
	public MVCArrayList<SingleObjectModel> getRows(){
		return (MVCArrayList<SingleObjectModel>) getProperty(ROWS);
	}
	
	public ColumnChooserModel getColumnModel(){
		return (ColumnChooserModel) getProperty(COLUMN_MODEL);
	}
	
	public ObjectTableModel getTableModel(){
		return (ObjectTableModel) getProperty(TABLE_MODEL);
	}
	
	public MVCArrayList<TridasObjectEx> getImportedList(){
		return (MVCArrayList<TridasObjectEx>) getProperty(IMPORTED_LIST);
	}
	
	public MVCArrayList<GPXWaypoint> getWaypointList(){
		return (MVCArrayList<GPXWaypoint>) getProperty(WAYPOINT_LIST);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#getImportedListStrings()
	 */
	@Override
	public String[] getImportedListStrings() {
		MVCArrayList<TridasObjectEx> imported = getImportedList();
		String[] s = new String[imported.size()];
		for(int i=0; i<s.length; i++){
			s[i] = imported.get(i).getLabCode();
		}
		return s;
	}
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#getImportedDynamicComboBoxKey()
	 */
	@Override
	public String getImportedDynamicComboBoxKey() {
		return BulkImportController.SET_DYNAMIC_COMBO_BOX_OBJECTS;
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#removeSelected()
	 */
	@Override
	public void removeSelected() {
		ArrayList<IBulkImportSingleRowModel> removed = new ArrayList<IBulkImportSingleRowModel>();
		getTableModel().removeSelected(removed);
		
		Iterator<IBulkImportSingleRowModel> it = removed.iterator();
		
		while(it.hasNext()){
			if(it.next().getImported() == null){
				it.remove();
			}
		}
		if(removed.size() == 0){
			return;
		}
		MVCArrayList<TridasObjectEx> imported = getImportedList();
		for(int i=0; i< imported.size(); i++){
			TridasObjectEx o = imported.get(i);
			for(IBulkImportSingleRowModel som : removed){
				if(o.getIdentifier().equals(som.getImported())){
					imported.remove(i);
					i--;
				}
			}
		}
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#createRowInstance()
	 */
	@Override
	public IBulkImportSingleRowModel createRowInstance() {
		return new SingleObjectModel();
	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#getModelTableProperties()
	 */
	@Override
	public String[] getModelTableProperties() {
		return SingleObjectModel.TABLE_PROPERTIES;
	}
}
