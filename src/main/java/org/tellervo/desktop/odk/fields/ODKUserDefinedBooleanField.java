package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tellervo.desktop.odk.SelectableChoice;
import org.tridas.interfaces.ITridas;

public class ODKUserDefinedBooleanField extends AbstractODKChoiceField {
	
	private static final long serialVersionUID = 1L;

	private final Class<? extends ITridas> attachedTo;
	
	
	public ODKUserDefinedBooleanField(String fieldcode, String fieldname, String description,
			Object defaultvalue, Class<? extends ITridas> attachedTo)
	{
		super(ODKDataType.SELECT_ONE, fieldcode, fieldname, description, null);
		this.attachedTo = attachedTo;	
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add("Yes");
		objects.add("No");
		
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(objects));
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
