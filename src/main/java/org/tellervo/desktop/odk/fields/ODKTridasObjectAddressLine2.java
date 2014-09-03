package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectAddressLine2 extends AbstractODKField {

	public ODKTridasObjectAddressLine2()
	{
		super(ODKDataType.STRING, "tridas_object_address_line2", "Address line 2", "Line 2 of address for this object", null);
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
