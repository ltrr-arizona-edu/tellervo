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

	String description;
	String name;
	
	public ODKTridasObjectType()
	{
		List<ControlledVoc> types = Dictionary.getMutableDictionary("objectTypeDictionary");
		
		ArrayList<Object> objects = new ArrayList<Object>();
		for(ControlledVoc type: types)
		{
			objects.add(type);
		}
		
		
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(objects));
		
		description = Documentation.getDocumentation("object.type");
		name = "Object type";
	}
	
	
	@Override
	public String getFieldName() {
		return name;
	}

	@Override
	public String getFieldCode() {
		return "tridas_object_type";
	}

	@Override
	public String getFieldDescription() {
		return description;
	}

	@Override
	public ODKDataType getFieldType() {
		return ODKDataType.SELECT_ONE;
	}

	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasObject.class;
	}

	@Override
	public void setName(String str)
	{
		this.name = str;
	}

	@Override
	public void setDescription(String str)
	{
		this.description = str;
	}
}
