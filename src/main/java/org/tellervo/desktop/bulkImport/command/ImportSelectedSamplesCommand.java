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
package org.tellervo.desktop.bulkImport.command;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.tellervo.desktop.bulkImport.model.BulkImportModel;
import org.tellervo.desktop.bulkImport.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkImport.model.SampleModel;
import org.tellervo.desktop.bulkImport.model.SampleTableModel;
import org.tellervo.desktop.bulkImport.model.SingleRadiusModel;
import org.tellervo.desktop.bulkImport.model.SingleSampleModel;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


/**
 * @author Daniel
 *
 */
public class ImportSelectedSamplesCommand implements ICommand {
	
	
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		BulkImportModel model = BulkImportModel.getInstance();
		SampleModel smodel = model.getSampleModel();
		SampleTableModel tmodel = smodel.getTableModel();
		
		ArrayList<IBulkImportSingleRowModel> selected = new ArrayList<IBulkImportSingleRowModel>();
		tmodel.getSelected(selected);
		
		// here is where we verify they contain required info
		HashSet<String> requiredMessages = new HashSet<String>();
		ArrayList<SingleSampleModel> incompleteModels = new ArrayList<SingleSampleModel>();
		
		HashSet<String> definedProps = new HashSet<String>();
		for(IBulkImportSingleRowModel srm : selected){
			SingleSampleModel som = (SingleSampleModel) srm;
			definedProps.clear();
			for(String s : SingleSampleModel.TABLE_PROPERTIES){
				if(som.getProperty(s) != null){
					definedProps.add(s);
				}
			}
			if(smodel.isRadiusWithSample()){
				for(String s : SingleRadiusModel.PROPERTIES){
					if(som.getRadiusModel().getProperty(s) != null){
						definedProps.add(s);
					}
				}
			}
			boolean incomplete = false;
			
			// object 
			if(!definedProps.contains(SingleSampleModel.ELEMENT)){
				requiredMessages.add("Cannot import without a parent element.");
				incomplete = true;
			}
			
			// type
			if(!definedProps.contains(SingleSampleModel.TYPE)){
				requiredMessages.add("Sample must contain a type.");
				incomplete = true;
			}
			
			// title
			if(!definedProps.contains(SingleSampleModel.TITLE)){
				requiredMessages.add("Sample must have a title.");
				incomplete = true;
			}
			
			if(smodel.isRadiusWithSample()){
				if(!definedProps.contains(SingleRadiusModel.TITLE)){
					requiredMessages.add("Radius title must be populated.");
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
			JOptionPane.showConfirmDialog(model.getMainView(), message.toString(), "Importing Errors", JOptionPane.OK_OPTION);
			return;
		}
		
		// now we actually create the models
		for(IBulkImportSingleRowModel srm : selected){
			SingleSampleModel som = (SingleSampleModel) srm;
			TridasSample origSample = new TridasSample();
			
			if(!som.isDirty()){
				System.out.println("Object isn't dirty, not saving/updating: "+som.getProperty(SingleSampleModel.TITLE).toString());
			}
			
			som.populateToTridasSample(origSample);
			TridasElement parentObject = (TridasElement) som.getProperty(SingleSampleModel.ELEMENT);
			
			// sample first
			EntityResource<TridasSample> sampleResource;
			if(origSample.getIdentifier() != null){
				sampleResource = new EntityResource<TridasSample>(origSample, TellervoRequestType.UPDATE, TridasSample.class);
			}else{
				sampleResource = new EntityResource<TridasSample>(origSample, parentObject, TridasSample.class);
			}
			
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
			TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parentWindow, sampleResource);
			
			sampleResource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) { 
				JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), I18n.getText("error.savingChanges") + "\r\n" +
						I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
						I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
				continue;
			}
			som.populateFromTridasSample(sampleResource.getAssociatedResult());
			som.setDirty(false);
			tmodel.setSelected(som, false);
			
			// add to imported list or update existing
			if(origSample.getIdentifier() != null){
				TridasSample found = null;
				for(TridasSample tox : model.getSampleModel().getImportedList()){
					if(tox.getIdentifier().getValue().equals(origSample.getIdentifier().getValue())){
						found = tox;
						break;
					}
				}
				if(found == null){
					Alert.error("Error updating model", "Couldn't find the object in the model to update, please report bug.");
				}else{
					sampleResource.getAssociatedResult().copyTo(found);
				}
			}
			else{
				model.getSampleModel().getImportedList().add(sampleResource.getAssociatedResult());
			}
			
			if(som.getRadiusModel() != null){
				// now lets do the radius
				TridasRadius origRadius = new TridasRadius();
				som.getRadiusModel().populateToTridasRadius(origRadius);
				
				TridasSample parentSample = sampleResource.getAssociatedResult();
				
				// sample first
				EntityResource<TridasRadius> radiusResource;
				if(origRadius.getIdentifier() != null){
					radiusResource = new EntityResource<TridasRadius>(origRadius, TellervoRequestType.UPDATE, TridasRadius.class);
				}else{
					radiusResource = new EntityResource<TridasRadius>(origRadius, parentSample, TridasRadius.class);
				}
				
				
				// set up a dialog...
				parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
				dialog = TellervoResourceAccessDialog.forWindow(parentWindow, radiusResource);
				
				radiusResource.query();
				dialog.setVisible(true);
				
				if(!dialog.isSuccessful()) { 
					JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), I18n.getText("error.savingChanges") + "\r\n" +
							I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
							I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
					continue;
				}
				som.getRadiusModel().populateFromTridasRadius(radiusResource.getAssociatedResult());
				som.getRadiusModel().setDirty(false);
				tmodel.setSelected(som, false);
			}
		}
	}
}
