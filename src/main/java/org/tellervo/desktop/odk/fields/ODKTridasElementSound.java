package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementSound extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasElementSound()
	{
		super(ODKDataType.AUDIO, "tridas_element_file_sound", "Sound clip(s)", "Audio recording, typically a voice memo regarding the element", null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasElement.class;
	}
	

	@Override
	public void setDefaultValue(Object o) {
		// NOT SUPPORTED FOR THIS DATA TYPE

	}

}
