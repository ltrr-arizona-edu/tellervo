package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTridasSampleDescription extends AbstractODKField {

	public ODKTridasSampleDescription()
	{
		super(ODKDataType.STRING, "tridas_sample_description", "Sample description", Documentation.getDocumentation("sample.description"), null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasSample.class;
	}
	
}
