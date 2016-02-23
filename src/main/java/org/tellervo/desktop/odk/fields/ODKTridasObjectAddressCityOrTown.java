package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectAddressCityOrTown extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectAddressCityOrTown()
	{
		super(ODKDataType.STRING, "tridas_object_address_cityortown", "City/town", "City or town", null);
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
