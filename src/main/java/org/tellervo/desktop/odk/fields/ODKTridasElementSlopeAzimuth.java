package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementSlopeAzimuth extends AbstractODKIntegerField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasElementSlopeAzimuth()
	{
		super("tridas_element_slope_azimuth", "Slope azimuth", Documentation.getDocumentation("element.slope.azimuth"), null);
		setMinValue(0);
		setMaxValue(360);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasElement.class;
	}

}
