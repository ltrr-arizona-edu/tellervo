package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectAddressStateProvince extends AbstractODKField {

	public ODKTridasObjectAddressStateProvince()
	{
		super(ODKDataType.STRING, "tridas_object_address_stateorprovince", "State/Province/Region", "State, province or region this object is in", null);
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
