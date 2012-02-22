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
package org.tellervo.desktop.io.command;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.io.control.ImportEntitySaveEvent;
import org.tellervo.desktop.io.control.ImportSwapEntityEvent;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow.ImportStatus;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class EntitySaveCommand implements ICommand {
	
	private final static Logger log = LoggerFactory.getLogger(EntitySaveCommand.class);

	@Override
	public void execute(MVCEvent argEvent) {
		
		try {
	        MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
		        // this means that the thread that called splitOff() was not an MVC thread, and the next event's won't be blocked anyways.
		        e.printStackTrace();
		} catch (IncorrectThreadException e) {
		        // this means that this MVC thread is not the main thread, it was already splitOff() previously
		        e.printStackTrace();
		}
		
		ImportEntitySaveEvent event = (ImportEntitySaveEvent) argEvent;
		log.debug("Compiling request to save current entity");	
		// if nothing actually changed, just ignore it
		//if(!event.model.isDirty()) return;
		
		// Remove all children as we just want the current entity for 
		// sending to the webservice
		TridasRepresentationTableTreeRow selnode = event.model.getSelectedRow();
		ITridas currEntity = selnode.getCurrentEntity();
		ITridas entity = removeChildren(currEntity);
		ITridas parentEntity = selnode.getParentEntity();	
		Class<? extends ITridas> currentEntityType = selnode.getCurrentEntityClass();

		// are we saving something new?
		boolean isNew = selnode.isCurrentEntityNew();
		
		// sanity check to ensure parent entity being null means we're an object
		if(parentEntity == null && !currentEntityType.equals(TridasObject.class)) {
			new Bug(new IllegalStateException("parentEntity is null, but not an object"));
			return;
		}
				
		// the resource we'll use
		EntityResource<? extends ITridas> resource;
		
		if(isNew)
		{	
			// This is a new entity, so scrub any existing external identifiers
			entity.setIdentifier(null);
			
			resource = getNewAccessorResource(entity, parentEntity, currentEntityType);
			resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
			log.debug("Creating new resource of type "+currentEntityType.toString());
		}
		else
		{
			resource = getUpdateAccessorResource(entity, currentEntityType);
			log.debug("Updating resource of type "+currentEntityType.toString());
		}
		
		// set up a dialog...
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(event.window, resource);

		// query the resource
		resource.query();
		dialog.setVisible(true);
		
		// on failure, just return
		if(!dialog.isSuccessful()) {
			JOptionPane.showMessageDialog(event.window, I18n.getText("error.savingChanges") + "\r\n" +
					I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
					I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		else
		{
			log.debug("Save successful");
		}
		
		// replace the saved result
		try{
		    entity = resource.getAssociatedResult();
		} catch (Exception e)
		{
			// Sanity check
			log.debug(e.getLocalizedMessage());
			new Bug(new IllegalStateException("CREATE or UPDATE entity returned null"));
			return;
		}
		
		
		// take the return value and update the model
		log.debug("Updating the GUI model with saved entity");
		selnode.setCurrentEntity(entity);
		selnode.saveChanges();
		
		
		TridasRepresentationTableTreeRow oldrow = event.model.getSelectedRow();	
		DefaultMutableTreeNode node = new DefaultMutableTreeNode((ITridas) entity);
		TridasRepresentationTableTreeRow newrow = 
			new TridasRepresentationTableTreeRow(node, ImportStatus.STORED_IN_DATABASE);
		ImportSwapEntityEvent ev = new ImportSwapEntityEvent(event.model, newrow, oldrow);
        ev.dispatch();
/*
		// if it was new, re-enable our editing
		if(isNew) {
			// stick it in the combo box list
			if(parentEntity == null) {
				// it's an object...
				// TODO: Fix new objects?
			}
			else
				lists.appendChildToList(parentEntity, temporaryEditingEntity);
			
			// repopulate the combo box...
			selectInTopChooser();
		
		}
		*/
		// on success...
		//currentMode.clearChanged();

		//populateComboAndSelect(false);
		//temporaryEditingEntity = null;
	
		 

		
	}

	
	/**
	 * Remove any children from an entity ready for sending to the webservice
	 *  
	 * @param entity
	 * @return
	 */
	private ITridas removeChildren(ITridas entity)
	{
	//	System.out.println("removing children from "+entity.getTitle());
		
		if(entity==null)
		{
			log.warn("Unable to remove children from this entity as it is null!");
			return null;
		}
		
		if(entity instanceof TridasProject)
		{
			((TridasProject) entity).setDerivedSeries(null);
			((TridasProject) entity).setObjects(null);
		}
		else if (entity instanceof TridasObject)
		{
			((TridasObject) entity).setElements(null);
			((TridasObject) entity).setObjects(null);
		}
		else if (entity instanceof TridasElement)
		{
			((TridasElement) entity).setSamples(null);
		}
		else if (entity instanceof TridasSample)
		{
			((TridasSample) entity).setRadiuses(null);
		}
		else if (entity instanceof TridasRadius)
		{
			((TridasRadius) entity).setMeasurementSeries(null);
		}		
		return entity;
	}
	
	
	/**
	 * For creating a new entity on the server
	 * 
	 * @param <T>
	 * @param entity
	 * @param parent
	 * @param type
	 * @return
	 */
	private <T extends ITridas> EntityResource<T> getNewAccessorResource(ITridas entity, ITridas parent, Class<T> type) {
		return new EntityResource<T>(entity, parent, type);
	}

	/**
	 * For updating an existing entity on the server
	 * 
	 * @param <T>
	 * @param entity
	 * @param type
	 * @return
	 */
	private <T extends ITridas> EntityResource<T> getUpdateAccessorResource(ITridas entity, Class<T> type) {
		return new EntityResource<T>(entity, TellervoRequestType.UPDATE, type);
	}
	
}
