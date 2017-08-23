package org.tellervo.desktop.bulkdataentry.view;

/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/

import java.awt.Component;

import javax.swing.JTable;

import org.tellervo.desktop.bulkdataentry.model.ImportStatus;
import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tellervo.desktop.ui.Builder;


public class ImportStatusRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
			super.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				row,
				column);

			if(value instanceof ImportStatus)
			{
				if(value.equals(ImportStatus.IMPORTED))
				{
					this.setIcon(Builder.getIcon("save-tick.png", 16));	
					this.setText(" Imported");
				}
				if(value.equals(ImportStatus.IMPORTED_WITH_LOCAL_EDITS))
				{
					this.setIcon(Builder.getIcon("save-warn.png", 16));
					this.setText(" Unsaved changes");
				}
				if(value.equals(ImportStatus.LOCAL))
				{
					this.setIcon(Builder.getIcon("filenew.png", 16));
					this.setText(" New record");
				}
			}
	    
			return this;
		}
	
}
