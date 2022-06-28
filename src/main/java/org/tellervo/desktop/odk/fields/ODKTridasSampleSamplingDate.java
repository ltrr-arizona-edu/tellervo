package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTridasSampleSamplingDate extends AbstractODKField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasSampleSamplingDate()
	{
		super(ODKDataType.DATE, "tridas_sample_samplingdate", "Sampling date", Documentation.getDocumentation("sample.samplingDate"), null);
		this.setDefaultValue("today");
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
