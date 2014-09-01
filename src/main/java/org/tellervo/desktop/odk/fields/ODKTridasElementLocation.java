package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementLocation extends AbstractODKField {
	
	public ODKTridasElementLocation()
	{
		super(ODKDataType.LOCATION, "tridas_element_location", "Location", "Coordinates for this element", null);
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
