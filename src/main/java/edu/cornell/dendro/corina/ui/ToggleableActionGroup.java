/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package edu.cornell.dendro.corina.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A ButtonGroup-like interface for actions
 * 
 * Allows a simple implementation of radio buttons.
 * Uses CorinaAction incase it's extended, maybe? :)
 * 
 * @author Lucas Madar
 *
 */
public class ToggleableActionGroup implements PropertyChangeListener {
	private List<CorinaAction> actions;
	private boolean currentlyProcessingAction;
	
	public ToggleableActionGroup() {
		actions = new ArrayList<CorinaAction>();
		currentlyProcessingAction = false;
	}
	
	public void add(CorinaAction action) {
		actions.add(action);		
		action.addPropertyChangeListener(this);
	}
	
	public void remove(CorinaAction action) {
		actions.remove(action);
		action.removePropertyChangeListener(this);
	}

	public synchronized void propertyChange(PropertyChangeEvent evt) {
		// only care about our selection key!
		if(!CorinaAction.CORINA_SELECTED_KEY.equals(evt.getPropertyName()))
			return;
		
		// ignore any infinite-loop causing events (uuurgh!)
		if(currentlyProcessingAction)
			return;
		
		Boolean selected = (Boolean) evt.getNewValue();
		
		// only handle the selected action, so we can deselect everything else
		if(!selected)
			return;
		
		currentlyProcessingAction = true;
		// deselect everything that's not the caller
		try {
			for(CorinaAction action : actions) {
				if(!action.equals(evt.getSource()))
					action.putValue(CorinaAction.CORINA_SELECTED_KEY, false);
			}
		} finally {
			// make sure this gets called even if things go fishy above
			currentlyProcessingAction = false;
		}
	}
}
