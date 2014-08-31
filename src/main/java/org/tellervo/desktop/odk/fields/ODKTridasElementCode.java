package org.tellervo.desktop.odk.fields;

import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;

public class ODKTridasElementCode extends AbstractODKField {

	String description;
	String name;
	Object defaultvalue;
	
	public ODKTridasElementCode()
	{
		description = Documentation.getDocumentation("element.code");
		name = "Element code";
	}
	
	@Override
	public String getFieldName() {
		return name;
	}

	@Override
	public String getFieldCode() {
		return "tridas_element_code";
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
		return TridasElement.class;
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
	@Override
	public Object getDefaultValue()
	{
		return defaultvalue;
	}
	
	
	@Override
	public void setDefaultValue(Object o)
	{
		this.defaultvalue = o;
	}
}
