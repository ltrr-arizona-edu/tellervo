package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementAddressCityOrTown extends AbstractODKField {

	public ODKTridasElementAddressCityOrTown()
	{
		super(ODKDataType.STRING, "tridas_element_address_cityortown", "City/town", "City or town", null);
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
