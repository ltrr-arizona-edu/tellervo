package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasRadius;

public class ODKTridasRadiusAzimuth extends AbstractODKIntegerField {
	
	public ODKTridasRadiusAzimuth()
	{
		super(ODKDataType.INTEGER, "tridas_radius_azimuth", "Sampling azimuth", Documentation.getDocumentation("radius.azimuth")+" This is only relevant in this form if the sample is a core.", null);
		setMinValue(0);
		setMaxValue(360);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasRadius.class;
	}

}
