/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.tridas.annotations.TridasCustomDictionary;
import org.tridas.annotations.TridasCustomDictionarySortType;
import org.tridas.interfaces.HumanName;
import org.tridas.interfaces.ITridasGeneric;
import org.tridas.interfaces.IdAble;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasGenericField;

import com.l2fprod.common.propertysheet.Property;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.SecurityUser;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.util.ListUtil;

/**
 * @author Lucas Madar
 *
 */
public class TridasDictionaryEntityProperty extends TridasEntityProperty {
	/**
	 * Sort ControlledVocs by their "normal" or their "value"
	 * @author Lucas Madar
	 *
	 */
	public static class ControlledVocComparator implements Comparator<ControlledVoc> {
		public int compare(ControlledVoc o1, ControlledVoc o2) {
			String s1 = o1.isSetNormal() ? o1.getNormal() : o1.getValue();
			String s2 = o2.isSetNormal() ? o2.getNormal() : o2.getValue();
			
			if(s1 == null)
				return 1;
			if(s2 == null)
				return -1;
			
			return s1.compareToIgnoreCase(s2);
		}
	}

	/**
	 * Sort names by last, first
	 * @author Lucas Madar
	 *
	 */
	public static class HumanNameComparator implements Comparator<HumanName> {
		public int compare(HumanName o1, HumanName o2) {
			boolean b1 = o1.isSetLastName();
			boolean b2 = o2.isSetLastName();
			int compare;
			
			if(b1 && !b2)
				return -1;
			else if(!b1 && b2)
				return 1;
			else if(!b1 && !b2)
				compare = 0;
			else
				compare = o1.getLastName().compareToIgnoreCase(o2.getLastName());
			
			if(compare != 0)
				return compare;
			
			b1 = o1.isSetFirstName();
			b2 = o2.isSetFirstName();

			if(b1 && !b2)
				return -1;
			else if(!b1 && b2)
				return 1;
			else if(!b1 && !b2)
				return 0;			// exactly the same!!
			else
				return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
		}
	}
	
	/** The annotation on the field I represent */
	private TridasCustomDictionary dictionary;

	/**
	 * @param qname
	 * @param lname
	 */
	public TridasDictionaryEntityProperty(String qname, String lname, TridasCustomDictionary dictionary) {
		super(qname, lname);
		
		this.dictionary = dictionary;
	}
		  	
	/**
	 * Copy constructor
	 * @param property
	 */
	public TridasDictionaryEntityProperty(TridasEntityProperty property, TridasCustomDictionary dictionary) {
		super(property);
		
		this.dictionary = dictionary;
	}

	@Override
	public String getCategory() {		
			return categoryPrefix + " General";
	}	
	
	@Override
	public List<?> getDictionary() {
		// get the dictionary based on the available info
		return sortedDictionary(Dictionary.getDictionary(dictionary.dictionary() + "Dictionary"));
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
	
	/**
	 * Sort a dictionary given a sort type
	 * If the sort type doesn't apply to the dictionary, returns an empty list!
	 * @param vocabulary
	 * @return
	 */
	private List<?> sortedDictionary(List<?> vocabulary) {
		// quick and easy: no sorting, no problem!
		if(dictionary.sortType() == TridasCustomDictionarySortType.NONE)
			return vocabulary;
		
		switch(dictionary.sortType()) {
		case LASTNAME_FIRSTNAME: {
			List<HumanName> list = new ArrayList<HumanName>(ListUtil.subListOfType(vocabulary, HumanName.class)); 
			Collections.sort(list, new HumanNameComparator());
			return list;
		}
		
		case NORMAL_OR_VALUE: {
			List<ControlledVoc> list = new ArrayList<ControlledVoc>(ListUtil.subListOfType(vocabulary, ControlledVoc.class)); 
			Collections.sort(list, new ControlledVocComparator());
			return list;
		}
		
		default:
			return vocabulary;
		}
	}
	
	@Override
	protected Object getExternalTranslatedValue(Object value) {
		switch(dictionary.type()) {
		case CORINA_GENERICID:
			if(value instanceof IdAble) {
				// security user: First Last (it's really just for show)
				if(value instanceof SecurityUser) {
					SecurityUser user = (SecurityUser) value;
					
					return user.getFirstName() + " " + user.getLastName();
				}
				else
					throw new IllegalStateException("Don't know how to translate for " + value.getClass().getName());
			}
			else
				return value;

		// default case: just return the value
		default:
			return value;
		}
	}
	
	@Override
	protected Object getInternalTranslatedValue(Object value) {
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
			Object ancestor;

			if(parentProperty == null) {
				// we're top level, so get our 'root' object
				if(rootObject == null)
					throw new IllegalStateException("GenericID dictionary attached to tree root, but no root object for " + this.qname);
				ancestor = rootObject;
			}
			else {
				// not top level, so get our parent's value
				ancestor = parentProperty.getValue();
				
				if(ancestor == null)
					throw new IllegalStateException("GenericID dictionary attached to object, but no parent value for " + this.qname);
			}
			
			if(!(ancestor instanceof ITridasGeneric))
				throw new IllegalStateException("GenericID dictionary attached, but parent isn't generic for " + this.qname);
			
			// ok, now we have our parent value that has the associated generic fields
			ITridasGeneric generic = (ITridasGeneric) ancestor;
			
			// IdAble? Nice, just set the generic fields then
			if(value instanceof IdAble) {
				GenericFieldUtils.setField(generic, dictionary.identifierField(), 
						((IdAble)value).getId());
				return value;
			}
			
			// ok, no match then. Let's look by id!
			TridasGenericField idField = GenericFieldUtils.findField(generic, dictionary.identifierField());
			if(idField != null && idField.isSetValue() && idField.getValue().length() > 0) {
				String givenId = idField.getValue();
				
				List<IdAble> vocabulary = ListUtil.subListOfType(getDictionary(), IdAble.class);
				for(IdAble idable : vocabulary) {
					if(givenId.equals(idable.getId())) {
						return idable;
					}
				}
			}

			// if it's null or empty string, remove the field (setField does this)
			if(value == null || "".equals(value.toString())) {
				GenericFieldUtils.setField(generic, dictionary.identifierField(), null);
				return null;
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
}
