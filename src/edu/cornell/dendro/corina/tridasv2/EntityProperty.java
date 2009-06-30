/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tridas.annotations.TridasCustomDictionary;
import org.tridas.annotations.TridasCustomDictionaryType;
import org.tridas.interfaces.ITridasGeneric;
import org.tridas.interfaces.IdAble;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasGenericField;

import com.l2fprod.common.beans.BeanUtils;
import com.l2fprod.common.propertysheet.AbstractProperty;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSIRequest.Dictionaries;
import edu.cornell.dendro.corina.tridasv2.doc.Documentation;
import edu.cornell.dendro.corina.util.ListUtil;


public class EntityProperty extends AbstractProperty {
	/**
	 * Construct a tridas entity property
	 */
	public EntityProperty(String qname, String lname) {
		childProperties = new ArrayList<EntityProperty>();
		nChildProperties = 0;
		isList = required = readOnly = dictionaryAttached = false;
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

	public boolean isDictionaryAttached() {
		return dictionaryAttached;
	}
	
	public List<?> getDictionary() {
		if(!dictionaryAttached)
			throw new IllegalArgumentException("Can't get dictionary when it doesn't exist!");
		
		// get the dictionary based on the available info
		return Dictionary.getDictionary(dictionary.dictionary() + "Dictionary");
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
	public Object getValue() {
		return super.getValue();
	}

	private Object transformDictionaryValue(Object value) {
		if(!dictionaryAttached)
			return value;

		switch(dictionary.type()) {
		/*
		 * Controlled Vocabulary:
		 * Simple enough; if given a controlled voc fall through
		 * If given anything else, try to find it in the dictionary values
		 * If that doesn't work, create a "dummy" controlled vocabulary
		 */
		case CORINA_CONTROLLEDVOC: {
			// if it's already a controlled vocabulary, nothing to see here...
			if(value instanceof ControlledVoc)
				break;
			
			// clear the value, sure
			if(value == null)
				break;
			
			// get the associated dictionary
			List<ControlledVoc> vocabulary = ListUtil.subListOfType(getDictionary(), ControlledVoc.class);

			for(ControlledVoc voc : vocabulary) {
				if(voc.getNormal().equalsIgnoreCase(value.toString())) {
					System.err.println("Mapping given " + value.getClass().getName() + " value of " + value.toString() + 
							" to " + voc.toString());
					return voc;
				}
			}

			System.err.println("Mapping given " + value.getClass().getName() + " value of " + value.toString() + 
					" to new controlled vocabulary. Fix this!");
			
			ControlledVoc voc = new ControlledVoc();
			voc.setValue(value.toString());
			voc.setNormalStd("corina/autogenerated");
			
			return voc;
		}

		/*
		 * Generic ID
		 * This means this dictionary element ties with a generic field!
		 */
		case CORINA_GENERICID: {
			if(parentProperty == null)
				throw new IllegalStateException("GenericID dictionary attached, but no parent property for " + this.qname);
			
			Object parentValue = parentProperty.getValue();
			if(!(parentValue instanceof ITridasGeneric))
				throw new IllegalStateException("GenericID dictionary attached, but parent isn't generic for " + this.qname);
			
			// ok, now we have our parent value that has the associated generic fields
			ITridasGeneric generic = (ITridasGeneric) parentValue;
			
			// IdAble? Nice, just set the generic fields then
			if(value instanceof IdAble) {
				GenericFieldUtils.setField(generic, dictionary.identifierField(), 
						((IdAble)value).getId());
				return value;
			}

			// if it's null or empty string, remove the field (setField does this)
			if(value == null || "".equals(value.toString())) {
				GenericFieldUtils.setField(generic, dictionary.identifierField(), null);
				return null;
			}
			
			// ok, no match then. Let's look by id!
			TridasGenericField idField = GenericFieldUtils.findField(generic, dictionary.identifierField());
			if(idField != null && idField.isSetValue() && idField.getValue().length() > 0) {
				String givenId = idField.getValue();
				
				List<IdAble> vocabulary = ListUtil.subListOfType(getDictionary(), IdAble.class);
				for(IdAble idable : vocabulary) {
					if(givenId.equals(idable.getId())) {
						System.out.println("Found " + value + " by id lookup as " + idable.toString());
						return idable;
					}
				}
			}

			// ok then. Give up!
			return value;
		}
		
		default:
			// do nothing special, drop through and ignore
			break;
		}
		
		return value;
	}
	
	@Override
	protected void initializeValue(Object value) {
		super.initializeValue(transformDictionaryValue(value));
	}
	
	@Override
	public void setValue(Object value) {
		// If the old value was null and the new value is empty string, 
		// keep the old value.
		if(getValue() == null && value != null) {
			if(value.toString() == "")
				value = null;
		}
		
		super.setValue(transformDictionaryValue(value));
		
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
		else {
			for (EntityProperty child : childProperties)
				child.setValue(null);
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
	private Class<?> clazz;
	public boolean isList;
	public boolean required;
	
	/** Is this a root node? Ignore it as a parent */
	private boolean isRootNode;
	
	/** Is a dictionary attached to this? (if yes, ignore children) */
	private boolean dictionaryAttached;
	private TridasCustomDictionary dictionary;
	
	/** Is it read only? */
	private boolean readOnly;
	
	private int nChildProperties;
	private List<EntityProperty> childProperties;
	private EntityProperty parentProperty;
	
	private String categoryPrefix = "Entity";
	
	public void setCategoryPrefix(String categoryPrefix) {
		this.categoryPrefix = categoryPrefix.substring(0, 1).toUpperCase() + categoryPrefix.substring(1);
	}
	
	public String getCategory() {		
		if (nChildProperties > 0 && !dictionaryAttached && !required) {
			return categoryPrefix + " Other";
		}
		else
			return categoryPrefix + " General";
	}

	public String getDisplayName() {
		return getNiceName();
	}

	public String getName() {
		return lname;
	}

	public String getShortDescription() {
		String docs = Documentation.getDocumentation(qname);
		
		if(docs != null)
			return docs;
		
		return "<i>No documentation is available for this entity</i>";
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public Class<?> getType() {
		return clazz;
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
		
		TridasCustomDictionary dict;
		if((dict = field.getAnnotation(TridasCustomDictionary.class)) != null) {
			this.dictionary = dict;
			this.dictionaryAttached = true;
		}
	}

	public boolean isEditable() {
		if(isList || readOnly)
			return false;
		return true;
	}

	public Property getParentProperty() {
		return parentProperty;
	}
		  
	public Property[] getSubProperties() {
		if(dictionaryAttached)
			return null;
		
		return childProperties.toArray(new Property[childProperties.size()]);	
	}
}