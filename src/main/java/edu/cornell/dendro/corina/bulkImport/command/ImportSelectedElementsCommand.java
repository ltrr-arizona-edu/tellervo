/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.bulkImport.command;

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

import edu.cornell.dendro.corina.bulkImport.model.BulkImportModel;
import edu.cornell.dendro.corina.bulkImport.model.ElementModel;
import edu.cornell.dendro.corina.bulkImport.model.ElementTableModel;
import edu.cornell.dendro.corina.bulkImport.model.IBulkImportSingleRowModel;
import edu.cornell.dendro.corina.bulkImport.model.SingleElementModel;
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
		
		ArrayList<IBulkImportSingleRowModel> selected = new ArrayList<IBulkImportSingleRowModel>();
		tmodel.getSelected(selected);
		
		// here is where we verify they contain required info
		HashSet<String> requiredMessages = new HashSet<String>();
		ArrayList<IBulkImportSingleRowModel> incompleteModels = new ArrayList<IBulkImportSingleRowModel>();
		
		HashSet<String> titles = new HashSet<String>();
		HashSet<String> definedProps = new HashSet<String>();
		for(IBulkImportSingleRowModel som : selected){
			
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
			message.append(StringUtils.join(requiredMessages.toArray(), "\n"));
			JOptionPane.showConfirmDialog(model.getMainView(), message.toString(), "Importing Errors", JOptionPane.OK_OPTION);
			return;
		}
		
		// now we actually create the models
		for(IBulkImportSingleRowModel srm : selected){
			SingleElementModel som = (SingleElementModel) srm;
			TridasElement origElement = new TridasElement();
			
			if(!som.isDirty()){
				System.out.println("Element isn't dirty, not saving/updating: "+som.getProperty(SingleElementModel.TITLE).toString());
			}
			
			som.populateToTridasElement(origElement);
			
			TridasObjectEx parentObject = (TridasObjectEx) som.getProperty(SingleElementModel.OBJECT);
			
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
//		
//		// finally, update the combo boxes in the table to the new options
//		DynamicJComboBoxEvent event = new DynamicJComboBoxEvent(emodel.getImportedDynamicComboBoxKey(), emodel.getImportedListStrings());
//		event.dispatch();
	}
}
