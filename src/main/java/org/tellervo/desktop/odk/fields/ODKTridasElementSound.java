package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasElementSound extends AbstractODKField {

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
		return TridasObject.class;
	}
	

	@Override
	public void setDefaultValue(Object o) {
		// NOT SUPPORTED FOR THIS DATA TYPE

	}

}
