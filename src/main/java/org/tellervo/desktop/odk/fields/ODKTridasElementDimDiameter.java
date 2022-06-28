package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementDimDiameter extends AbstractODKDecimalField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasElementDimDiameter()
	{
		super("tridas_element_dimensions_diameter", "Dimension diameter", Documentation.getDocumentation("dimensions.diameter"), null);
		setMinValue(0.0);
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
