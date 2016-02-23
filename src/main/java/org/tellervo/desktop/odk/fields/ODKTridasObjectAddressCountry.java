package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectAddressCountry extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectAddressCountry()
	{
		super(ODKDataType.STRING, "tridas_object_address_country", "Country", "Country where this object is found", null);
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
