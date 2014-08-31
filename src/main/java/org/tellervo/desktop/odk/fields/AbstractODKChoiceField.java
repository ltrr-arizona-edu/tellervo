package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.odk.SelectableChoice;


public abstract class AbstractODKChoiceField extends AbstractODKField {

	private ArrayList<SelectableChoice> choices = new ArrayList<SelectableChoice>();
	private static final Logger log = LoggerFactory.getLogger(AbstractODKChoiceField.class);


	public AbstractODKChoiceField(ODKDataType datatype, String fieldcode, String fieldname, String description, Object defaultvalue)
	{
		super(datatype, fieldcode, fieldname, description, defaultvalue);

	}
	
	public void setPossibleChoices(ArrayList<SelectableChoice> choices)
	{
		this.choices = choices;
	
	}
	
	public void setSelectedChoices(Object[] selchoice)
	{
		for(SelectableChoice c : choices)
		{
			c.setSelected(false);
		}
		
		for(Object c : selchoice)
		{
			if(choices.contains(c))
			{
				choices.get(choices.indexOf(c)).setSelected(true);
			}
			else
			{
				log.error("The selected choice is not one of the possible choices");
			}
		}
	}
	
	public int[] getSelectedChoicesIndices()
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		for(int i=0; i<choices.size(); i++)
		{
			if(choices.get(i).isSelected()) indices.add(i);
		}
		
		return ArrayUtils.toPrimitive(indices.toArray(new Integer[indices.size()]));
	}
	
	public SelectableChoice[] getSelectedChoices()
	{
		ArrayList<SelectableChoice> arr = new ArrayList<SelectableChoice>();
		
		for(SelectableChoice choice : choices)
		{
			if(choice.isSelected()) arr.add(choice);
		}
		
		return arr.toArray(new SelectableChoice[arr.size()]);
	}
	
	public ArrayList<SelectableChoice> getAvailableChoices()
	{
		return choices;
	}
	

}
