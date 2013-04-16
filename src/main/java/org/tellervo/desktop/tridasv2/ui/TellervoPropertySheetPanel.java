package org.tellervo.desktop.tridasv2.ui;

import java.util.List;

import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetPanel;


/**
 * This class exists to kludge over a bug in PropertySheetPanel, whereby 
 * the array of TridasFiles is not written to the entity when writeToObject
 * is called.  
 * 
 * I presume this bug is also an issue for any other arrays, so be warned!
 * 
 * @author pwb48
 *
 */
public class TellervoPropertySheetPanel extends PropertySheetPanel {

	
	private static final long serialVersionUID = 1L;
	
	public TellervoPropertySheetPanel(TellervoPropertySheetTable propertiesTable) {
		super(propertiesTable);
	}

	@Override
	public void writeToObject(Object data) {
		// ensure pending edits are committed
		getTable().commitEditing();
		Property[] properties = getProperties();
		for (int i = 0, c = properties.length; i < c; i++) {
			properties[i].writeToObject(data);
				
		}

		Property[] prop2 = this.getProperties();
		
		for(Property p : prop2)
		{
			// Find any properties called 'files'
			if(p.getName().equals("files"))
			{
				Object files = p.getValue();
				
				if(data instanceof TridasObject)
				{
					((TridasObject)data).setFiles((List<TridasFile>) files);
				}
				else if(data instanceof TridasElement)
				{
					((TridasElement)data).setFiles((List<TridasFile>) files);
				}
				else if(data instanceof TridasSample)
				{
					((TridasSample)data).setFiles((List<TridasFile>) files);
				}
			}
		}
		

	}
	
	
}
