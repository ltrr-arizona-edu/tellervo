package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectAddressPostalCode extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectAddressPostalCode()
	{
		super(ODKDataType.STRING, "tridas_object_address_postalcode", "Postal code", "Postal code for this object", null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasObject.class;
	}

}
