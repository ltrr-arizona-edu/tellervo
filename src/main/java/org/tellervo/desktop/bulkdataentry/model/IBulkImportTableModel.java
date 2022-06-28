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
 * Created at Aug 24, 2010, 3:09:24 PM
 */
package org.tellervo.desktop.bulkdataentry.model;

import java.util.ArrayList;

import javax.swing.table.TableModel;

/**
 * @author Daniel
 *
 */
public interface IBulkImportTableModel extends TableModel {

	public void selectAll();
	
	public void selectNone();
	
	public int getSelectedCount();
	
	public void getSelected(ArrayList<IBulkImportSingleRowModel> argModels);
}
