/**
 * Created at Aug 8, 2010, 10:21:28 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.components.table.DynamicJComboBoxEvent;
import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectTableModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleObjectModel;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

/**
 * @author daniel
 *
 */
public class ImportSelectedObjectsCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		BulkImportModel model = BulkImportModel.getInstance();
		
		ObjectTableModel tmodel = model.getObjectModel().getTableModel();
		ArrayList<SingleObjectModel> selected = new ArrayList<SingleObjectModel>();
		tmodel.getSelected(selected);
		
		// here is where we verify they contain required info
		HashSet<String> requiredMessages = new HashSet<String>();
		ArrayList<SingleObjectModel> incompleteModels = new ArrayList<SingleObjectModel>();
		
		HashSet<String> definedProps = new HashSet<String>();
		HashSet<String> objectCodeSet = new HashSet<String>();
		for(SingleObjectModel som : selected){
			
			definedProps.clear();
			for(String s : SingleObjectModel.TABLE_PROPERTIES){
				if(som.getProperty(s) != null){
					definedProps.add(s);
				}
			}
			boolean incomplete = false;
			
			// object code
			if(!definedProps.contains(SingleObjectModel.OBJECT_CODE)){
				requiredMessages.add("Cannot import without an object code.");
				incomplete = true;
			} else{
				String code = som.getProperty(SingleObjectModel.OBJECT_CODE).toString();
				if(code.length() < 3){
					requiredMessages.add("Object code must be at least 3 characters");
					incomplete = true;
				}
				if(code.contains(" ")){
					requiredMessages.add("Object code cannot contain whitespace.");
					incomplete = true;
				}
				if(objectCodeSet.contains(code)){
					requiredMessages.add("There cannot be duplicate object codes.");
					incomplete = true;
				}else{
					objectCodeSet.add(code);
				}
			}
			
			// type
			if(!definedProps.contains(SingleObjectModel.TYPE)){
				requiredMessages.add("Object must contain type.");
				incomplete = true;
			}
			
			// title
			if(!definedProps.contains(SingleObjectModel.TITLE)){
				requiredMessages.add("Object must have a title");
				incomplete = true;
			}
			
			// lat/long
			if(definedProps.contains(SingleObjectModel.LATITUDE) || definedProps.contains(SingleObjectModel.LONGTITUDE)){
				if(!definedProps.contains(SingleObjectModel.LATITUDE) || !definedProps.contains(SingleObjectModel.LONGTITUDE)){
					requiredMessages.add("Object cannot have either a latitude or a longtitude.  Both or none must be provided");
					incomplete = true;
				}
			}
			
			
			if(incomplete){
				incompleteModels.add(som);
			}
		}
		
		if(!incompleteModels.isEmpty()){
			StringBuilder message = new StringBuilder();
			message.append("Please correct the following errors:\n");
			message.append(StringUtils.join(requiredMessages, "\n"));
			JOptionPane.showConfirmDialog(model.getMainView(), message.toString());
			return;
		}
		
		// now we actually create the models
		for(SingleObjectModel som : selected){
			TridasObjectEx origObject = new TridasObjectEx();
			
			if(!som.isDirty()){
				System.out.println("Object isn't dirty, not saving/updating: "+som.getProperty(SingleObjectModel.OBJECT_CODE).toString());
			}
			
			som.populateTridasObject(origObject);
			EntityResource<TridasObjectEx> resource;
			if(origObject.getIdentifier() != null){
				resource = new EntityResource<TridasObjectEx>(origObject, CorinaRequestType.UPDATE, TridasObjectEx.class);
			}else{
				resource = new EntityResource<TridasObjectEx>(origObject, CorinaRequestType.CREATE, TridasObjectEx.class);
			}
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
			CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);
			
			resource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) { 
				Alert.message("Error", "Error creating/updating object, check logs.");
				continue;
			}
			som.populateFromTridasObject(resource.getAssociatedResult());
			som.setDirty(false);
			tmodel.setSelected(som, false);
			
			// add to imported list or update existing
			if(origObject.getIdentifier() != null){
				TridasObjectEx found = null;
				for(TridasObjectEx tox : model.getObjectModel().getImportedList()){
					if(tox.getIdentifier().getValue().equals(origObject.getIdentifier().getValue())){
						found = tox;
						break;
					}
				}
				if(found == null){
					Alert.error("Error updating model", "Couldn't find the object in the model to update, please report bug.");
				}else{
					resource.getAssociatedResult().copyTo(found);
				}
			}
			else{
				model.getObjectModel().getImportedList().add(resource.getAssociatedResult());
			}
		}
		
		// finally, update the combo boxes in the table to the new options
		String[] items = new String[model.getObjectModel().getImportedList().size()];
		for(int i=0; i<items.length; i++){
			items[i] = model.getObjectModel().getImportedList().get(i).getLabCode();
		}
		DynamicJComboBoxEvent event = new DynamicJComboBoxEvent(BulkImportController.SET_DYNAMIC_COMBO_BOX_OBJECTS, items);
		event.dispatch();
	}
}
