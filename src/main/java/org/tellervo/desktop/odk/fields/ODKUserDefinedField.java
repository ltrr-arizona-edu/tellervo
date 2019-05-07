package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;

public abstract class ODKUserDefinedField extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	private Class<? extends ITridas> attachedTo;
	
	public ODKUserDefinedField(ODKDataType type, String fieldcode, String fieldname, String description,
			Object defaultvalue, Class<? extends ITridas> attachedTo) {
		super(type, fieldcode, fieldname, description, null);
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


