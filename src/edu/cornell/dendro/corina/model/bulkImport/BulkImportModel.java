/**
 * Created at Jul 24, 2010, 3:40:48 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.AbstractModel;

import edu.cornell.dendro.corina.view.bulkImport.BulkImportWindow;
import edu.cornell.dendro.corina.view.bulkImport.ColumnChooserView;

/**
 * @author daniel
 *
 */
public class BulkImportModel {
	private static BulkImportModel model = null;
	
	private final ObjectModel objectModel;
	private ColumnChooserView currColumnChooser = null;
	private BulkImportWindow mainView;
	
	private BulkImportModel(){
		objectModel = new ObjectModel();
	}
	
	public ObjectModel getObjectModel(){
		return objectModel;
	}

	public void setCurrColumnChooser(ColumnChooserView currColumnChooser) {
		this.currColumnChooser = currColumnChooser;
	}

	public ColumnChooserView getCurrColumnChooser() {
		return currColumnChooser;
	}

	public void setMainView(BulkImportWindow mainView) {
		this.mainView = mainView;
	}

	public BulkImportWindow getMainView() {
		return mainView;
	}

	public static BulkImportModel getInstance(){
		if(model == null){
			model = new BulkImportModel();
		}
		return model;
	}
}
