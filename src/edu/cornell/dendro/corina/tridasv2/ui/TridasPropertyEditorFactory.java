/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.math.BigInteger;

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
	}
	
	public synchronized PropertyEditor getEditor(Property property) {
		if(property instanceof TridasEntityProperty) {
			TridasEntityProperty ep = (TridasEntityProperty) property;
			
			// Whew, it's an enum! Easy!
			Class<?> type = ep.getType();
			if(type.isEnum())
				return new EnumComboBoxPropertyEditor(type);
			
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
