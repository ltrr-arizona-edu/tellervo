package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectAddressCityOrTown extends AbstractODKField {

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
