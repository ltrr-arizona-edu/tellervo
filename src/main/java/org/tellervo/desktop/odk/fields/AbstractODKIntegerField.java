package org.tellervo.desktop.odk.fields;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractODKIntegerField extends AbstractODKField {

	private Integer minValue = Integer.MIN_VALUE;
	private Integer maxValue = Integer.MAX_VALUE;
	
	public AbstractODKIntegerField(ODKDataType datatype, String fieldcode, String fieldname, String description, Object defaultvalue )
	{
		super(datatype, fieldcode, fieldname, description, defaultvalue);

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
