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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasFile;

/**
 * @author Lucas Madar
 *
 */
public class TridasListRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;
	  private final static Logger log = LoggerFactory.getLogger(TridasListRenderer.class);

	/* (non-Javadoc)
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object value) {
		
		log.debug("TridasListRenderer called for "+value);
		
		if(!(value instanceof List)) return null;
		
		List<Object> items=null;
		items  = (List<Object>) value;

		if(items.size()==0) 
		{
			return null;
		}
		if(items.size()==1) 
		{
			Class clazz = items.get(0).getClass();
			if(clazz==TridasFile.class)
			{
				return ((TridasFile)items.get(0)).getHref();
			}
			else if(clazz==ControlledVoc.class)
			{
				return ((ControlledVoc)items.get(0)).getNormal();
			}
			else
			{
				return items.get(0).toString();
			}
		}
		else if (items.size()<=3)
		{
			Class clazz = items.get(0).getClass();
			
			String str = "";
			for(Object item : items)
			{
				if(clazz==TridasFile.class)
				{
					str+= ((TridasFile)item).getHref()+"; ";
				}
				else if(clazz==ControlledVoc.class)
				{
					str+= ((ControlledVoc)item).getNormal()+"; ";
				}
				else
				{
					str+= item.toString()+"; ";
				}
			}
			
			return str;

		}
		else
		{
			Class clazz = items.get(0).getClass();
			String type="";
			if(clazz==TridasFile.class)
			{
				type = "files";
			}
			else if(clazz==ControlledVoc.class)
			{
				type = "vocab items";
			}
			else
			{
				type = "files";
			}

			return "["+items.size()+" "+type+"]";
		}
		
				
	}

}
