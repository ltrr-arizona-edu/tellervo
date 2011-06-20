/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

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
