package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTridasSampleCode extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasSampleCode()
	{
		super(ODKDataType.STRING, "tridas_sample_title", "Sample code", Documentation.getDocumentation("sample.title"), null);
		this.setDefaultValue("A");
	}
	
	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasSample.class;
	}

}
