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
import org.tridas.schema.ControlledVoc;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.dictionary.Dictionary;
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
		for(SingleObjectModel som : selected){
			
			definedProps.clear();
			for(String s : SingleObjectModel.PROPERTIES){
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
				if(som.getProperty(SingleObjectModel.OBJECT_CODE).toString().length() < 3){
					requiredMessages.add("Object code must be at least 3 characters");
					incomplete = true;
				}
				if(som.getProperty(SingleObjectModel.OBJECT_CODE).toString().contains(" ")){
					requiredMessages.add("Object code cannot contain whitespace.");
					incomplete = true;
				}
			}
			
			// type
			if(!definedProps.contains(SingleObjectModel.TYPE)){
				requiredMessages.add("Object must contain type.");
				incomplete = true;
			}else{
				boolean found = false;
				String type = som.getProperty(SingleObjectModel.TYPE).toString();
				for(ControlledVoc voc: Dictionary.getDictionaryAsArrayList("objectTypeDictionary").toArray(new ControlledVoc[0])){
					if(type.equals(voc.getNormal())){
						som.setProperty(SingleObjectModel.TYPE, voc);
						found = true;
						break;
					}
				}
				if(!found){
					System.err.println("Couldn't find object '"+type+"' in object type dictionary.");
					requiredMessages.add("Error with finding object type '"+type+"', please report bug.");
					incomplete = true;
				}
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
				}else{
					String attempt = som.getProperty(SingleObjectModel.LATITUDE).toString().trim();
					try{
						Double.parseDouble(attempt);
					}catch(NumberFormatException e){
						requiredMessages.add("Cannot parse '"+attempt+"' into a number.");
						incomplete = true;
					}
					attempt = som.getProperty(SingleObjectModel.LONGTITUDE).toString().trim();
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
		for(SingleObjectModel som : selected){
			TridasObjectEx object = new TridasObjectEx();
			
			som.populateTridasObject(object);
			
			EntityResource<TridasObjectEx> resource = new EntityResource<TridasObjectEx>(object, CorinaRequestType.CREATE, TridasObjectEx.class);
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(model.getMainView());
			CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);
			
			resource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) { 
				Alert.message("Error", "Error creating object, check logs.");
				return;
			}
			som.populateFromTridasObject(resource.getAssociatedResult());
			som.setImported(true);
			tmodel.setSelected(som, false);
		}
	}
}
