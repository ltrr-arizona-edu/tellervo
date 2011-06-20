package edu.cornell.dendro.corina.io.command;

import javax.swing.JOptionPane;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.io.control.ImportEntitySaveEvent;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

public class EntitySaveCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		ImportEntitySaveEvent event = (ImportEntitySaveEvent) argEvent;
			
		// if nothing actually changed, just ignore it
		if(!event.model.isDirty()) return;
		
		// Remove all children as we just want the current entity for 
		// sending to the webservice
		ITridas entity = removeChildren(event.model.getSelectedNode().model.getCurrentEntity());
		ITridas parentEntity = event.model.getSelectedNode().model.getParentEntity();	
		Class<? extends ITridas> currentEntityType = event.model.getSelectedNode().model.getCurrentEntityClass();
		
		// are we saving something new?
		boolean isNew = event.model.getSelectedNode().model.isCurrentEntityNew();
		
		// sanity check to ensure parent entity being null means we're an object
		if(parentEntity == null && !currentEntityType.equals(TridasObject.class)) {
			new Bug(new IllegalStateException("parentEntity is null, but not an object"));
			return;
		}
				
		// the resource we'll use
		EntityResource<? extends ITridas> resource;
		
		if(isNew)
			resource = getNewAccessorResource(entity, parentEntity, 
					currentEntityType);
		else
			resource = getUpdateAccessorResource(entity, currentEntityType);

		// set up a dialog...
		CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(event.window, resource);

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
		
		// replace the saved result
		entity = resource.getAssociatedResult();
				
		// sanity check the result
		if(entity == null) {
			new Bug(new IllegalStateException("CREATE or UPDATE entity returned null"));
			return;
		}
		
		// take the return value and update the model
		event.model.getSelectedNode().model.setCurrentEntity(entity);
		event.model.getSelectedNode().model.saveChanges();
		
		/*
		TridasRepresentationTableTreeRow oldrow = event.model.getSelectedNode();	
		DefaultMutableTreeNode node = new DefaultMutableTreeNode((ITridas) entity);
		TridasRepresentationTableTreeRow newrow = 
			new TridasRepresentationTableTreeRow(node, ImportStatus.STORED_IN_DATABASE);
		ImportSwapEntityEvent ev = new ImportSwapEntityEvent(event.model, newrow, oldrow);
        ev.dispatch();

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
		
		// on success...
		//currentMode.clearChanged();
		editEntity.setSelected(false);
		enableEditing(false);
		//populateComboAndSelect(false);
		//temporaryEditingEntity = null;
	
		 */

		
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
		return new EntityResource<T>(entity, CorinaRequestType.UPDATE, type);
	}
	
}
