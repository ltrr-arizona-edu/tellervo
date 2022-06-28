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
/**
 * 
 */
package org.tellervo.desktop.tridasv2.ui;

import java.util.List;

import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tridas.schema.TridasFile;

/**
 * @author Lucas Madar
 *
 */
public class TridasFileArrayRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object value) {
		
		if(value instanceof TridasFile) 
		{
			return ((TridasFile) value).getHref();
		}
		
		if(!(value instanceof List)) return null;
		
		List<TridasFile> files=null;

		files  = (List<TridasFile>) value;
		
		
		
		
		String ret = "";
		
		if(files.size()==0) 
		{
			return null;
		}
		else if(files.size()==1) 
		{
			ret = files.get(0).getHref();
		}
		else
		{
			ret = "["+files.size()+" files] ";
		}
		
				
		return ret;
	}

}
