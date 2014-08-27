package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectTitle extends AbstractODKField {

	@Override
	public String getFieldName() {
		return "Object title";
	}

	@Override
	public String getFieldCode() {
		return "tridas_object_title";
	}

	@Override
	public String getFieldDescription() {
		return Documentation.getDocumentation("object.title");
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
		return TridasObject.class;
	}

}
