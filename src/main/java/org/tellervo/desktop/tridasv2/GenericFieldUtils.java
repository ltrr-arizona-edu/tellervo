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
package org.tellervo.desktop.tridasv2;

import org.tridas.interfaces.ITridasGeneric;
import org.tridas.schema.TridasGenericField;

/**
 * A class that provides static methods for dealing with generic fields
 * 
 * @author Lucas Madar
 *
 */
public class GenericFieldUtils {
	/**
	 * Finds a field by the name of 'fieldName' in 'parent'
	 * If it doesn't exist, it creates it and adds it to parent
	 * @param parent
	 * @param fieldName
	 * @return a TridasGenericField that's a member of 'parent', never null
	 */
	public static TridasGenericField findOrAddField(ITridasGeneric parent, String fieldName) {
		TridasGenericField field = findField(parent, fieldName);
		
		if(field != null)
			return field;
		
		// create and add the field
		field = new TridasGenericField();
		field.setName(fieldName);
		parent.getGenericFields().add(field);
		
		return field;
	}

	/**
	 * Finds a field by the name of 'fieldName' in 'parent'
	 * @param parent
	 * @param fieldName
	 * @return a TridasGenericField that's a member of 'parent', or null if it doesn't exist
	 */
	public static TridasGenericField findField(ITridasGeneric parent, String fieldName) {
		for(TridasGenericField field : parent.getGenericFields()) {
			if(field.getName().equals(fieldName))
				return field;
		}
		
		return null;
	}

	/**
	 * Removes a field by the name of 'fieldName' in 'parent'
	 * @param parent
	 * @param fieldName
	 * @return true if it was removed, false if it wasn't found
	 */
	public static boolean removeField(ITridasGeneric parent, String fieldName) {
		for(TridasGenericField field : parent.getGenericFields()) {
			if(field.getName().equals(fieldName)) {
				return parent.getGenericFields().remove(field);
			}
		}
		
		return false;
	}

	/**
	 * Sets a tridas generic field to a specific value
	 * Supports, boolean, integer, float, double, and String values (defaults to string on other objects)
	 * If value is null or empty string, removes the field
	 * @param parent
	 * @param fieldName
	 * @param value
	 */
	public static TridasGenericField setField(ITridasGeneric parent, String fieldName, Object value) {
		// if we're setting the value to null or empty string, remove it
		if(value == null || "".equals(value.toString())) {
			removeField(parent, fieldName);
			return null;
		}
		
		TridasGenericField field = findOrAddField(parent, fieldName);
		setFieldValue(field, value);
		
		return field;
	}

	/**
	 * Adds the specified field directly to the parent
	 * @param parent
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static TridasGenericField addField(ITridasGeneric parent, String fieldName, Object value) {
		TridasGenericField field = new TridasGenericField();
		field.setName(fieldName);
		parent.getGenericFields().add(field);

		setFieldValue(field, value);
		
		return field;
	}
	
	/**
	 * Sets the specific field to the given value
	 * 
	 * @param field The TridasGenericField to modify
	 * @param value
	 */
	public static void setFieldValue(TridasGenericField field, Object value) {
		String fieldValue = value.toString();
		String fieldValueType;
		
		if(value instanceof Boolean)
			fieldValueType = "xs:boolean";
		else if(value instanceof Integer)
			fieldValueType = "xs:int";
		else if(value instanceof Float || value instanceof Double)
			fieldValueType = "xs:float";
		else
			fieldValueType = "xs:string";

		field.setType(fieldValueType);
		field.setValue(fieldValue);
	}
}
