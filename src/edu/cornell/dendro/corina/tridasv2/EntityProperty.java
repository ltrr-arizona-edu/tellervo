/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.l2fprod.common.beans.BeanUtils;
import com.l2fprod.common.propertysheet.AbstractProperty;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;


public class EntityProperty extends AbstractProperty {
	/**
	 * Construct a tridas entity property
	 */
	public EntityProperty(String qname, String lname) {
		childProperties = new ArrayList<EntityProperty>();
		nChildProperties = 0;
		isList = required = false;
		parentProperty = null;
		
		// it's a root node if there's no name!
		isRootNode = (qname == null && lname == null);
		
		this.qname = qname;
		this.lname = lname;
	}
	
	public void addChildProperty(EntityProperty property) {
		childProperties.add(property);
		nChildProperties++;
		
		if(!isRootNode)
			property.parentProperty = this;
	}
	
	public List<EntityProperty> getChildProperties() {
		return childProperties;
	}
	
	/**
	 * Reads the value of this Property from the given object. It uses
	 * reflection and looks for a method starting with "is" or "get" followed by
	 * the capitalized Property name.
	 */
	public void readFromObject(Object object) {
		try {
			Method method = BeanUtils.getReadMethod(object.getClass(),
					getName());
			if (method != null) {
				Object value = method.invoke(object, (Object[]) null);
				initializeValue(value); // avoid updating parent or firing
										// property change
				if (value != null) {
					for (EntityProperty child : childProperties)
						child.readFromObject(value);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
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
				method.invoke(object, new Object[] { getValue() });
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	private static void ensureParentValuesExist(EntityProperty ep) {
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
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		
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
			for (EntityProperty child : childProperties)
				child.readFromObject(value);
		}
	}
	
	@Override
	public int hashCode() {
		return qname.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		
		if (other == null || !getClass().equals(other.getClass())) {
			return false;
		}
		
		EntityProperty ep = (EntityProperty) other;
		
		return qname.equals(ep.qname);
	}
	

	/** The fully qualified name of this property (e.g. object.title) */
	public final String qname;
	
	/** The local name of this property (e.g. title) */
	public final String lname;
	
	/** The class of this property */
	public Class<?> clazz;
	public boolean isList;
	public boolean required;
	
	/** Is this a root node? Ignore it as a parent */
	private boolean isRootNode;
	
	private int nChildProperties;
	private List<EntityProperty> childProperties;
	private EntityProperty parentProperty;
	
	public String getCategory() {
		if (nChildProperties > 0 && !required) {
			return "Other";
		}
		else
			return "General";
	}

	public String getDisplayName() {
		return getNiceName();
	}

	public String getName() {
		return lname;
	}

	public String getShortDescription() {
		return getNiceName() + ": " + qname;
	}

	public Class getType() {
		return clazz;
	}

	public boolean isEditable() {
		if(isList)
			return false;
		return true;
	}

	public Property getParentProperty() {
		return parentProperty;
	}
		  
	public Property[] getSubProperties() {
		return childProperties.toArray(new Property[childProperties.size()]);
	}
}