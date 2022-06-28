package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementAddressStateProvince extends AbstractODKField {

	private static final long serialVersionUID = 1L;

	public ODKTridasElementAddressStateProvince()
	{
		super(ODKDataType.STRING, "tridas_element_address_stateorprovince", "State/Province/Region", "State, province or region this element is in", null);
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
