package edu.cornell.dendro.corina.io.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.tridasv2.TridasComparator;

public class UpdateEntityListCommand implements ICommand {
	/*private TridasEntityListHolder lists = new TridasEntityListHolder();
	@Override
	public void execute(MVCEvent argEvent) {
		ImportEntityListChangedEvent event = (ImportEntityListChangedEvent) argEvent;
		
		List<? extends ITridas> entities = null;
		
		ITridas currentEntity = event.getValue();
		ITridas parentEntity = event.parentEntity;
		
		if (currentEntity instanceof TridasProject)
		{
			// This entity is a project so set list to null as 
			// projects aren't supported
		}
		else if(currentEntity instanceof TridasObject)
		{
			// This entity is an object so grab object dictionary
			entities = App.tridasObjects.getObjectList();
		}
		
		// Otherwise, check that the parent is already in db by checking
		// the identifier domain and set list accordingly 
		try{
			if (parentEntity.getIdentifier().getDomain().equals(App.domain))
			{
				entities = lists.getChildList(parentEntity, true);
			}
		} catch (Exception e)
		{	}	
		
		event.model.setEntityChooserList(sortList(entities));
		
	}
	
	/**
	 * Sort a list of ITridas objects
	 * 
	 * @param list
	 */
	@SuppressWarnings("unused")
	private ArrayList<? extends ITridas> sortList(List<? extends ITridas> list) {
		// Sort list intelligently
		ArrayList<ITridas> entities = new ArrayList<ITridas>();
		
		if(list==null || list.isEmpty()) return null;
		
		for(int i=0; i<list.size(); i++)
		{
			ITridas e = list.get(i);	
			entities.add(e);
		}
		
		
		
		if(!(entities.get(0) instanceof TridasObject))
		{
			TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
					TridasComparator.NullBehavior.NULLS_LAST, 
					TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
			
			Collections.sort(entities, numSorter);
		}

		return entities;
	}

	@Override
	public void execute(MVCEvent argEvent) {
		// TODO Auto-generated method stub
		
	}

}
