package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementBedrock extends AbstractODKField {
	
	public ODKTridasElementBedrock()
	{
		super(ODKDataType.STRING, "tridas_element_soil_description", "Soil description", Documentation.getDocumentation("element.soil.description"), null);
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
