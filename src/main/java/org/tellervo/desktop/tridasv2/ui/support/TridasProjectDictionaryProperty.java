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
package org.tellervo.desktop.tridasv2.ui.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.util.ListUtil;
import org.tridas.annotations.TridasCustomDictionary;
import org.tridas.annotations.TridasCustomDictionarySortType;
import org.tridas.interfaces.HumanName;
import org.tridas.interfaces.ITridasGeneric;
import org.tridas.interfaces.IdAble;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasProject;

import com.l2fprod.common.propertysheet.Property;


/**
 * @author Lucas Madar
 *
 */
public class TridasProjectDictionaryProperty extends TridasEntityProperty {
	private static final long serialVersionUID = 1L;
	

	/**
	 * @param qname
	 * @param lname
	 */
	public TridasProjectDictionaryProperty(String qname, String lname) {
		super(qname, lname);
		this.setType(TridasProject.class, null);
	}
		  	
	/**
	 * Copy constructor
	 * @param property
	 */
	public TridasProjectDictionaryProperty(TridasEntityProperty property) {
		super(property);

	}

	@Override
	public String getCategory() {		
			return categoryPrefix + " General";
	}	
	
	@Override
	public List<?> getDictionary() {
		// get the dictionary based on the available info
		return App.tridasProjects.getProjectList();
	}

	/**
	 * No sub-properties for a dictionary!
	 */
	@Override
	public Property[] getSubProperties() {
		return null;
	}
	
	@Override
	public boolean isDictionaryAttached() {
		return true;
	}
	
	
	@Override
	protected Object getExternalTranslatedValue(Object value) {
		
		if(value instanceof TridasProject)
		{
			return ((TridasProject)value).getTitle();
			
		}
		return value;
		
	}
	
	@Override
	protected Object getInternalTranslatedValue(Object value) {
		
		if(value instanceof TridasProject)
		{
			return value;
			
		}
		return value;
	}
	
	public TridasGenericField getTridasGenericField()
	{
		if(this.getValue()==null) return null;
		
		
		TridasGenericField gf = new TridasGenericField();
		gf.setName("tellervo.object.projectid");
		
		
		gf.setType("xs:string");
		TridasProject project = (TridasProject) this.getValue();
		gf.setValue(project.getIdentifier().getValue().toString());
		
		return gf;
	}
}
