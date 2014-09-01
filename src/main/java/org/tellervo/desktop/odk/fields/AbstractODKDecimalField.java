package org.tellervo.desktop.odk.fields;



public abstract class AbstractODKDecimalField extends AbstractODKField {

	private Double minValue = Double.MIN_VALUE;
	private Double maxValue = Double.MAX_VALUE;
	
	public AbstractODKDecimalField(ODKDataType datatype, String fieldcode, String fieldname, String description, Object defaultvalue )
	{
		super(datatype, fieldcode, fieldname, description, defaultvalue);

	}
	
	public void setMinValue(Double val)
	{
		minValue = val;
	}
	
	public void setMaxValue(Double val)
	{
		maxValue = val;
	}
	
	public Double getMinValue()
	{
		return minValue;
	}
	
	public Double getMaxValue()
	{
		return maxValue;
	}
}
