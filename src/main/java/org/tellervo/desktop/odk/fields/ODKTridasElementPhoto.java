package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementPhoto extends AbstractODKField {

	public ODKTridasElementPhoto()
	{
		super(ODKDataType.IMAGE, "tridas_element_file_photo", "Photo(s) of element", "Photos of the element being studied", null);
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
