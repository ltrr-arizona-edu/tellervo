package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tellervo.desktop.odk.SelectableChoice;


public abstract class AbstractODKChoiceField extends AbstractODKField {

	private ArrayList<SelectableChoice> choices = new ArrayList<SelectableChoice>();
	

	
	public void setPossibleChoices(ArrayList<SelectableChoice> choices)
	{
		this.choices = choices;
	}
	
	public ArrayList<SelectableChoice> getAvailableChoices()
	{
		return choices;
	}
	

}
