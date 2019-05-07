package org.tellervo.desktop.odk.fields;

import org.tridas.interfaces.ITridas;

public interface ODKFieldInterface extends Comparable<ODKFieldInterface>  {

	public String toString();
	
	public String getFieldName();
	
	public String getFieldCode();
	
	public String getFieldDescription();
	
	public Object getDefaultValue();
	
	public ODKDataType getFieldType();
	
	public Boolean isFieldRequired();
	
	public boolean isFieldHidden();
	
	public Class<? extends ITridas> getTridasClass();
	
	public void setIsFieldHidden(boolean b);
	
	public void setName(String str);
	
	public void setDescription(String str);
	
	public void setDefaultValue(Object o);
	
}
