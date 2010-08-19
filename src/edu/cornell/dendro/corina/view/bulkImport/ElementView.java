/**
 * Created on Aug 18, 2010, 1:10:49 PM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.control.bulkImport.AddRowEvent;
import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.control.bulkImport.DisplayColumnChooserEvent;
import edu.cornell.dendro.corina.control.bulkImport.ImportSelectedEvent;
import edu.cornell.dendro.corina.control.bulkImport.RemoveSelectedEvent;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectTableModel;
import edu.cornell.dendro.corina.schema.WSIElementTypeDictionary;
import edu.cornell.dendro.corina.schema.WSIObjectTypeDictionary;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class ElementView extends JPanel{
	
	private ElementModel model;

	private JTable table;
	private JButton addRow;
	private JButton showHideColumns;
	private JButton removeSelected;
	private JButton selectAll;
	private JButton selectNone;
	private JButton importSelected;
	private JComboBox objectChooseBox;
	
	public ElementView(ElementModel argModel){
		model = argModel;
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
	}

	private void initComponents(){
		table = new JTable(0,3);
		addRow = new JButton();
		showHideColumns = new JButton();
		removeSelected = new JButton();
		selectAll = new JButton();
		selectNone = new JButton();
		importSelected = new JButton();
		
		setLayout(new BorderLayout());
		
		Box box = Box.createHorizontalBox();
		box.add(addRow);
		box.add( Box.createHorizontalGlue());
		box.add(showHideColumns);
		add(box, "North");

		JScrollPane panel = new JScrollPane(table);
		panel.setPreferredSize(new Dimension(500, 400));
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true); 
		
		// editors for combo box stuff
		ControlledVoc[] vocs = Dictionary.getDictionaryAsArrayList("elementTypeDictionary").toArray(new ControlledVoc[0]);
		String[] names = new String[vocs.length];
		for(int i=0; i<vocs.length; i++){
			names[i] = vocs[i].getNormal();
		}
		JComboBox typeBox = new JComboBox(names);
		table.setDefaultEditor(WSIElementTypeDictionary.class, new DefaultCellEditor(typeBox));
		
		TridasObjectEx[] objects = BulkImportModel.getInstance().getObjectModel().getImportedList().toArray(new TridasObjectEx[0]);
		String[] codes = new String[objects.length];
		for(int i=0; i<objects.length; i++){
			codes[i] = objects[i].getLabCode();
		}
		objectChooseBox = new JComboBox();
		objectChooseBox.setModel(new DefaultComboBoxModel(codes));
		table.setDefaultEditor(TridasObject.class, new DefaultCellEditor(objectChooseBox));
		add(panel, "Center");
		
		box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(selectAll);
		box.add(selectNone);
		box.add(Box.createRigidArea(new Dimension(30, 1)));
		box.add(removeSelected);
		box.add(Box.createHorizontalGlue());
		box.add(importSelected);
		add(box, "South");
	}
	
	private void linkModel() {
		table.setModel((TableModel) model.getProperty(ElementModel.TABLE_MODEL));
		
		BulkImportModel.getInstance().getObjectModel().addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				if(prop.equals(ObjectModel.IMPORTED_LIST)){
					TridasObjectEx[] objects = BulkImportModel.getInstance().getObjectModel().getImportedList().toArray(new TridasObjectEx[0]);
					DefaultComboBoxModel cbmodel = (DefaultComboBoxModel)objectChooseBox.getModel();
					cbmodel.removeAllElements();

					for(int i=0; i<objects.length; i++){
						cbmodel.addElement(objects[i].getLabCode());
					}
					table.setDefaultEditor(TridasObject.class, new DefaultCellEditor(objectChooseBox));
				}
			}
		});
//		ColumnChooserModel ccmodel = (ColumnChooserModel)model.getProperty(ElementModel.COLUMN_MODEL);
//		ccmodel.addPropertyChangeListener(new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent argEvt) {
//				TableColumn column = table.getColumnModel().getColumn(0);
//				column.setWidth(10);
//				column.setPreferredWidth(10);
//				column.setResizable(false);
//			}
//		});
	}
	
	private void addListeners() {
		showHideColumns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplayColumnChooserEvent event = new DisplayColumnChooserEvent(model);
				event.dispatch();
			}
		});
		
		addRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				AddRowEvent event = new AddRowEvent(model);
				event.dispatch();
			}
		});
		
		importSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				ImportSelectedEvent event = new ImportSelectedEvent(BulkImportController.IMPORT_SELECTED_OBJECTS);
				event.dispatch();
			}
		});
		
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				// skip controller
				ObjectTableModel tmodel = (ObjectTableModel) model.getProperty(ElementModel.TABLE_MODEL);
				tmodel.selectAll();
			}
		});
		
		selectNone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				// skip controller
				ObjectTableModel tmodel = (ObjectTableModel) model.getProperty(ElementModel.TABLE_MODEL);
				tmodel.selectNone();
			}
		});
		
		removeSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				RemoveSelectedEvent event = new RemoveSelectedEvent(model);
				event.dispatch();
			}
		});
	}

	private void populateLocale() {
		addRow.setText("Add Row");
		showHideColumns.setText("Show/Hide Columns");
		removeSelected.setText("Remove Selected");
		selectAll.setText("Select All");
		selectNone.setText("Select None");
		importSelected.setText("Import Selected");
	}
}