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
 * Created at Jul 24, 2010, 3:33:39 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author daniel
 *
 */
public class ColumnChooserModel extends MVCArrayList<String> {	
	private static final long serialVersionUID = 1L;
	
	private final MVCArrayList<String> possibleColumns;
	
	public ColumnChooserModel(){
		possibleColumns = new MVCArrayList<String>();
//		for(String s: argPossibleProperties){
//			possibleColumns.add(s);
//		}
	}
	
	public void populatePossibleColumns(String[] argTableColumns){
		possibleColumns.clear();
		for(String s : argTableColumns){
			if(!s.equals(IBulkImportSingleRowModel.IMPORTED)){
				possibleColumns.add(s);
			}
		}
	}
	
	/**
	 * Note that this property doesn't get affected by any mvc operations on the {@link ColumnChooserModel} 
	 * object.
	 * @return
	 */
	public MVCArrayList<String> getPossibleColumns(){
		return possibleColumns;
	}
}
