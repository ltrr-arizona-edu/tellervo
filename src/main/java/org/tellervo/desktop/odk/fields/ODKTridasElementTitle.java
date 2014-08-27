package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementTitle extends AbstractODKField {

	@Override
	public String getFieldName() {
		return "Element title";
	}

	@Override
	public String getFieldCode() {
		return "tridas_element_title";
	}

	@Override
	public String getFieldDescription() {
		return Documentation.getDocumentation("element.title");
	}

	@Override
	public Class getFieldType() {
		return String.class;
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
