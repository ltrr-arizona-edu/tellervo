package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTridasSamplePosition extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasSamplePosition()
	{
		super(ODKDataType.STRING, "tridas_sample_position", "Sample position", Documentation.getDocumentation("sample.position"), null);
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
