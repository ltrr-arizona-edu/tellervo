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
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.SampleTableModel;
import org.tellervo.desktop.bulkdataentry.model.ElementTableModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleRadiusModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.bulkdataentry.model.TridasElementOrPlaceholder;
import org.tellervo.desktop.bulkdataentry.model.TridasObjectOrPlaceholder;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Daniel
 *
 */
public class ImportSelectedSamplesCommand implements ICommand {
	
	private static final Logger log = LoggerFactory.getLogger(ImportSelectedSamplesCommand.class);

	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		BulkImportModel model = BulkImportModel.getInstance();
		SampleModel smodel = model.getSampleModel();
		SampleTableModel tmodel = smodel.getTableModel();
		
		ElementModel emodel = model.getElementModel();
		
		MVCArrayList<TridasElement> elementlist = emodel.getImportedList();
		ElementTableModel etablemodel = emodel.getTableModel();
		
		
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
			
			String currentIdentifier = "";
			
			if(som.getProperty(SingleSampleModel.OBJECT)!=null)
			{
				Object tempob = som.getProperty(SingleSampleModel.OBJECT);
				
				if(tempob instanceof TridasObjectEx)
				{
					currentIdentifier+=TridasUtils.getObjectCodeMulti((TridasObjectEx) som.getProperty(SingleSampleModel.OBJECT))+"-";
				}
				else
				{
					currentIdentifier+=som.getProperty(SingleSampleModel.OBJECT)+"-";
				}
				
				if(som.getProperty(SingleSampleModel.ELEMENT)!=null)
				{
					Object tempel = som.getProperty(SingleSampleModel.ELEMENT);
					if(tempel instanceof TridasElement)
					{
						currentIdentifier+=((TridasElement)som.getProperty(SingleSampleModel.ELEMENT)).getTitle()+"-"+som.getProperty(SingleSampleModel.TITLE);

					}
					else
					{
						currentIdentifier+=som.getProperty(SingleSampleModel.ELEMENT)+"-"+som.getProperty(SingleSampleModel.TITLE);

					}
					
				}
			}
	
			
			// object
			if(!definedProps.contains(SingleSampleModel.OBJECT)){
				requiredMessages.add("Cannot import '"+currentIdentifier+"'. Parent object is missing.");
				incomplete = true;
			}
			
			// element 
			if(!definedProps.contains(SingleSampleModel.ELEMENT)){
				requiredMessages.add("Cannot import '"+currentIdentifier+"'. Parent element is missing.");
				incomplete = true;
			}
			else if (fixTempParentCodes(som, emodel))
			{
				// There was a temp code but it is fixed now
			}
			else
			{
				requiredMessages.add("Cannot import '"+currentIdentifier+"'. Parent element has not been created yet");
				incomplete = true;
			}
		
			// type
			if(!definedProps.contains(SingleSampleModel.TYPE)){
				requiredMessages.add("Cannot import '"+currentIdentifier+"'. Sample must contain a type.");
				incomplete = true;
			}
			
			// title
			if(!definedProps.contains(SingleSampleModel.TITLE)){
				requiredMessages.add("Cannot import '"+currentIdentifier+"'. Sample must have a title.");
				incomplete = true;
			}
			
