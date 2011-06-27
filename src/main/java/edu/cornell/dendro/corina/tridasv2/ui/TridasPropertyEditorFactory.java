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
package edu.cornell.dendro.corina.tridasv2.ui;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.tridas.schema.SeriesLink;
import org.tridas.schema.TridasDatingReference;
import org.tridas.schema.TridasLocationGeometry;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.util.converter.ConverterRegistry;

import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityProperty;

/**
 * @author Lucas Madar
 *
 */
public class TridasPropertyEditorFactory extends PropertyEditorRegistry {
	public TridasPropertyEditorFactory() {
		super();
		
		registerEditor(BigInteger.class, new BigIntegerPropertyEditor());
		registerEditor(BigDecimal.class, new BigDecimalPropertyEditor());
		new BigNumberConverter().register(ConverterRegistry.instance());

		PropertyEditor editor = new TridasSeriesLinkRendererEditor();
		registerEditor(SeriesLink.class, editor);
		registerEditor(TridasDatingReference.class, editor);
		
		// edit location geometry with dialog
		registerEditor(TridasLocationGeometry.class, LocationGeometryEditor.class);
	}
	
	public synchronized PropertyEditor getEditor(Property property) {
		if(property instanceof TridasEntityProperty) {
			TridasEntityProperty ep = (TridasEntityProperty) property;
			
			// Whew, it's an enum! Easy!
			if(ep.representsEnumType())
				return new EnumComboBoxPropertyEditor(ep.getEnumType());
			
			if(ep.isDictionaryAttached())
				return new ListComboBoxPropertyEditor(ep.getDictionary());
		}

		PropertyEditor defaultEditor = super.getEditor(property);

		if(defaultEditor == null && property instanceof TridasEntityProperty) {
			TridasEntityProperty ep = (TridasEntityProperty) property;
			
			if(ep.getChildProperties().size() > 0)
				return new TridasDefaultPropertyEditor();
		}
		
		return defaultEditor;
	}
}
