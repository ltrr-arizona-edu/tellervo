package edu.cornell.dendro.corina.tridasv2.ui;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.tridas.schema.Date;
import org.tridas.schema.DateTime;
import org.tridas.schema.SeriesLink;
import org.tridas.schema.TridasDating;
import org.tridas.schema.TridasDatingReference;
import org.tridas.schema.Year;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;

import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityProperty;

public class TridasPropertyRendererFactory extends PropertyRendererRegistry {
	public TridasPropertyRendererFactory() {
		super();
		
		TableCellRenderer myRenderer = new TridasYearDateTimeCellRenderer();
		super.registerRenderer(Date.class, myRenderer);
		super.registerRenderer(DateTime.class, myRenderer);
		super.registerRenderer(Year.class, myRenderer);
		
		myRenderer = new TridasSeriesLinkRendererEditor();
		super.registerRenderer(SeriesLink.class, myRenderer);
		super.registerRenderer(TridasDatingReference.class, myRenderer);
		
		super.registerRenderer(TridasDating.class, TridasDatingCellRenderer.class);
	}
	
	public synchronized TableCellRenderer getRenderer(Property property) {
		boolean required = false;
		
		// handle enums nicely
		if(property instanceof TridasEntityProperty) {
			TridasEntityProperty ep = (TridasEntityProperty)property;
			
			// flag if it's required
			required = ep.isRequired();
			
			// check it's an enum or a parent of an enum...
			if(ep.representsEnumType())
				return new EnumComboBoxRenderer(required);
			
			if(ep.isDictionaryAttached())
				return new ListComboBoxRenderer(required);
		}
		
		// get a renderer, if one exists
		TableCellRenderer defaultRenderer = super.getRenderer(property);
		
		// if one doesn't exist, and it has children, mark it as such
		if(defaultRenderer == null && property instanceof TridasEntityProperty) {
			TridasEntityProperty ep = (TridasEntityProperty) property;
			
			if(ep.isEditable() && ep.getChildProperties().size() > 0)
				return new TridasDefaultPropertyRenderer();			
		}

		if(!required)
			return defaultRenderer;
		
		// ok, create a renderer for it if nothing exists
		if(defaultRenderer == null)
			defaultRenderer = new DefaultTableCellRenderer();
		
		// wrap it if it's required
		return new RequiredCellRendererWrapper(defaultRenderer);
	}
}
