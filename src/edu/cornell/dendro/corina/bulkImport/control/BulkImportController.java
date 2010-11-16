/**
 * Created at Jul 24, 2010, 3:38:04 PM
 */
package edu.cornell.dendro.corina.bulkImport.control;

import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;
import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

import edu.cornell.dendro.corina.bulkImport.command.AddRowCommand;
import edu.cornell.dendro.corina.bulkImport.command.CopyRowCommand;
import edu.cornell.dendro.corina.bulkImport.command.CopySelectedRowsCommand;
import edu.cornell.dendro.corina.bulkImport.command.DeleteRowCommand;
import edu.cornell.dendro.corina.bulkImport.command.GPXBrowseCommand;
import edu.cornell.dendro.corina.bulkImport.command.HideColumnWindowCommand;
import edu.cornell.dendro.corina.bulkImport.command.ImportSelectedElementsCommand;
import edu.cornell.dendro.corina.bulkImport.command.ImportSelectedObjectsCommand;
import edu.cornell.dendro.corina.bulkImport.command.ImportSelectedSamplesCommand;
import edu.cornell.dendro.corina.bulkImport.command.PrintBarcodesCommand;
import edu.cornell.dendro.corina.bulkImport.command.RemoveSelectedCommand;
import edu.cornell.dendro.corina.bulkImport.command.ShowColumnWindowCommand;
import edu.cornell.dendro.corina.bulkImport.model.BulkImportModel;
import edu.cornell.dendro.corina.bulkImport.model.ColumnChooserModel;
import edu.cornell.dendro.corina.bulkImport.model.SingleElementModel;
import edu.cornell.dendro.corina.bulkImport.model.SingleObjectModel;
import edu.cornell.dendro.corina.bulkImport.model.SingleRadiusModel;
import edu.cornell.dendro.corina.bulkImport.model.SingleSampleModel;
import edu.cornell.dendro.corina.bulkImport.view.BulkImportWindow;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.schema.WSIRequest.Dictionaries;

/**
 * @author daniel
 *
 */
public class BulkImportController extends FrontController {
	public static final String DISPLAY_COLUMN_CHOOSER = "BULK_IMPORT_DISPLAY_COLUMN_CHOOSER";
	public static final String HIDE_COLUMN_CHOOSER = "BULK_IMPORT_HIDE_COLUMN_CHOOSER";
	public static final String ADD_ROW = "BULK_IMPORT_ADD_ROW";
	public static final String COPY_ROW = "BULK_IMPORT_COPY_ROW";
	public static final String DELETE_ROW = "BULK_IMPORT_DELETE_ROW";
	public static final String DISPLAY_BULK_IMPORT = "BULK_IMPORT_DISPLAY_BULK_IMPORT";
	public static final String REMOVE_SELECTED = "BULK_IMPORT_REMOVE_SELECTED_ROW";
	public static final String COPY_SELECTED_ROWS = "BULK_IMPORT_COPY_ALL_SELECTED_ROWS";
	
	public static final String IMPORT_SELECTED_OBJECTS = "BULK_IMPORT_SELECTED_OBJECTS";
	public static final String IMPORT_SELECTED_ELEMENTS = "BULK_IMPORT_SELECTED_ELEMENTS";
	public static final String IMPORT_SELECTED_SAMPLES = "BULK_IMPORT_SELECTED_SAMPLES";
	
	public static final String SET_DYNAMIC_COMBO_BOX_OBJECTS = "BULK_IMPORT_SET_DYNAMIC_COMBO_BOX_OBJECTS";
	public static final String SET_DYNAMIC_COMBO_BOX_ELEMENTS = "BULK_IMPORT_SET_DYNAMIC_COMBO_BOX_ELEMENTS";
	public static final String SET_DYNAMIC_COMBO_BOX_SAMPLES = "BULK_IMPORT_SET_DYNAMIC_COMBO_BOX_SAMPLES";
	
