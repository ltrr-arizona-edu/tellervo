package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasParentObjectCode extends AbstractODKField {

	public ODKTridasParentObjectCode()
	{
		super(ODKDataType.STRING, "tridas_parent_object_code", "Parent object code", "Lab code for parent object when this object is a subobject", null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasObject.class;
	}
}
