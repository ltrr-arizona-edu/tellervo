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
package org.tellervo.desktop.bulkdataentry.command;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.ElementTableModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.TridasObjectOrPlaceholder;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;


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
			else if (fixTempObjectCode(som))
			{
				// There was a temp code but it is fixed now
			}
			else
			{
				requiredMessages.add("Cannot import as parent object has not been created yet");
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
			}
			
			// lat/long
			if(definedProps.contains(SingleElementModel.LATITUDE) || definedProps.contains(SingleElementModel.LONGITUDE)){
				if(!definedProps.contains(SingleElementModel.LATITUDE) || !definedProps.contains(SingleElementModel.LONGITUDE)){
					requiredMessages.add("If coordinates are specified then both latitude and longitude are required");
					incomplete = true;
				}else{
					String attempt = som.getProperty(SingleElementModel.LATITUDE).toString().trim();
					try{
						Double.parseDouble(attempt);
					}catch(NumberFormatException e){
						requiredMessages.add("Cannot parse '"+attempt+"' into a number.");
						incomplete = true;
					}
					attempt = som.getProperty(SingleElementModel.LONGITUDE).toString().trim();
					try{
						Double.parseDouble(attempt);
					}catch(NumberFormatException e){
						requiredMessages.add("Cannot parse '"+attempt+"' into a number.");
						incomplete = true;
					}
				}
			}
			
			if(definedProps.contains(SingleElementModel.HEIGHT) || 
					definedProps.contains(SingleElementModel.WIDTH) || 
					definedProps.contains(SingleElementModel.DEPTH) || 
					definedProps.contains(SingleElementModel.DIAMETER))
			{
				if (!definedProps.contains(SingleElementModel.UNIT))
				{
					requiredMessages.add("Units must be specified when dimensions are included");
					incomplete = true;
				}
				
				if ((definedProps.contains(SingleElementModel.HEIGHT) 
						&& definedProps.contains(SingleElementModel.DIAMETER) 
						&& !definedProps.contains(SingleElementModel.WIDTH) 
						&& !definedProps.contains(SingleElementModel.DEPTH)))
				{
					// h+diam but not width or depth
				}
				else if ((definedProps.contains(SingleElementModel.HEIGHT) 
						&& definedProps.contains(SingleElementModel.WIDTH) 
						&& definedProps.contains(SingleElementModel.DEPTH) 
						&& !definedProps.contains(SingleElementModel.DIAMETER)))
				{
					// h+w+d but not diam
				}
				else
				{
					requiredMessages.add("When dimensions are included they must be: height/width/depth or height/diameter.");
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
			message.append(StringUtils.join(requiredMessages.toArray(), "\n"));
			JOptionPane.showMessageDialog(model.getMainView(), message.toString(), "Importing Errors", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// now we actually create the models
		int i=-1;
		boolean hideErrorMessage=false;
		for(IBulkImportSingleRowModel srm : selected)
		{
			i++;
			SingleElementModel som = (SingleElementModel) srm;
			TridasElement origElement = new TridasElement();
			
			if(!som.isDirty()){
				System.out.println("Element isn't dirty, not saving/updating: "+som.getProperty(SingleElementModel.TITLE).toString());
			}
			
			som.populateToTridasElement(origElement);
			
			TridasObject parentObject = ((TridasObjectOrPlaceholder) som.getProperty(SingleElementModel.OBJECT)).getTridasObject();
			
			EntityResource<TridasElement> resource;
			
			if(origElement.getIdentifier() != null){
				resource = new EntityResource<TridasElement>(origElement, TellervoRequestType.UPDATE, TridasElement.class);
			}else{
				resource = new EntityResource<TridasElement>(origElement, parentObject, TridasElement.class);
			}
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
			TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(parentWindow, resource, i, selected.size());
			
			resource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) { 
				
				if(hideErrorMessage)
				{
					continue;
				}
				else if(i<selected.size()-1)
				{
					// More records remain
					Object[] options = {"Yes",
							"Yes, but hide further messages",
		                    "No"};
					int result = JOptionPane.showOptionDialog(BulkImportModel.getInstance().getMainView(), //parent
							I18n.getText("error.savingChanges") + ":" +System.lineSeparator()+ // message
							dialog.getFailException().getLocalizedMessage() + System.lineSeparator()+ System.lineSeparator()+
							"Would you like to continue importing the remaining records?",
							I18n.getText("error"), // title 
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.ERROR_MESSAGE,
							null,
							options,
							options[0]);
					
					if(result==JOptionPane.NO_OPTION)
					{
						hideErrorMessage=true;
						continue;
					}
					else if ( result==JOptionPane.YES_OPTION)
					{
						continue;
					}
					else
					{
						break;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), //parent
							I18n.getText("error.savingChanges")+":" +System.lineSeparator()+ // message
							dialog.getFailException().getLocalizedMessage(),
							I18n.getText("error"), // title 
							JOptionPane.ERROR_MESSAGE); //option
					
					break;
				}
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
		
		tmodel.fireTableDataChanged();
	}
	
	/**
	 * Check for temporary object code and convert to proper object.  If object has not been created yet, return false.
	 * 
	 * @param som
	 * @return
	 */
	private boolean fixTempObjectCode(IBulkImportSingleRowModel som)
	{
	
		Object rowitem = (Object) som.getProperty(SingleElementModel.OBJECT);
		
		if(rowitem instanceof TridasObjectOrPlaceholder)
		{
			if(((TridasObjectOrPlaceholder) rowitem).getTridasObject()!=null) return true;
			
			MVCArrayList<TridasObjectEx> objlist = App.tridasObjects.getMutableObjectList();

			for(TridasObjectEx o : objlist)
			{
				if(o.getLabCode().equals(((TridasObjectOrPlaceholder) rowitem).getCode())) 
				{
					som.setProperty(SingleElementModel.OBJECT, new TridasObjectOrPlaceholder(o));
					return true;
				}
			}
					
		}
		else if (rowitem instanceof String)
		{
			MVCArrayList<TridasObjectEx> objlist = App.tridasObjects.getMutableObjectList();

			for(TridasObjectEx o : objlist)
			{
				if(o.getLabCode().equals(rowitem)) 
				{
					som.setProperty(SingleElementModel.OBJECT, new TridasObjectOrPlaceholder(o));
					return true;
				}
			}
		}
		else if (rowitem instanceof TridasObjectEx)
		{
			return true;
		}
		
		
		return false;
	}
	
}
