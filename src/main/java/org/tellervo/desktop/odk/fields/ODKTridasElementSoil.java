package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementSoil extends AbstractODKField {
	
	public ODKTridasElementSoil()
	{
		super(ODKDataType.STRING, "tridas_element_bedrock_description", "Bedrock description", Documentation.getDocumentation("element.bedrock.description"), null);
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
