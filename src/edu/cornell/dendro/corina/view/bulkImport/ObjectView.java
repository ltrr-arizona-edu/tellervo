/**
 * Created on Jul 22, 2010, 2:15:56 AM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.dmurph.mvc.util.MVCArrayList;

import edu.cornell.dendro.corina.control.bulkImport.DisplayColumnChooserEvent;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectTableModel;
import edu.cornell.dendro.corina.model.bulkImport.SingleObjectModel;

/**
 * @author Daniel Murphy
 *
 */
public class ObjectView extends JPanel{
	
	private ObjectModel model;

	private JTable table;
	private JButton addRow;
	private JButton showHideColumns;
	private JButton removeSelected;
	private JButton selectAll;
	private JButton selectNone;
	private JButton importSelected;
	
	public ObjectView(ObjectModel argModel){
		model = argModel;
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
	}

	private void initComponents(){
		table = new JTable();
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
		table.setModel((TableModel) model.getProperty(ObjectModel.TABLE_MODEL));
	}
	
	private void addListeners() {
		showHideColumns.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DisplayColumnChooserEvent event = new DisplayColumnChooserEvent(model);
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