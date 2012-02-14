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
package org.tellervo.desktop.tridasv2.ui.support;

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
