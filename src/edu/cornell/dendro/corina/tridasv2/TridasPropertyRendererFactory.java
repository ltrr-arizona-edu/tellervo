package edu.cornell.dendro.corina.tridasv2;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableCellRenderer;

import org.tridas.schema.ControlledVoc;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;

public class TridasPropertyRendererFactory extends PropertyRendererRegistry {
	public TridasPropertyRendererFactory() {
		super();
	}
	
	public synchronized TableCellRenderer getRenderer(Property property) {
		// handle enums nicely
		if(property instanceof EntityProperty) {
			EntityProperty ep = (EntityProperty)property;
			
			if(ep.clazz.isEnum())
				return new EnumComboBoxRenderer();
			
			if(ep.isDictionaryAttached())
				return new ListComboBoxRenderer();
		}
		
		TableCellRenderer defaultRenderer = super.getRenderer(property);
		
		if(defaultRenderer == null && property instanceof EntityProperty) {
			EntityProperty ep = (EntityProperty) property;
			
			if(ep.isEditable() && ep.getChildProperties().size() > 0)
				return new TridasDefaultPropertyRenderer();			
		}
				
		return defaultRenderer;
	}
}
