package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectCode extends AbstractODKField {

	@Override
	public String getFieldName() {
		return "Object code";
	}
	
	@Override
	public String getFieldCode() {
		return "tridas_object_code";
	}

	@Override
	public String getFieldDescription() {
		return Documentation.getDocumentation("object.code");
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
	
	@Override
	public Object getDefaultValue()
	{
		return "ABC";
	}

}
