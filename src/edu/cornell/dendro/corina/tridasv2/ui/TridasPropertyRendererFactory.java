package edu.cornell.dendro.corina.tridasv2.ui;

import javax.swing.table.TableCellRenderer;

import org.tridas.schema.Date;
import org.tridas.schema.DateTime;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;

import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityProperty;

public class TridasPropertyRendererFactory extends PropertyRendererRegistry {
	public TridasPropertyRendererFactory() {
		super();
		
		super.registerRenderer(Date.class, new TridasDateTimeCellRenderer());
		super.registerRenderer(DateTime.class, new TridasDateTimeCellRenderer());
	}
	
	public synchronized TableCellRenderer getRenderer(Property property) {
		boolean required = false;
		
		// handle enums nicely
		if(property instanceof TridasEntityProperty) {
			TridasEntityProperty ep = (TridasEntityProperty)property;
			
			// flag if it's required
			required = ep.isRequired();
			
			if(ep.getType().isEnum())
				return new EnumComboBoxRenderer(required);
			
			if(ep.isDictionaryAttached())
				return new ListComboBoxRenderer(required);
		}
		
		TableCellRenderer defaultRenderer = super.getRenderer(property);
		
		if(defaultRenderer == null && property instanceof TridasEntityProperty) {
			TridasEntityProperty ep = (TridasEntityProperty) property;
			
			if(ep.isEditable() && ep.getChildProperties().size() > 0)
				return new TridasDefaultPropertyRenderer();			
		}
				
		return defaultRenderer;
	}
}
