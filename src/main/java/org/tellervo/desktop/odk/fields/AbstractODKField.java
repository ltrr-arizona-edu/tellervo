package org.tellervo.desktop.odk.fields;


public abstract class AbstractODKField implements ODKFieldInterface {

	protected String name;
	protected String description;
	protected Object defaultvalue;
	protected String fieldcode;
	protected ODKDataType datatype;
	protected boolean isFieldHidden = false;
	
	/**
	 * Constructor for a generic ODK field
	 * 
	 * @param datatype
	 * @param fieldcode
	 * @param fieldname
	 * @param description
	 * @param defaultvalue
	 */
	public AbstractODKField(ODKDataType datatype, String fieldcode, String fieldname, String description, Object defaultvalue)
	{
		this.description = description;
		this.name = fieldname;
		this.defaultvalue = defaultvalue;
		this.fieldcode = fieldcode;
		this.datatype = datatype;
	}
		
	@Override
	public Object getDefaultValue()
	{
		return defaultvalue;
	}
	
	@Override
	public ODKDataType getFieldType() {
		return datatype;
	}
		
	@Override
	public String getFieldName() {
		return name;
	}

	@Override
	public String getFieldCode() {
		return fieldcode;
	}

	@Override
	public String getFieldDescription() {
		return description;
	}
	
	@Override
	public boolean isFieldHidden(){
		return this.isFieldHidden;
	}
	
	@Override
	public String toString()
	{
		return this.getFieldName();
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
	public void setDefaultValue(Object o)
	{
		this.defaultvalue = o;
	}

	@Override
	public void setIsFieldHidden(boolean b)
	{
		this.isFieldHidden=b;
	}

	
}
