package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasObject;

public class ODKTridasElementDimUnit extends AbstractODKChoiceField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasElementDimUnit()
	{
		super(ODKDataType.SELECT_ONE, "tridas_object_dimensions_unit", "Element dimensions units", Documentation.getDocumentation("unit"), null);
		
		@SuppressWarnings("unchecked")
		List<ControlledVoc> dictItems = Dictionary.getMutableDictionary("objectTypeDictionary");	
		ArrayList<Object> objects = new ArrayList<Object>();
		for(ControlledVoc item : dictItems)
		{
			objects.add(item);
		}
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(objects));
	}
	
	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasObject.class;
	}

}

