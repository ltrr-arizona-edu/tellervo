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
 * Created on Aug 18, 2010, 1:11:20 PM
 */
package org.tellervo.desktop.bulkImport.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.tellervo.desktop.bulkImport.control.BulkImportController;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tridas.schema.TridasElement;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("unchecked")
public class ElementModel extends HashModel implements IBulkImportSectionModel{
	private static final long serialVersionUID = 1L;
	
	public ElementModel(){
		registerProperty(ROWS, PropertyType.FINAL, new MVCArrayList<SingleElementModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel());
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new ElementTableModel(this));
		registerProperty(IMPORTED_LIST, PropertyType.FINAL, new MVCArrayList<TridasElement>());
		registerProperty(WAYPOINT_LIST, PropertyType.FINAL, new MVCArrayList<GPXWaypoint>());
		getColumnModel().poplutePossibleColumns(getModelTableProperties());
	}
	
	public MVCArrayList<SingleElementModel> getRows(){
		return (MVCArrayList<SingleElementModel>) getProperty(ROWS);
	}
	
	public ColumnChooserModel getColumnModel(){
		return (ColumnChooserModel) getProperty(COLUMN_MODEL);
	}
	
	public ElementTableModel getTableModel(){
		return (ElementTableModel) getProperty(TABLE_MODEL);
	}
	
	public MVCArrayList<TridasElement> getImportedList(){
		return (MVCArrayList<TridasElement>) getProperty(IMPORTED_LIST);
	}
	
	public MVCArrayList<GPXWaypoint> getWaypointList(){
		return (MVCArrayList<GPXWaypoint>) getProperty(WAYPOINT_LIST);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkImport.model.IBulkImportSectionModel#getImportedListStrings()
	 */
	@Override
	public String[] getImportedListStrings() {
		MVCArrayList<TridasElement> imported = getImportedList();
		String[] s = new String[imported.size()];
		for(int i=0; i<s.length; i++){
			s[i] = imported.get(i).getTitle();
		}
		return s;
	}
	
	/**
	 * @see org.tellervo.desktop.bulkImport.model.IBulkImportSectionModel#getImportedDynamicComboBoxKey()
	 */
	@Override
	public String getImportedDynamicComboBoxKey() {
		return BulkImportController.SET_DYNAMIC_COMBO_BOX_ELEMENTS;
	}
	
	/**
	 * @see org.tellervo.desktop.bulkImport.model.IBulkImportSectionModel#removeSelected()
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
		MVCArrayList<TridasElement> imported = getImportedList();
		for(int i=0; i< imported.size(); i++){
			TridasElement o = imported.get(i);
			for(IBulkImportSingleRowModel som : removed){
				if(o.getIdentifier().equals(som.getImported())){
					imported.remove(i);
					i--;
				}
			}
		}
	}
	
	/**
	 * @see org.tellervo.desktop.bulkImport.model.IBulkImportSectionModel#createRowInstance()
	 */
	@Override
	public IBulkImportSingleRowModel createRowInstance() {
		return new SingleElementModel();
	}

	/**
	 * @see org.tellervo.desktop.bulkImport.model.IBulkImportSectionModel#getModelTableProperties()
	 */
	@Override
	public String[] getModelTableProperties() {
		return SingleElementModel.TABLE_PROPERTIES;
	}
}
