package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementProcessing extends AbstractODKField {
	
	public ODKTridasElementProcessing()
	{
		super(ODKDataType.STRING, "tridas_element_processing", "Processing", Documentation.getDocumentation("element.processing"), null);
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
