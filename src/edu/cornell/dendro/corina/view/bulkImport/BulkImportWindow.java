/**
 * Created on Jul 22, 2010, 5:20:52 PM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.ui.Builder;

/**
 * @author Daniel Murphy
 *
 */
public class BulkImportWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabs;
	private ObjectView objects;
	private ElementView elements;
	private SampleView samples;
	
	public BulkImportWindow() {
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
		pack();
		this.setLocationRelativeTo(null);
	}
	
	public void initComponents() {
		tabs = new JTabbedPane();
		
		objects = new ObjectView(BulkImportModel.getInstance().getObjectModel());
		elements = new ElementView(BulkImportModel.getInstance().getElementModel());
		samples = new SampleView(BulkImportModel.getInstance().getSampleModel());
		add(tabs,"Center");
		tabs.addTab("Objects", Builder.getIcon("object.png", 22), objects);
		tabs.addTab("Elements", Builder.getIcon("element.png", 22), elements);
		tabs.addTab("Samples", Builder.getIcon("sample.png", 22), samples);
	}
	
	public void linkModel() {

	}
	
	public void addListeners() {

	}
	
	public void populateLocale() {

	}
	
	public static void main()	
	{
		CorinaModelLocator.getInstance();
		MVCEvent event = new MVCEvent(BulkImportController.DISPLAY_BULK_IMPORT);
		event.dispatch();
	}
}
