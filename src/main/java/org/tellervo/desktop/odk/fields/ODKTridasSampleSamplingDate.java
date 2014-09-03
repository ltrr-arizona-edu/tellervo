package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTridasSampleSamplingDate extends AbstractODKField {
	
	public ODKTridasSampleSamplingDate()
	{
		super(ODKDataType.STRING, "tridas_sample_samplingdate", "Sampling date", Documentation.getDocumentation("sample.samplingDate"), null);
		this.setDefaultValue("string(today())");
		this.setIsFieldHidden(true);
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
