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
import org.tridas.schema.TridasElement;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.components.table.DynamicJComboBoxEvent;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementTableModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleElementModel;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
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
		
		HashSet<String> titles = new HashSet<String>();
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
				requiredMessages.add("Element must contain a type.");
				incomplete = true;
			}
			
			// taxon
			if(!definedProps.contains(SingleElementModel.TAXON)){
				requiredMessages.add("Element must contain a taxon.");
				incomplete = true;
			}
			
			// title
			if(!definedProps.contains(SingleElementModel.TITLE)){
				requiredMessages.add("Element must have a title");
				incomplete = true;
			}else{
				String title = (String) som.getProperty(SingleElementModel.TITLE);
				if(titles.contains(title)){
					requiredMessages.add("Elements cannot have duplicate titles '"+title+"'.");
					incomplete = true;
				}else{
					titles.add(title);
				}
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
			JOptionPane.showConfirmDialog(model.getMainView(), message.toString(), "Importing Errors", JOptionPane.OK_OPTION);
			return;
		}
		
		// now we actually create the models
		for(SingleElementModel som : selected){
			TridasElement origElement = new TridasElement();
			
			if(!som.isDirty()){
				System.out.println("Object isn't dirty, not saving/updating: "+som.getProperty(SingleElementModel.TITLE).toString());
			}
			
			som.populateToTridasElement(origElement);
			
			TridasObjectEx parentObject = null;
			ObjectModel omodel = BulkImportModel.getInstance().getObjectModel();
			for(TridasObjectEx o : omodel.getImportedList()){
				if(o.getLabCode().equals(som.getProperty(SingleElementModel.OBJECT))){
					parentObject = o;
				}
			}
			if(parentObject == null){
				String s = "Could not find parent object locally with code: "+som.getProperty(SingleElementModel.OBJECT);
				System.err.println(s);
				Alert.error("Saving Error", s);
				continue;
			}
			
			EntityResource<TridasElement> resource;
			
			if(origElement.getIdentifier() != null){
				resource = new EntityResource<TridasElement>(origElement, CorinaRequestType.UPDATE, TridasElement.class);
			}else{
				resource = new EntityResource<TridasElement>(origElement, parentObject, TridasElement.class);
			}
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
			CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);
			
			resource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) { 
				JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), I18n.getText("error.savingChanges") + "\r\n" +
						I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
						I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
				continue;
			}
			som.populateFromTridasElement(resource.getAssociatedResult());
			som.setDirty(false);
			tmodel.setSelected(som, false);
			
			// add to imported list or update existing
			if(origElement.getIdentifier() != null){
				TridasElement found = null;
				for(TridasElement tox : model.getElementModel().getImportedList()){
					if(tox.getIdentifier().getValue().equals(origElement.getIdentifier().getValue())){
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
				model.getElementModel().getImportedList().add(resource.getAssociatedResult());
			}
		}
		
		// finally, update the combo boxes in the table to the new options
		DynamicJComboBoxEvent event = new DynamicJComboBoxEvent(emodel.getImportedDynamicComboBoxKey(), emodel.getImportedListStrings());
		event.dispatch();
	}
}
