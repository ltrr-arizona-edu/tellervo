/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
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
