package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;

public class ODKUserDefinedIntegerField extends AbstractODKIntegerField {
	
	private static final long serialVersionUID = 1L;

	
	private Class<? extends ITridas> attachedTo;
	
	public ODKUserDefinedIntegerField(String fieldcode, String fieldname, String description,
			Object defaultvalue, Class<? extends ITridas> attachedTo) {
		super(fieldcode, fieldname, description, null);
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
