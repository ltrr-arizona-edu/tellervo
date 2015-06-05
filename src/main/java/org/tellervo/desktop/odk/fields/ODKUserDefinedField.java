package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;

public class ODKUserDefinedField extends AbstractODKField {

	private Class<? extends ITridas> attachedTo;
	
	public ODKUserDefinedField(ODKDataType datatype, String fieldcode, String fieldname, String description,
			Object defaultvalue, Class<? extends ITridas> attachedTo) {
		super(datatype, fieldcode, fieldname, description, null);
		this.attachedTo = attachedTo;
		
	}

	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return attachedTo;
	}

}