	public static final String BROWSE_GPX_FILE = "BULK_IMPORT_BROWSE_GPX_FILE";
	public static final String PRINT_SAMPLE_BARCODES = "BULK_IMPORT_PRINT_BARCODES";

	
	public BulkImportController(){
		registerCommand(DISPLAY_COLUMN_CHOOSER, ShowColumnWindowCommand.class);
		registerCommand(IMPORT_SELECTED_OBJECTS, ImportSelectedObjectsCommand.class);
		registerCommand(HIDE_COLUMN_CHOOSER, HideColumnWindowCommand.class);
		registerCommand(ADD_ROW, AddRowCommand.class);
		registerCommand(COPY_ROW, CopyRowCommand.class);
		registerCommand(COPY_SELECTED_ROWS, CopySelectedRowsCommand.class);
		registerCommand(DELETE_ROW, DeleteRowCommand.class);
		registerCommand(REMOVE_SELECTED, RemoveSelectedCommand.class);
		registerCommand(DISPLAY_BULK_IMPORT, "display");
		registerCommand(IMPORT_SELECTED_ELEMENTS, ImportSelectedElementsCommand.class);
		registerCommand(IMPORT_SELECTED_SAMPLES, ImportSelectedSamplesCommand.class);
		registerCommand(BROWSE_GPX_FILE, GPXBrowseCommand.class);
		registerCommand(PRINT_SAMPLE_BARCODES, PrintBarcodesCommand.class);
	}
	
	public void display(MVCEvent argEvent){
		if(MVC.getTracker() == null){
			JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(new AnalyticsConfigData("UA-17109202-7"), GoogleAnalyticsVersion.V_4_7_2);
			MVC.setTracker(tracker);
		}else{
			MVC.getTracker().resetSession();
		}
		//MVC.showEventMonitor();
		
		if(BulkImportModel.getInstance().getMainView() != null){
			BulkImportWindow window = BulkImportModel.getInstance().getMainView();
			window.setVisible(true);
			window.toFront();
			return;
		}
		
		BulkImportModel model = BulkImportModel.getInstance();
		populateObjectDefaults(model.getObjectModel().getColumnModel());
		populateElementDefaults(model.getElementModel().getColumnModel());
		populateSampleDefaults(model.getSampleModel().getColumnModel());
		
		// just put inot the sample model for now, change if we want to add extra radius functionality
		populateRadiusDefaults(model.getSampleModel().getColumnModel());
		
		BulkImportWindow frame = new BulkImportWindow();
		BulkImportModel.getInstance().setMainView(frame);
		frame.pack();
		frame.setVisible(true);
		//MVC.showEventMonitor();
	}
	
	private void populateObjectDefaults(ColumnChooserModel ccmodel){
		ccmodel.add(SingleObjectModel.OBJECT_CODE);
		ccmodel.add(SingleObjectModel.TITLE);
		ccmodel.add(SingleObjectModel.TYPE);
		ccmodel.add(SingleObjectModel.IMPORTED);
	}
	
	private void populateElementDefaults(ColumnChooserModel ccmodel){
		ccmodel.add(SingleElementModel.OBJECT);
		ccmodel.add(SingleElementModel.TITLE);
		ccmodel.add(SingleElementModel.TYPE);
		ccmodel.add(SingleElementModel.TAXON);
		ccmodel.add(SingleElementModel.IMPORTED);
	}
	
	private void populateSampleDefaults(ColumnChooserModel ccmodel){
		ccmodel.add(SingleSampleModel.OBJECT);
		ccmodel.add(SingleSampleModel.ELEMENT);
		ccmodel.add(SingleSampleModel.TITLE);
		ccmodel.add(SingleSampleModel.TYPE);
		ccmodel.add(SingleSampleModel.BOX);
	}
	
	private void populateRadiusDefaults(ColumnChooserModel ccmodel){
		ccmodel.add(SingleElementModel.IMPORTED);
	}
	
	public static void main() {
		CorinaModelLocator.getInstance();
		MVCEvent event = new MVCEvent(DISPLAY_BULK_IMPORT);
		event.dispatch();
	}
}
