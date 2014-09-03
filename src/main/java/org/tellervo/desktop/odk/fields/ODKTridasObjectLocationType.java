package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectLocationType extends AbstractODKChoiceField {

	public ODKTridasObjectLocationType()
	{
		super(ODKDataType.SELECT_ONE, "tridas_object_location_type", "Location type", Documentation.getDocumentation("object.location.type"), null);
		
		ArrayList<Object> objects = new ArrayList<Object>();
		for(NormalTridasLocationType type: NormalTridasLocationType.values())
		{
			objects.add(type);
		}
		
		
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(objects));
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
