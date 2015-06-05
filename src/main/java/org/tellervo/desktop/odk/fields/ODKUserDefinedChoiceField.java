package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;
import java.util.Enumeration;

import org.tellervo.desktop.odk.SelectableChoice;
import org.tridas.interfaces.ITridas;

public class ODKUserDefinedChoiceField extends AbstractODKChoiceField {

	private Class<? extends ITridas> attachedTo;
	
	public ODKUserDefinedChoiceField(String fieldcode, String fieldname, String description,
			Object defaultvalue, Class<? extends ITridas> attachedTo, Enumeration<String> choices) {
		super(ODKDataType.SELECT_ONE, fieldcode, fieldname, description, defaultvalue);
		this.attachedTo = attachedTo;
		
		ArrayList<Object> list = new ArrayList<Object>();
		while(choices.hasMoreElements()){
			list.add(choices.nextElement());
			
		}
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(list));
		this.selectAllChoices();
	}

	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return attachedTo;
	}

}
