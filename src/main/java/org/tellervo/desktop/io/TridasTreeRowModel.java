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

import org.netbeans.swing.outline.RowModel;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;

public class TridasTreeRowModel implements RowModel {

    @SuppressWarnings("unchecked")
	@Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                assert false;
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Action" : "Status";
    }

    @Override
    public Object getValueFor(Object node, int column) {
        ITridas f = (ITridas) node;
        switch (column) {
            case 0:
            	if(f instanceof TridasProject)
            	{
            		return "Ignore";
            	}
            	else if (f instanceof TridasObject)
            	{
            		return "Stored in database";
            	}
                return "Pending";
            case 1:
                return "OK";
            default:
                assert false;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    @Override
    public void setValueFor(Object node, int column, Object value) {
        //do nothing for now
    }
    
    
    
}
