package edu.cornell.dendro.corina.tridasv2;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableCellRenderer;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;

public class TridasPropertyRendererFactory extends PropertyRendererRegistry {
	public TridasPropertyRendererFactory() {
		super();
	}
	
	public synchronized TableCellRenderer getRenderer(Property property) {
		// handle enums nicely
		if(property instanceof EntityProperty && ((EntityProperty)property).clazz.isEnum())
			return new EnumComboBoxRenderer();
		
		return super.getRenderer(property);
	}
}
