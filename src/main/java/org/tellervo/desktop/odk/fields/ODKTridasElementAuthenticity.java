package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementAuthenticity extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	public ODKTridasElementAuthenticity()
	{
		super(ODKDataType.STRING, "tridas_element_authenticity", "Authenticity", Documentation.getDocumentation("element.authenticity"), null);
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
