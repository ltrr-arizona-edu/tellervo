package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectDescription extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectDescription()
	{
		super(ODKDataType.STRING, "tridas_object_description", "Object description", Documentation.getDocumentation("object.description"), null);
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
