/**
 * Created on Jul 22, 2010, 5:20:52 PM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.dmurph.mvc.util.MVCArrayList;

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
		
		ObjectModel model = new ObjectModel();
		objects = new ObjectView(model);
		
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
		ObjectModel model = new ObjectModel();
		SingleObjectModel smodel = new SingleObjectModel();
		smodel.setProperty(SingleObjectModel.TITLE, "TEST");
		MVCArrayList<SingleObjectModel> objects = (MVCArrayList<SingleObjectModel>) model.getProperty(ObjectModel.OBJECTS);
		objects.add(smodel);
		
		JFrame frame = new JFrame();
		frame.add(new ObjectView(model));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
	}
}
