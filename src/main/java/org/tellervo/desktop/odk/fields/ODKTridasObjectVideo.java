package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectVideo extends AbstractODKField {

	public ODKTridasObjectVideo()
	{
		super(ODKDataType.AUDIO, "tridas_object_file_video", "Video(s) of object", "Video of the object being studied", null);
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
