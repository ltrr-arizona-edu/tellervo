package edu.cornell.dendro.corina.tridasv2.ui.support;

import org.tridas.interfaces.NormalTridasVoc;
import org.tridas.schema.ControlledVoc;

import com.l2fprod.common.propertysheet.Property;

/**
 * An entity property that supports NormalTridas-style ControlledVocs
 * 
 * @author Lucas Madar
 */
public class TridasNormalProperty extends TridasEntityProperty {
	
	/**
	 * Copy constructor
	 * 
	 * @param property
	 */
	public TridasNormalProperty(TridasEntityProperty property) {
		super(property);
	
		// sanity check
		if(!NormalTridasVoc.class.isAssignableFrom(property.getType()))
			throw new IllegalArgumentException(property.getType().getName() + 
					" is not a NormalTridas property");
	}
	
	/**
	 * No sub-properties here
	 */
	@Override
	public Property[] getSubProperties() {
		return null;
	}
	
	@Override
	protected Object getInternalTranslatedValue(Object value) {
		// return the NormalTridasVoc
		if(value instanceof NormalTridasVoc<?>)
			return ((NormalTridasVoc<?>)value).getNormalTridas();
		
		return value;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected Object getExternalTranslatedValue(Object value) {
		// already how we want it? Sweet!
		if(value instanceof ControlledVoc || value instanceof NormalTridasVoc<?>)
			return value;
		
		// ok, it's an enum, so map it to a NormalTridasVoc
		if(value instanceof Enum<?>) {
			try {
				// instantiate our type
				NormalTridasVoc voc = (NormalTridasVoc) getType().newInstance();
				voc.setNormalTridas((Enum) value);
				
				return voc;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return value;
	}
	
	@Override
	public boolean representsEnumType() {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Enum<?>> getEnumType() {
		// get our child 
		for(TridasEntityProperty property : childProperties) {
			if(property.getType().isEnum())
				return (Class<? extends Enum<?>>) property.getType();
		}
		
		throw new IllegalStateException("No child enum type to rely on for TridasNormal?");
	}

	private static final long serialVersionUID = 1L;
}
