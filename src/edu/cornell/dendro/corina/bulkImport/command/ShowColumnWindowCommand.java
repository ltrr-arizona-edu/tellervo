/**
s * Created at Jul 24, 2010, 3:48:12 PM
 */
package edu.cornell.dendro.corina.bulkImport.command;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.bulkImport.control.DisplayColumnChooserEvent;
import edu.cornell.dendro.corina.bulkImport.model.BulkImportModel;
import edu.cornell.dendro.corina.bulkImport.model.ColumnChooserModel;
import edu.cornell.dendro.corina.bulkImport.model.IBulkImportSingleRowModel;
import edu.cornell.dendro.corina.bulkImport.view.ColumnChooserView;

/**
 * @author daniel
 *
 */
public class ShowColumnWindowCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		DisplayColumnChooserEvent event = (DisplayColumnChooserEvent) argEvent;
		
		BulkImportModel biModel = BulkImportModel.getInstance();
		
		if(biModel.getCurrColumnChooser() != null){
			
			if(biModel.getCurrColumnChooser().isVisible())
			{
				biModel.getCurrColumnChooser().setVisible(false);
				return;
			}
			else
			{
				biModel.setCurrColumnChooser(null);
			}
		}
	
		// give it the possible columns
		ColumnChooserModel model = event.model.getColumnModel();
		model.poplutePossibleColumns(event.model.getModelTableProperties());
		
		
		// remove any columns in the model that aren't possible columns
		for(String s : model){
			if(!model.getPossibleColumns().contains(s)){
				if(!s.equals(IBulkImportSingleRowModel.IMPORTED)){
					model.remove(s);	
				}
			}
		}
		
		ColumnChooserView view = new ColumnChooserView(model, biModel.getMainView(), event.locationComponent);
		biModel.setCurrColumnChooser(view);
		
		try {
			MVC.splitOff();
		}catch (IllegalThreadException e) { e.printStackTrace();}
		catch (IncorrectThreadException e) { e.printStackTrace();}
		
		view.pack();
		view.setVisible(true);
	}
	
}
