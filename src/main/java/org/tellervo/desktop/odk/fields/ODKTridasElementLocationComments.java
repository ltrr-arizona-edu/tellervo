package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementLocationComments extends AbstractODKField {

	public ODKTridasElementLocationComments()
	{
		super(ODKDataType.STRING, "tridas_element_location_comments", "Location comments", Documentation.getDocumentation("element.location.comments"), null);
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
