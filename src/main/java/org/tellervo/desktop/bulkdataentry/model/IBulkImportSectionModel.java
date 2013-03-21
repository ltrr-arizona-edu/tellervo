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
 * Created at Aug 1, 2010, 3:04:07 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import org.tridas.interfaces.ITridas;

import com.dmurph.mvc.IModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author daniel
 *
 */
public interface IBulkImportSectionModel extends IModel{
	public static final String COLUMN_MODEL = "columnModel";
	public static final String ROWS = "rows";
	public static final String TABLE_MODEL = "tableModel";
	public static final String IMPORTED_LIST = "importedList";
	public static final String WAYPOINT_LIST = "waypointList";
	
	
	/**
	 * goes to the table model, which handles the selected rows
	 */
	public void removeSelected();
	
	/**
	 * Gets an instance of a row object for this model;
	 * @return
	 */
	public IBulkImportSingleRowModel createRowInstance();
		
	/**
	 * Gets the table properties
	 * @return
	 */
	public String[] getModelTableProperties();
	
	/**
	 * Gets the column model
	 * @return
	 */
	public ColumnChooserModel getColumnModel();
	
	/**
	 * Gets the table model.
	 * @return
	 */
	public IBulkImportTableModel getTableModel();
	
	/**
	 * Gets the list of imported objects
	 * @return
	 */
	public MVCArrayList<? extends ITridas> getImportedList();
	
	/**
	 * Gets the string representations of the imported list
	 * @return
	 */
	public String[] getImportedListStrings();
	
	/**
	 * Convenience method so commands can just use the interface
	 * @return
	 */
	public String getImportedDynamicComboBoxKey();
	
	/**
	 * Gets the rows
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public MVCArrayList getRows(); // can't figure out how to get generics to work here, either there's a command error or model errors
}
