package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;

import edu.emory.mathcs.backport.java.util.Collections;

public class ODKTridasProject extends AbstractODKChoiceField {
	
	private static final long serialVersionUID = 1L;

	
	public ODKTridasProject()
	{
		super(ODKDataType.SELECT_ONE, "tridas_project", "Project", Documentation.getDocumentation("project"), null, 0);
		
		List<TridasProject> projects = App.tridasProjects.getProjectList();
		
		ArrayList<Object> p2 = new ArrayList<Object>();
		for(TridasProject p: projects)
		{
			p2.add(p);
		}
		
		
		Collections.sort(p2, new TridasComparator());

		
		this.setPossibleChoices(SelectableChoice.makeObjectsSelectable(p2));
	}
	
	@Override
	public Boolean isFieldRequired() {
		return true;
	}

	@Override
	public Class<? extends ITridas> getTridasClass() {
		return TridasObject.class;
	}

}
