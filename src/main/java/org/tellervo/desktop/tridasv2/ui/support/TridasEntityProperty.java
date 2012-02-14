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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.tridasv2.doc.Documentation;

import com.l2fprod.common.beans.BeanUtils;
import com.l2fprod.common.propertysheet.AbstractProperty;
import com.l2fprod.common.propertysheet.Property;



public class TridasEntityProperty extends AbstractProperty {
	private static final long serialVersionUID = 1L;

	/** The fully qualified name of this property (e.g. object.title) */
	public final String qname;
	
	/** The local name of this property (e.g. title) */
	public final String lname;
	
	/** The class of this property */
	protected Class<?> clazz;
	
	public boolean isList;
	
	public boolean required;

    /** Is this a root node? Ignore it as a parent */
	protected boolean isRootNode;
	  	
	/** Is it read only? */
	protected boolean readOnly;
	
	protected int nChildProperties;
	
	protected List<TridasEntityProperty> childProperties;
		
	protected TridasEntityProperty parentProperty;

	/** The object being acted upon from the root of this tree
	 * Only valid when parentProperty == null
	 */
	protected Object rootObject;
	
	protected String categoryPrefix = "Entity";
	
	/**
	 * Construct a tridas entity property
	 */
	public TridasEntityProperty(String qname, String lname) {
		childProperties = new ArrayList<TridasEntityProperty>();
		nChildProperties = 0;
		isList = required = readOnly = false;
		parentProperty = null;
		
		// it's a root node if there's no name!
		isRootNode = (qname == null && lname == null);
		
		this.qname = qname;
		this.lname = lname;
	}
	
	public TridasEntityProperty(TridasEntityProperty property) {
		this.qname = property.qname;
		this.lname = property.lname;
		
		this.clazz = property.clazz;
		this.isList = property.isList;		
		this.required = property.required;
		this.isRootNode = property.isRootNode;
		this.readOnly = property.readOnly;
		this.nChildProperties = property.nChildProperties;
		this.childProperties = property.childProperties; // shallow copy!
		this.parentProperty = property.parentProperty; // dangerous!
		this.rootObject = property.rootObject;
		this.categoryPrefix = property.categoryPrefix;
	}

	public void addChildProperty(TridasEntityProperty property) {
		childProperties.add(property);
		nChildProperties++;
		
		if(!isRootNode)
			property.parentProperty = this;
		
		if(property.isRequired()==true)
		{
			this.required = true;
		}
	}

