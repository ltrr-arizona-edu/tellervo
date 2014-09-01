package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementMarks extends AbstractODKField {
	
	public ODKTridasElementMarks()
	{
		super(ODKDataType.STRING, "tridas_element_marks", "Marks", Documentation.getDocumentation("element.marks"), null);
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
