package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTridasSamplePhoto extends AbstractODKField {

	public ODKTridasSamplePhoto()
	{
		super(ODKDataType.IMAGE, "tridas_sample_file_photo", "Photo(s) of sample", "Photos of the sample being studied", null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasSample.class;
	}
	

	@Override
	public void setDefaultValue(Object o) {
		// NOT SUPPORTED FOR THIS DATA TYPE

	}

}
