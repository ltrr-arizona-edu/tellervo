package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTridasSampleKnots extends AbstractODKChoiceField {

	public ODKTridasSampleKnots()
	{
		super(ODKDataType.SELECT_ONE, "tridas_sample_knots", "Does sample have knots?", Documentation.getDocumentation("sample.knots"), null);
				
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add("Yes");
		objects.add("No");
		
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(objects));
	}
	
	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasSample.class;
	}

}
