package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementAddressPostalCode extends AbstractODKField {

	private static final long serialVersionUID = 1L;

	public ODKTridasElementAddressPostalCode()
	{
		super(ODKDataType.STRING, "tridas_element_address_postalcode", "Postal code", "Postal code for this element", null);
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
