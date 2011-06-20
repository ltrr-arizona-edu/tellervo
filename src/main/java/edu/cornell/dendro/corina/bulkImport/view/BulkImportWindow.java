/**
 * Created on Jul 22, 2010, 5:20:52 PM
 */
package edu.cornell.dendro.corina.bulkImport.view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.bulkImport.control.BulkImportController;
import edu.cornell.dendro.corina.bulkImport.model.BulkImportModel;
import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

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
		tabs.addTab(I18n.getText("tridas.object")+"s", Builder.getIcon("object.png", 22), objects);
		tabs.addTab(I18n.getText("tridas.element")+"s", Builder.getIcon("element.png", 22), elements);
		tabs.addTab(I18n.getText("tridas.sample")+"s", Builder.getIcon("sample.png", 22), samples);
		
		setIconImage(Builder.getApplicationIcon());
	}
	
	public void linkModel() {

	}
	
	public void addListeners() {

	}
	
	public void populateLocale() {
		this.setTitle(I18n.getText("menus.file.bulkimport"));
	}
	
	public static void main()	
	{
		CorinaModelLocator.getInstance();
		MVCEvent event = new MVCEvent(BulkImportController.DISPLAY_BULK_IMPORT);
		event.dispatch();
	}
}
