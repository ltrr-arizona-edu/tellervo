package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectLocationComments extends AbstractODKField {

	public ODKTridasObjectLocationComments()
	{
		super(ODKDataType.STRING, "tridas_object_location_comments", "Location comments", Documentation.getDocumentation("object.location.comments"), null);
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
