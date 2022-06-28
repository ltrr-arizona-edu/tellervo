package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectLocation extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectLocation()
	{
		super(ODKDataType.LOCATION, "tridas_object_location", "Location", "Coordinates for this object", null);
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
