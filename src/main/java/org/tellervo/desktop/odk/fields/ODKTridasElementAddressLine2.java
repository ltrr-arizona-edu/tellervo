package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementAddressLine2 extends AbstractODKField {

	public ODKTridasElementAddressLine2()
	{
		super(ODKDataType.STRING, "tridas_element_address_line2", "Address line 2", "Line 2 of address for this element", null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasElement.class;
	}

}
