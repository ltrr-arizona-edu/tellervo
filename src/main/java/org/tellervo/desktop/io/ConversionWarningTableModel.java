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
package org.tellervo.desktop.io;

import javax.swing.table.AbstractTableModel;

import org.tridas.io.exceptions.ConversionWarning;

@SuppressWarnings("serial")
public class ConversionWarningTableModel extends AbstractTableModel {

	private ConversionWarning[] warnings;
	
	
	String[] columnNames = {"Type", "Field", "Message"};

	
	public ConversionWarningTableModel()
	{
	}
	
	public ConversionWarningTableModel(ConversionWarning[] warnings)
	{
		this.warnings = warnings;
	}
	

	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(warnings!=null)
		{
			return warnings.length;
		}
		return 0;
	}

	@Override
	public Object getValueAt(int row, int col) {
		ConversionWarning warning = warnings[row];
		
		switch(col)
		{
		case 0:
			return warning.getWarningType();
		case 1:
			return warning.getField();
		case 2:
			return warning.getMessage();
		}
	
		return null;
		
	}
	
	@Override
	public Class<?> getColumnClass(int col)
	{
		return String.class;
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col].toString();
	}

	@Override
	public boolean isCellEditable(int row, int col)
    { 
		return false; 
	}

	
}
