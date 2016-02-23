package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementVideo extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasElementVideo()
	{
		super(ODKDataType.VIDEO, "tridas_element_file_video", "Video(s) of element", "Video of the element being studied", null);
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
