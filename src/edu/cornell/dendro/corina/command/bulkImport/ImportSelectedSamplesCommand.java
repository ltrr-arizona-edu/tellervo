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
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementModel;
import edu.cornell.dendro.corina.model.bulkImport.SampleModel;
import edu.cornell.dendro.corina.model.bulkImport.SampleTableModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleRadiusModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleSampleModel;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

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
		
		ArrayList<SingleSampleModel> selected = new ArrayList<SingleSampleModel>();
		tmodel.getSelected(selected);
		
		// here is where we verify they contain required info
		HashSet<String> requiredMessages = new HashSet<String>();
		ArrayList<SingleSampleModel> incompleteModels = new ArrayList<SingleSampleModel>();
		
		HashSet<String> definedProps = new HashSet<String>();
		for(SingleSampleModel som : selected){
			
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
			message.append(StringUtils.join(requiredMessages, "\n"));
			JOptionPane.showConfirmDialog(model.getMainView(), message.toString(), "Importing Errors", JOptionPane.OK_OPTION);
			return;
		}
		
		// now we actually create the models
		for(SingleSampleModel som : selected){
			TridasSample origSample = new TridasSample();
			
			if(!som.isDirty()){
				System.out.println("Object isn't dirty, not saving/updating: "+som.getProperty(SingleSampleModel.TITLE).toString());
			}
			
			som.populateToTridasSample(origSample);
			
			TridasElement parentObject = null;
			ElementModel emodel = BulkImportModel.getInstance().getElementModel();
			for(TridasElement o : emodel.getImportedList()){
				if(o.getTitle().equals(som.getProperty(SingleSampleModel.ELEMENT))){
					parentObject = o;
				}
			}
			if(parentObject == null){
				String s = "Could not find parent object locally with title: "+som.getProperty(SingleSampleModel.ELEMENT);
				System.err.println(s);
				Alert.error("Saving Error", s);
				continue;
			}
			
			EntityResource<TridasSample> resource;
			
			if(origSample.getIdentifier() != null){
				resource = new EntityResource<TridasSample>(origSample, CorinaRequestType.UPDATE, TridasSample.class);
			}else{
				resource = new EntityResource<TridasSample>(origSample, parentObject, TridasSample.class);
			}
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
			CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);
			
			resource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) { 
				Alert.message("Error", "Error creating/updating element, check logs.");
				continue;
			}
			som.populateFromTridasSample(resource.getAssociatedResult());
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
					resource.getAssociatedResult().copyTo(found);
				}
			}
			else{
				model.getSampleModel().getImportedList().add(resource.getAssociatedResult());
			}
		}
	}
}
