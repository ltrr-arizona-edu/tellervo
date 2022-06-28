package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementAddressLine1 extends AbstractODKField {

	private static final long serialVersionUID = 1L;

	public ODKTridasElementAddressLine1()
	{
		super(ODKDataType.STRING, "tridas_element_address_line1", "Address line 1", "Line 1 of address for this element", null);
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
