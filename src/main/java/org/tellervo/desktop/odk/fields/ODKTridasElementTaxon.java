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

public class ODKTridasElementTaxon extends AbstractODKChoiceField {

	public ODKTridasElementTaxon()
	{
		super(ODKDataType.SELECT_ONE, "tridas_element_taxon", "Taxon", Documentation.getDocumentation("element.taxon"), null);
		
		List<ControlledVoc> types = Dictionary.getMutableDictionary("taxonDictionary");
		
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
