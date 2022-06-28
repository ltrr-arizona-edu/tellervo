package org.tellervo.desktop.odk.fields;



public abstract class AbstractODKIntegerField extends AbstractODKField {

	private static final long serialVersionUID = 1L;
	private Integer minValue = Integer.MIN_VALUE;
	private Integer maxValue = Integer.MAX_VALUE;
	
	public AbstractODKIntegerField(String fieldcode, String fieldname, String description, Object defaultvalue )
	{
		super(ODKDataType.INTEGER, fieldcode, fieldname, description, defaultvalue);

	}
	
	public AbstractODKIntegerField(String fieldcode, String fieldname, String description, Object defaultvalue, int sortIndex )
	{
		super(ODKDataType.INTEGER, fieldcode, fieldname, description, defaultvalue, sortIndex);

	}
	
	public void setMinValue(Integer val)
	{
		minValue = val;
	}
	
	public void setMaxValue(Integer val)
	{
		maxValue = val;
	}
	
	public Integer getMinValue()
	{
		return minValue;
	}
	
	public Integer getMaxValue()
	{
		return maxValue;
	}
}
