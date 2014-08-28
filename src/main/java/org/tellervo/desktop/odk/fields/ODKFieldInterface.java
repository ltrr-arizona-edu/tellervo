package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;

public interface ODKFieldInterface {

	public String toString();
	
	public String getFieldName();
	
	public String getFieldCode();
	
	public String getFieldDescription();
	
	public Object getDefaultValue();
	
	public Class getFieldType();
	
	public Boolean isFieldRequired();
	
	public Class<? extends ITridas> getTridasClass();
	

	
}
