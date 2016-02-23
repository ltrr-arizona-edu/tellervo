package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementCode extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	public ODKTridasElementCode()
	{
		super(ODKDataType.STRING, "tridas_element_title", "Element code", Documentation.getDocumentation("element.title"), null);
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
