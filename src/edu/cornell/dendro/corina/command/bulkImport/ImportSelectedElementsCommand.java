/**
 * Created at Aug 20, 2010, 6:27:07 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.components.table.DynamicJComboBoxEvent;
import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementTableModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleElementModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleElementModel;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

/**
 * @author Daniel
 *
 */
public class ImportSelectedElementsCommand implements ICommand {
	
	
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		BulkImportModel model = BulkImportModel.getInstance();
		ElementModel emodel = model.getElementModel();
		ElementTableModel tmodel = emodel.getTableModel();
		
		ArrayList<SingleElementModel> selected = new ArrayList<SingleElementModel>();
		tmodel.getSelected(selected);
		
		// here is where we verify they contain required info
		HashSet<String> requiredMessages = new HashSet<String>();
		ArrayList<SingleElementModel> incompleteModels = new ArrayList<SingleElementModel>();
		
		HashSet<String> definedProps = new HashSet<String>();
		for(SingleElementModel som : selected){
			
			definedProps.clear();
			for(String s : SingleElementModel.TABLE_PROPERTIES){
				if(som.getProperty(s) != null){
					definedProps.add(s);
				}
			}
			boolean incomplete = false;
			
			// object 
			if(!definedProps.contains(SingleElementModel.OBJECT)){
				requiredMessages.add("Cannot import without a parent object.");
				incomplete = true;
			}
			
			// type
			if(!definedProps.contains(SingleElementModel.TYPE)){
				requiredMessages.add("Element must contain type.");
				incomplete = true;
			}else{
				boolean found = false;
				String type = som.getProperty(SingleElementModel.TYPE).toString();
				for(ControlledVoc voc: Dictionary.getDictionaryAsArrayList("elementTypeDictionary").toArray(new ControlledVoc[0])){
					if(type.equals(voc.getNormal())){
						som.setProperty(SingleElementModel.TYPE, voc);
						found = true;
						break;
					}
				}
				if(!found){
					System.err.println("Couldn't find element type '"+type+"' in element type dictionary.");
					requiredMessages.add("Error with finding element type '"+type+"', please report bug.");
					incomplete = true;
				}
			}
			
			// title
			if(!definedProps.contains(SingleElementModel.TITLE)){
				requiredMessages.add("Element must have a title");
				incomplete = true;
			}
			
			// lat/long
			if(definedProps.contains(SingleElementModel.LATITUDE) || definedProps.contains(SingleElementModel.LONGTITUDE)){
				if(!definedProps.contains(SingleElementModel.LATITUDE) || !definedProps.contains(SingleElementModel.LONGTITUDE)){
					requiredMessages.add("Element cannot have either a latitude or a longtitude.  Both or none must be provided");
					incomplete = true;
				}else{
					String attempt = som.getProperty(SingleElementModel.LATITUDE).toString().trim();
					try{
						Double.parseDouble(attempt);
					}catch(NumberFormatException e){
						requiredMessages.add("Cannot parse '"+attempt+"' into a number.");
						incomplete = true;
					}
					attempt = som.getProperty(SingleElementModel.LONGTITUDE).toString().trim();
					try{
						Double.parseDouble(attempt);
					}catch(NumberFormatException e){
						requiredMessages.add("Cannot parse '"+attempt+"' into a number.");
						incomplete = true;
					}
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
		for(SingleElementModel som : selected){
			TridasElement origElement = new TridasElement();
			
			if(!som.isDirty()){
				System.out.println("Object isn't dirty, not saving/updating: "+som.getProperty(SingleElementModel.TITLE).toString());
			}
			
			som.populateFromTridasElement(origElement);
			EntityResource<TridasElement> resource;
//			if(origObject.getIdentifier() != null){
//				resource = new EntityResource<TridasElementEx>(origObject, CorinaRequestType.UPDATE, TridasElementEx.class);
//			}else{
//				resource = new EntityResource<TridasElementEx>(origObject, CorinaRequestType.CREATE, TridasElementEx.class);
//			}
//			
//			// set up a dialog...
//			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
//			CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);
//			
//			resource.query();
//			dialog.setVisible(true);
//			
//			if(!dialog.isSuccessful()) { 
//				Alert.message("Error", "Error creating/updating object, check logs.");
//				return;
//			}
//			som.populateFromTridasElement(resource.getAssociatedResult());
//			som.setImported(resource.getAssociatedResult().getIdentifier());
//			som.setDirty(false);
//			tmodel.setSelected(som, false);
			
			// add to imported list or update existing
//			if(origObject.getIdentifier() != null){
//				TridasElementEx found = null;
//				for(TridasElementEx tox : model.getObjectModel().getImportedList()){
//					if(tox.getIdentifier().getValue().equals(origObject.getIdentifier().getValue())){
//						found = tox;
//						break;
//					}
//				}
//				if(found == null){
//					Alert.error("Error updating model", "Couldn't find the object in the model to update, please report bug.");
//				}else{
//					resource.getAssociatedResult().copyTo(found);
//				}
//			}
//			else{
//				model.getObjectModel().getImportedList().add(resource.getAssociatedResult());
//			}
		}
		
		// finally, update the combo boxes in the table to the new options
		String[] items = new String[model.getObjectModel().getImportedList().size()];
		for(int i=0; i<items.length; i++){
			items[i] = model.getObjectModel().getImportedList().get(i).getLabCode();
		}
		DynamicJComboBoxEvent event = new DynamicJComboBoxEvent(BulkImportController.SET_DYNAMIC_COMBO_BOX, items);
		event.dispatch();
	}
}
