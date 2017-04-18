package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectType extends AbstractODKChoiceField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasObjectType()
	{
		super(ODKDataType.SELECT_ONE, "tridas_object_type", "Object type", Documentation.getDocumentation("Object type"), null, 3);
		
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

