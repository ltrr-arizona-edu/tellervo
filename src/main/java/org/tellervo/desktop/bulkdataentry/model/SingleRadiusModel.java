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
 * Created on Jul 16, 2010, 7:34:07 PM
 */
package org.tellervo.desktop.bulkdataentry.model;

import java.math.BigDecimal;

import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasRadius;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleRadiusModel extends HashModel implements IBulkImportSingleRowModel{
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Radius Code";
	public static final String COMMENTS = "Radius Comments";
	public static final String AZIMUTH = "Radius Azimuth";
	
	public static final String[] PROPERTIES = {
		TITLE, COMMENTS, AZIMUTH, IMPORTED
	};
	
	public SingleRadiusModel(){
		registerProperty(PROPERTIES, PropertyType.READ_WRITE);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, null);
	}
	
	public void setImported(TridasIdentifier argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	/**
	 * @see com.dmurph.mvc.support.AbstractMVCSupport#cloneImpl(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object cloneImpl(String argProperty, Object argO) {
		if(argProperty.equals(IMPORTED)){
			return null;
		}
		return super.cloneImpl(argProperty, argO);
	}
	
	public void populateToTridasRadius(TridasRadius argTridasRadius){
		argTridasRadius.setTitle((String) getProperty(TITLE));
		argTridasRadius.setAzimuth((BigDecimal) getProperty(AZIMUTH));
		argTridasRadius.setComments((String) getProperty(COMMENTS));
		argTridasRadius.setIdentifier((TridasIdentifier) getProperty(IMPORTED));
	}
	
	public void populateFromTridasRadius(TridasRadius argTridasRadius){
		setProperty(TITLE, argTridasRadius.getTitle());
		setProperty(COMMENTS, argTridasRadius.getComments());
		setProperty(AZIMUTH, argTridasRadius.getAzimuth());
		
		
		
	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel#getImported()
	 */
	@Override
	public TridasIdentifier getImported() {
		return (TridasIdentifier) getProperty(IMPORTED);
	}
}
