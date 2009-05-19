/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.util.converter.ConverterRegistry;

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
		if(property instanceof EntityProperty) {
			EntityProperty ep = (EntityProperty) property;
			
			// Whew, it's an enum! Easy!
			if(ep.clazz.isEnum())
				return new EnumComboBoxPropertyEditor(ep.clazz);
			
			if(ep.isDictionaryAttached())
				return new ListComboBoxPropertyEditor(DictionaryMappings.getDictionaryList(ep.qname));
		}

		PropertyEditor defaultEditor = super.getEditor(property);

		if(defaultEditor == null && property instanceof EntityProperty) {
			EntityProperty ep = (EntityProperty) property;
			
			if(ep.getChildProperties().size() > 0)
				return new TridasDefaultPropertyEditor();
		}
		
		return defaultEditor;
	}
}
