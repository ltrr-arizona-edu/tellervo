package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementTitle extends AbstractODKField {

	public ODKTridasElementTitle()
	{
		super(ODKDataType.STRING, "tridas_element_title", "Element title", Documentation.getDocumentation("element.title"), null);
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
