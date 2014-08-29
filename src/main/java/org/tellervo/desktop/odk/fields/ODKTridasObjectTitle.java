package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

public class ODKTridasObjectTitle extends AbstractODKField {
	
	String description;
	String name;
	
	public ODKTridasObjectTitle()
	{
		description = Documentation.getDocumentation("object.title");
		name = "Object title";
	}
	
	@Override
	public String getFieldName() {
		return name;
	}

	@Override
	public String getFieldCode() {
		return "tridas_object_title";
	}

	@Override
	public String getFieldDescription() {
		return description;
	}
	
	@Override
	public ODKDataType getFieldType() {
		return ODKDataType.STRING;
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
