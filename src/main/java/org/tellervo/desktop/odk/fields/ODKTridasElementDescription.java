package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementDescription extends AbstractODKField {

	private static final long serialVersionUID = 1L;

	public ODKTridasElementDescription()
	{
		super(ODKDataType.STRING, "tridas_element_description", "Element description", Documentation.getDocumentation("element.description"), null);
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
