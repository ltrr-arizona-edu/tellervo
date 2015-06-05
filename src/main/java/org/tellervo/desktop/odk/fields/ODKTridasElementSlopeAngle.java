package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementSlopeAngle extends AbstractODKIntegerField {
	
	public ODKTridasElementSlopeAngle()
	{
		super("tridas_element_slope_angle", "Slope angle", Documentation.getDocumentation("element.slope.angle"), null);
		setMinValue(0);
		setMaxValue(90);
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
