package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementAddressCountry extends AbstractODKField {

	public ODKTridasElementAddressCountry()
	{
		super(ODKDataType.STRING, "tridas_element_address_country", "Country", "Country where this element is found", null);
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
