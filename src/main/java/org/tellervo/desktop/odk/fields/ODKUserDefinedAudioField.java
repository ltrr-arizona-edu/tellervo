package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;

public class ODKUserDefinedAudioField extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	private final Class<? extends ITridas> attachedTo;
	
	
	public ODKUserDefinedAudioField(String fieldcode, String fieldname, String description,
			Object defaultvalue, Class<? extends ITridas> attachedTo)
	{
		super(ODKDataType.AUDIO, fieldcode, fieldname, description, null);
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
