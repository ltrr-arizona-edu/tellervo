package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectVegetationType extends AbstractODKField {

	public ODKTridasObjectVegetationType()
	{
		super(ODKDataType.STRING, "tridas_object_vegetation_type", "Vegetation type", Documentation.getDocumentation("object.vegetationType"), null);
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasObject.class;
	}

}
