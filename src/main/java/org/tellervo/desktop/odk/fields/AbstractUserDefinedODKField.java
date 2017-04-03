package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;

public abstract class AbstractUserDefinedODKField extends AbstractODKField {

	private static final long serialVersionUID = 1L;
	private boolean isRequired;
	private final Class<? extends ITridas> clazz;
	
	public AbstractUserDefinedODKField(ODKDataType datatype, String fieldcode,
			String fieldname, String description, Object defaultvalue, Class<? extends ITridas> clazz, boolean isRequired) {
		super(datatype, fieldcode, fieldname, description, defaultvalue);
		
		this.clazz = clazz;
		this.isRequired = isRequired;
	}

	@Override
	public Boolean isFieldRequired() {
		return isRequired;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return clazz;
	}

}
