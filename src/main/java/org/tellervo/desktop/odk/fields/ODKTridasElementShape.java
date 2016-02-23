package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.TridasElement;

public class ODKTridasElementShape extends AbstractODKChoiceField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasElementShape()
	{
		super(ODKDataType.SELECT_ONE, "tridas_element_shape", "Shape of element", Documentation.getDocumentation("element.shape"), null);

		
		ArrayList<Object> objects = new ArrayList<Object>();
		for(NormalTridasShape type: NormalTridasShape.values())
		{
			objects.add(type);
		}
		
		
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(objects));
	}
	
	@Override
	public Boolean isFieldRequired() {
		return false;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasElement.class;
	}

}