	/**
	 * Replace a property by identity (==).
	 *  
	 * @param oldProp the property to replace
	 * @param newProp the property to replace it with
	 */
	public void replaceChildProperty(TridasEntityProperty oldProp, TridasEntityProperty newProp) {
		int nProps = childProperties.size();
		for(int i = 0; i < nProps; i++) {
			if(childProperties.get(i) == oldProp)
				childProperties.set(i, newProp);
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		
		if (other == null || !(other instanceof TridasEntityProperty)) {
			return false;
		}
		
		TridasEntityProperty ep = (TridasEntityProperty) other;
		
		return qname.equals(ep.qname);
	}
	
	public String getCategory() {		
		if (nChildProperties > 0 && !required) {
			return categoryPrefix + " Other";
		}
		else
			return categoryPrefix + " General";
	}
	
	public List<TridasEntityProperty> getChildProperties() {
		return childProperties;
	}
	
	public List<?> getDictionary() {
		throw new IllegalArgumentException("No dictionary for " + qname);
	}
	
	public String getDisplayName() {
		return getNiceName();
	}
	
	public String getName() {
		return lname;
	}
	public String getNiceName() {
		StringBuffer sb = new StringBuffer();
		String[] nameTokens = qname.split("\\.");
		String camelCaseName = nameTokens[nameTokens.length - 1];
		int len = camelCaseName.length();
		
		boolean didCapitalize = false;
		for(int i = 0; i < len; i++) {
			char c = camelCaseName.charAt(i);
			
			// skip attribute
			if(i == 0 && c == '@')
				continue;
			
			if(!didCapitalize && Character.isLowerCase(c)) {
				sb.append(Character.toUpperCase(c));
				didCapitalize = true;
				continue;
			}
			
			if(Character.isUpperCase(c)) {
				sb.append(' ');
				sb.append(Character.toLowerCase(c));	
				continue;
			}
			
			sb.append(c);
		}
		
		return sb.toString();
	}
	
	@Override
	public Property getParentProperty() {
		return parentProperty;
	}
	
	public String getShortDescription() {
		String docs = Documentation.getDocumentation(qname);
		
		if(docs != null)
			return docs;
		
		return "<i>No documentation is available for this entity</i>";
	}
	
	@Override
	public Property[] getSubProperties() {		
		return childProperties.toArray(new Property[childProperties.size()]);	
	}
	
	/**
	 * Get the type of this property
	 */
	public Class<?> getType() {
		return clazz;
	}

	/**
	 * Does this class represent an enum property?
	 * If true, getEnumType() can be called
	 * @return true if this class represents an enum property
	 */
	public boolean representsEnumType() {
		return clazz.isEnum();
	}
	
	/**
	 * @return The enum class
	 * @throws IllegalArgumentException If this doesn't represent an enum property
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends Enum<?>> getEnumType() throws IllegalArgumentException {
		if(clazz.isEnum())
			return (Class<? extends Enum<?>>) clazz;
		
		throw new IllegalArgumentException("Calling getEnumType() on a non-enum!");
	}
	
	@Override
	public int hashCode() {
		return qname.hashCode();
	}
	
	public boolean isDictionaryAttached() {
		return false;
	}

	/**
	 * @return true if this property is required to be set
	 */
	public boolean isRequired() {
		return required;
	}
	
	public boolean isEditable() {
		if(isList || readOnly)
			return false;
		return true;
	}
	
	/**
	 * Reads the value of this Property from the given object. It uses
	 * reflection and looks for a method starting with "is" or "get" followed by
	 * the capitalized Property name.
	 */
	public void readFromObject(Object object) {
		// memorize our 'root' object so we can make non-obvious changes to it
		if(parentProperty == null)
			rootObject = object;

		//
		// we have to be smart - if our value is null, set null on ourselves
		// and all of our children. Otherwise, old stuff can accumulate!
		//
		
		Object value = null;
		
		if(object != null) {
			try {
				Method method = BeanUtils.getReadMethod(object.getClass(),
						getName());
				if (method != null) {
					value = method.invoke(object, (Object[]) null);
					initializeValue(value); // avoid updating parent or firing
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} 
		else {
			// empty object, set our value to null...
			initializeValue(null);
		}
		
		// have all child properties read from *us* 
		for (TridasEntityProperty child : childProperties) {
			child.readFromObject(value);
		}
	}
	
	public void setCategoryPrefix(String categoryPrefix) {
		this.categoryPrefix = categoryPrefix.substring(0, 1).toUpperCase() + categoryPrefix.substring(1);
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Set the type of the class
	 * 
	 * Checks to see if we have dictionary mappings and
	 * other annotations.
	 * 
	 * @param clazz
	 * @param field
	 */
	public void setType(Class<?> clazz, Field field) {
		this.clazz = clazz;		
	}

	@Override
	public void setValue(Object value) {
		// If the old value was null and the new value is empty string, 
		// keep the old value.
		if(getValue() == null && value != null) {
			if(value.toString() == "")
				value = null;
		}
		
		super.setValue(getInternalTranslatedValue(value));
		
		if (parentProperty != null) {
			
			// if we have a legitimate value, make sure our structure exists
			if(!isNullOrEmpty(value))
				ensureParentValuesExist(parentProperty);

			Object parentValue = parentProperty.getValue();
			
			if (parentValue != null) {
				writeToObject(parentValue);
				parentProperty.setValue(parentValue);
			}
		}
		
		// reload any children
		if (value != null) {
			for (TridasEntityProperty child : childProperties)
				child.readFromObject(value);
		}
		else {
			for (TridasEntityProperty child : childProperties)
				child.setValue(null);
		}
	}

	/**
	 * Writes the value of the Property to the given object. It uses reflection
	 * and looks for a method starting with "set" followed by the capitalized
	 * Property name and with one parameter with the same type as the Property.
	 */
	public void writeToObject(Object object) {
		try {
			Method method = BeanUtils.getWriteMethod(object.getClass(),
					getName(), getType());
			if (method != null) {
				method.invoke(object, new Object[] { getExternalTranslatedValue(getValue()) });
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void ensureParentValuesExist(TridasEntityProperty ep) {
		// make sure that all values down the tree exist! 
		if(ep.parentProperty != null)
			ensureParentValuesExist(ep.parentProperty);
		
		if(ep.getValue() == null) {
			System.out.println("Creating " + ep.qname);

			try {
				Constructor<?> cons = ep.clazz.getConstructor((Class<?>[]) null);
				Object newObj = cons.newInstance((Object[]) null);
				ep.initializeValue(newObj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private boolean isNullOrEmpty(Object o) {
		if(o == null)
			return true;
		
		if(o instanceof String && ((String)o).length() == 0)
			return true;
		
		return false;
	}

	/**
	 * Translate the value for external representation
	 * (how it's supposed to be represented in the object)
	 * @return
	 */
	protected Object getExternalTranslatedValue(Object value) {
		return value;
	}
	
	/**
	 * Translate the value for internal representation
	 * (This is what we display in our property table)
	 * @param value
	 * @return
	 */
	protected Object getInternalTranslatedValue(Object value) {
		return value;
	}

	@Override
	protected void initializeValue(Object value) {
		super.initializeValue(getInternalTranslatedValue(value));
	}
}