			if(smodel.isRadiusWithSample()){
				if(!definedProps.contains(SingleRadiusModel.TITLE)){
					requiredMessages.add("Cannot import '"+currentIdentifier+"'. Radius title must be populated.");
					incomplete = true;
				}
			}
			
			
			if(incomplete){
				incompleteModels.add(som);
			}
		}
		
		if(!incompleteModels.isEmpty()){
			StringBuilder message = new StringBuilder();
			if(requiredMessages.size()>5)
			{
				message.append("There were "+requiredMessages.size()+" rows that failed to import, including:\n");
				
				Iterator<String> value = requiredMessages.iterator(); 
				int i=0;
		        // Displaying the values after iterating through the set 
		        System.out.println("The iterator values are: "); 
		        while (value.hasNext()) {
		        	i++;
		        	if(i>5) break;
		        	message.append(value.next()+"\n");
		        } 
				
			}
			else
			{
				message.append("There were "+requiredMessages.size()+" rows that failed to import:\n");
				message.append(StringUtils.join(requiredMessages.toArray(), "\n"));
			}
			
			
			Alert.message(model.getMainView(), "Importing Errors", message.toString());
			return;
		}
		
		// now we actually create the models
		int i=0;
		for(IBulkImportSingleRowModel srm : selected)
		{
			SingleSampleModel ssm = (SingleSampleModel) srm;
			TridasSample origSample = new TridasSample();
			
			if(!ssm.isDirty()){
				System.out.println("Object isn't dirty, not saving/updating: "+ssm.getProperty(SingleSampleModel.TITLE).toString());
			}
			
			try {
				ssm.populateToTridasSample(origSample);
			} catch (Exception e1) {
				new BugDialog(e1);
			}
			
			Object e = ssm.getProperty(SingleSampleModel.ELEMENT);
			TridasElement parentElement = null;
			if(e instanceof TridasElementOrPlaceholder)
			{
				parentElement = ((TridasElementOrPlaceholder)e).getTridasElement();
			}
			else if (e instanceof TridasElement)
			{
				parentElement = (TridasElement) e;
			}
			
					
			// sample first
			EntityResource<TridasSample> sampleResource;
			if(origSample.getIdentifier() != null){
				sampleResource = new EntityResource<TridasSample>(origSample, TellervoRequestType.UPDATE, TridasSample.class);
			}else{
				sampleResource = new EntityResource<TridasSample>(origSample, parentElement, TridasSample.class);
			}
			sampleResource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);

			
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
			TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(parentWindow, sampleResource, i, selected.size());
			
			sampleResource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) { 
				JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), I18n.getText("error.savingChanges") + "\r\n" +
						I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
						I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
				continue;
			}
			ssm.populateFromTridasSample(sampleResource.getAssociatedResult());
			ssm.setDirty(false);
			tmodel.setSelected(ssm, false);
			
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
					//Alert.error("Error updating model", "Couldn't find the object in the model to update, please report bug.");
				}else{
					sampleResource.getAssociatedResult().copyTo(found);
				}
			}
			else{
				model.getSampleModel().getImportedList().add(sampleResource.getAssociatedResult());
			}
			
			if(ssm.getRadiusModel() != null){
				// now lets do the radius
				TridasRadius origRadius = new TridasRadius();
				
				try {
					ssm.getRadiusModel().populateToTridasRadius(origRadius);
				} catch (Exception e1) {
					new BugDialog(e1);

				}
				
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
				ssm.getRadiusModel().populateFromTridasRadius(radiusResource.getAssociatedResult());
				ssm.getRadiusModel().setDirty(false);
				tmodel.setSelected(ssm, false);
			}
			i++;
		}
	}
	
	
	/**
	 * Check for temporary element code and convert to proper element.  If element has not been created yet, return false.
	 * 
	 * @param som
	 * @return
	 */
	private boolean fixTempParentCodes(IBulkImportSingleRowModel som, ElementModel emodel)
	{
		TridasObject parentObject = null;
	
		// First get parent object for sample 
		Object objectField = (Object) som.getProperty(SingleSampleModel.OBJECT);
		boolean objectSet = false;
		if(objectField == null)
		{
			log.error("No parent object specified");
			return false;
		}
		else if(objectField instanceof TridasObjectOrPlaceholder)
		{
			if(((TridasObjectOrPlaceholder) objectField).getTridasObject()!=null) 
			{
				parentObject = ((TridasObjectOrPlaceholder) objectField).getTridasObject();
				som.setProperty(SingleSampleModel.OBJECT, objectField);
				objectSet = true;
			}
			else
			{		
				String code = ((TridasObjectOrPlaceholder) objectField).getCode();
				TridasObjectEx o = App.tridasObjects.findObjectBySiteCode(code);
				if(o!=null)	{
					
					parentObject = o;
					som.setProperty(SingleSampleModel.OBJECT, new TridasObjectOrPlaceholder(o));
					objectSet = true;
				}
			}
		}
		else if (objectField instanceof String)
		{
			MVCArrayList<TridasObjectEx> objlist = App.tridasObjects.getMutableObjectList();

			for(TridasObjectEx o : objlist)
			{
				if(o.getLabCode().equals(objectField)) 
				{
					parentObject = o;
					som.setProperty(SingleSampleModel.OBJECT, new TridasObjectOrPlaceholder(o));
					objectSet = true;
				}
			}
		}
		else if (objectField instanceof TridasObjectEx)
		{
			parentObject = (TridasObject) objectField;
			objectSet = true;
		}
		
		
		if(objectSet==false)
		{
			log.error("Unable to find object");
			return false;
		}

		
		// At this point we have a valid object in the object field.  
		// Now we need to try and get a valid Element 

		Object elementField = (Object) som.getProperty(SingleSampleModel.ELEMENT);
		String elementCode = null;
		if(elementField == null)
		{
			log.error("No parent element set");
			return false;
		}
		else if (elementField instanceof TridasElement)
		{
			// Element field is already a proper TridasElement so nothing to do!
			return true;
		}
		else if(elementField instanceof TridasElementOrPlaceholder)
		{
			if(((TridasElementOrPlaceholder) elementField).getTridasElement()!=null) 
			{
				// Element field is already a proper TridasElement so nothing to do!
				return true;
			}
			
			// element field must be a placeholder code string
			// Loop through rows of the Element Table to try and grab recently imported element
			
			elementCode = ((TridasElementOrPlaceholder) elementField).getCode();
			for(SingleElementModel elemrow : emodel.getRows())
			{
				if(elemrow.getProperty(SingleElementModel.IMPORTED)==null)
				{
					// Row is not imported so skip
					continue;
				}

				Object elementTitleField = elemrow.getProperty(SingleElementModel.TITLE);
				
				if(elementTitleField==null)
				{
					// title field is empty so
					continue;
				}
				else if(elementTitleField.equals(elementCode))
				{	
					// This row from the element table has the same element code as our sample row
					// Check the object is the same 
					
					Object elementObjectField = elemrow.getProperty(SingleElementModel.OBJECT);
		
					if(elementObjectField instanceof TridasObjectOrPlaceholder)
					{
						TridasObjectOrPlaceholder toop = (TridasObjectOrPlaceholder) elementObjectField;
						if(toop.getTridasObject()!=null) {
							// Good!
						}
						else
						{
							continue;
						}
					}
					else if (elementObjectField instanceof TridasObject)
					{	
						// Good!
					}
					else 
					{
						continue;
					}
					
					// This element must be the right one so go ahead and set it in the sample model
					
					TridasElement element = new TridasElement();
					try {
						elemrow.populateToTridasElement(element);
					} catch (Exception e1) {
						
						e1.printStackTrace();
					}
					
					som.setProperty(SingleSampleModel.ELEMENT, new TridasElementOrPlaceholder(element));
					return true;
					
				}
			}						
		}
		else if (elementField instanceof String)
		{
			elementCode = (String) elementField;
		}
		
		// This sample's parent element is not in the element table
		// Try to search from the database in case the user is entering a new sample for an element
		// that was imported some time ago.  We could do this from the start, but it is expensive
		// as it makes lots of db calls.		
		TridasElement e = searchForElement(parentObject, elementCode);
		if(e==null)
		{
			log.error("Parent element was not found in database");
			return false;
		}
		else
		{
			som.setProperty(SingleSampleModel.ELEMENT, new TridasElementOrPlaceholder(e));
			return true;
		}
		
	}
	
	private TridasElement searchForElement(TridasObject o, String elementCode)
	{
		if(o==null || elementCode == null) {
			log.error("Can't seaerch for element if object of element code are null"); 
			return null;
		}
		
		// Find all elements for an object 
    	SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
    	param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, o.getIdentifier().getValue().toString());
    	param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, elementCode);

		EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error searching database for element");
			return null;
		}
		
		List<TridasElement> elList = resource.getAssociatedResult();
		
		if(elList.size()==1)
		{
			return elList.get(0);
		}
		
		log.error("Error searching database for element.  Got "+elList.size()+" elements when searching");

		return null;		
	}
	

}
