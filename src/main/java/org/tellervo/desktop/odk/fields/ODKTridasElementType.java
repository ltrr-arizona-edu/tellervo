package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tellervo.desktop.tridasv2.ui.support.TridasDictionaryEntityProperty;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;

import edu.emory.mathcs.backport.java.util.Collections;

public class ODKTridasElementType extends AbstractODKChoiceField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasElementType()
	{
		super(ODKDataType.SELECT_ONE, "tridas_element_type", "Element type", Documentation.getDocumentation("element.type"), null);
		
		@SuppressWarnings("unchecked")
		List<ControlledVoc> types = Dictionary.getMutableDictionary("elementTypeDictionary");
		
		ArrayList<Object> objects = new ArrayList<Object>();
		for(ControlledVoc type: types)
		{
			objects.add(type);
		}
		
		
		Collections.sort(objects, new TridasDictionaryEntityProperty.ControlledVocComparator());

		
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(objects));
	}
	
	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasElement.class;
	}

}
