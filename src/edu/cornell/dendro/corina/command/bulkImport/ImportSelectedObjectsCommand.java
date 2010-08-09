/**
 * Created at Aug 8, 2010, 10:21:28 PM
 */
package edu.cornell.dendro.corina.command.bulkImport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectTableModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleObjectModel;

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
		
		// verify they contain required info
		//what is  the required info?
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
			// here is where we check for properties
			if(!definedProps.contains(SingleObjectModel.OBJECT_CODE)){
				requiredMessages.add("Cannot import without an object code.");
				incomplete = true;
			}// else if 
			
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
		
		// now import somehow... all objects to save are in the "selected" arraylist.
	}
}
