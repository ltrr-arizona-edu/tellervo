/**
 * Created on Jul 22, 2010, 5:20:52 PM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ColumnChooserModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleObjectModel;

/**
 * @author Daniel Murphy
 *
 */
public class BulkImportWindow extends JFrame {
	
	private JTabbedPane tabs;
	private ObjectView objects;
	
	public BulkImportWindow() {
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
		pack();
	}
	
	public void initComponents() {
		tabs = new JTabbedPane();
		
		objects = new ObjectView(BulkImportModel.getInstance().getObjectModel());
		
		add(tabs,"Center");
		tabs.add("Objects", objects);
	}
	
	public void linkModel() {

	}
	
	public void addListeners() {

	}
	
	public void populateLocale() {

	}
	
	public static void main(String[] args) {
		CorinaModelLocator locator = CorinaModelLocator.getInstance();
		ObjectModel model = BulkImportModel.getInstance().getObjectModel();
		SingleObjectModel smodel = new SingleObjectModel();
		smodel.setProperty(SingleObjectModel.TITLE, "Test title");
		smodel.setProperty(SingleObjectModel.COMMENTS, "Some comments right here");
		smodel.setProperty(SingleObjectModel.DESCRIPTION, "My description");
		smodel.setProperty(SingleObjectModel.OBJECT_CODE, "OBJECT CODE 2");
		smodel.setProperty(SingleObjectModel.OBJECT_CODE, "OBJECT CODE");
		smodel.setProperty(SingleObjectModel.TYPE, "my type");
		
		ColumnChooserModel cmodel = (ColumnChooserModel) model.getProperty(ObjectModel.COLUMN_MODEL);
		cmodel.add(SingleObjectModel.OBJECT_CODE);
		cmodel.add(SingleObjectModel.IMPORTED);

		MVCArrayList<SingleObjectModel> objects = (MVCArrayList<SingleObjectModel>) model.getProperty(ObjectModel.ROWS);
		objects.add(smodel);
		
		BulkImportWindow frame = new BulkImportWindow();
		BulkImportModel.getInstance().setMainView(frame);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
	}
}
