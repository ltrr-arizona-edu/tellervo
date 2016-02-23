package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectCode extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectCode()
	{
		super(ODKDataType.STRING, "tridas_object_code", "Object code", "Short code name for object, traditionally three letters", null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasObject.class;
	}
}
