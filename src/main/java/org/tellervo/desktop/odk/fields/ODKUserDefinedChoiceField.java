package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;
import java.util.Enumeration;

import org.tellervo.desktop.odk.SelectableChoice;
import org.tridas.interfaces.ITridas;

public class ODKUserDefinedChoiceField extends AbstractODKChoiceField {
	
	private static final long serialVersionUID = 1L;

	
	private Class<? extends ITridas> attachedTo;
	
	public ODKUserDefinedChoiceField(String fieldcode, String fieldname, String description,
			Object defaultvalue, Class<? extends ITridas> attachedTo, ArrayList<Object>  choices) {
		super(ODKDataType.SELECT_ONE, fieldcode, fieldname, description, defaultvalue);
		this.attachedTo = attachedTo;
	
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(choices));
		
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
