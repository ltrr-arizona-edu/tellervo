package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementCode extends AbstractODKField {
	
	public ODKTridasElementCode()
	{
		super(ODKDataType.STRING, "tridas_element_code", "Element code", Documentation.getDocumentation("element.code"), null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasElement.class;
	}

}
