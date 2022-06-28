package org.tellervo.desktop.odk.fields;



public abstract class AbstractODKDecimalField extends AbstractODKField {

	private static final long serialVersionUID = 1L;
	private Double minValue = Double.MIN_VALUE;
	private Double maxValue = Double.MAX_VALUE;
	
	public AbstractODKDecimalField(String fieldcode, String fieldname, String description, Object defaultvalue )
	{
		super(ODKDataType.DECIMAL, fieldcode, fieldname, description, defaultvalue);

	}
	
	public AbstractODKDecimalField(String fieldcode, String fieldname, String description, Object defaultvalue, int sortIndex )
	{
		super(ODKDataType.DECIMAL, fieldcode, fieldname, description, defaultvalue, sortIndex);

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
