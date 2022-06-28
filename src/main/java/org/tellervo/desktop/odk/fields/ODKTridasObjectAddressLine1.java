package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectAddressLine1 extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectAddressLine1()
	{
		super(ODKDataType.STRING, "tridas_object_address_line1", "Address line 1", "Line 1 of address for this object", null);
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
