/**
 * Created on Jul 22, 2010, 2:15:56 AM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.cornell.dendro.corina.control.bulkImport.AddRowEvent;
import edu.cornell.dendro.corina.control.bulkImport.DisplayColumnChooserEvent;
import edu.cornell.dendro.corina.control.bulkImport.RemoveSelectedEvent;
import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;
import edu.cornell.dendro.corina.model.bulkImport.IBulkImportTableModel;
import edu.cornell.dendro.corina.ui.Builder;

/**
 * @author Daniel Murphy
 *
 */
public abstract class AbstractBulkImportView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	protected IBulkImportSectionModel model;
	protected JTable table;
	private JButton addRow;
	protected JButton showHideColumns;
	private JButton removeSelected;
	private JButton selectAll;
	private JButton selectNone;
	private JButton importSelected;
	
	public AbstractBulkImportView(IBulkImportSectionModel argModel){
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
		
	
		add(setupHeaderElements(addRow, removeSelected, showHideColumns, selectAll, selectNone), "North");
		//add(setupToolbar(showHideColumns, selectAll, selectNone), "West");

		JScrollPane panel = new JScrollPane(table);
		panel.setPreferredSize(new Dimension(500, 400));
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true); 
		// editors for combo box stuff
		setupTableCells(table);
		
		add(panel, "Center");
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(importSelected);
		add(box, "South");
	}
	
	private void linkModel() {
		table.setModel(model.getTableModel());
	}
	
	protected void addListeners() {
		showHideColumns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHideColumnsPressed();
			}
		});
		
		addRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				addRowPressed();
			}
		});
		
		importSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				importSelectedPressed();
			}
		});
		
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				selectAllPressed();
			}
		});
		
		selectNone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				selectNonePressed();
			}
		});
		
		removeSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				removeSelectedPressed();
			}
		});
	}
	
	protected Box setupHeaderElements(JButton argAddRowButton, JButton argDeleteRowButton, JButton argShowHideColumnButton,
			JButton argSelectAll, JButton argSelectNone){
		Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add( Box.createHorizontalGlue());
		box.add(argShowHideColumnButton);
		box.add(argSelectAll);
		box.add(argSelectNone);
		return box;
	}
	
	/*protected Box setupToolbar(JButton argShowHideColumnButton, JButton selectAll, JButton selectNone)
	{
		Box box = Box.createVerticalBox();
		box.add(argShowHideColumnButton);
		box.add(selectAll);
		box.add(selectNone);
		return box;
	}*/
	
	protected abstract void setupTableCells(JTable argTable);
	
	
	
	protected void removeSelectedPressed(){
		RemoveSelectedEvent event = new RemoveSelectedEvent(model);
		event.dispatch();
	}
	
	protected void selectAllPressed(){
		IBulkImportTableModel tmodel =  model.getTableModel();
		tmodel.selectAll();
	}
	
	protected void selectNonePressed(){
		IBulkImportTableModel tmodel =  model.getTableModel();
		tmodel.selectNone();
	}
	
	protected abstract void importSelectedPressed();
	
	protected void addRowPressed(){
		AddRowEvent event = new AddRowEvent(model);
		event.dispatch();
	}
	
	protected void showHideColumnsPressed(){
		DisplayColumnChooserEvent event = new DisplayColumnChooserEvent(model);
		event.dispatch();
	}

	private void populateLocale() {
		addRow.setText("Add Row");
		addRow.setIcon(Builder.getIcon("insertrow.png", 22));
		
		showHideColumns.setToolTipText("Show/Hide Columns");
		showHideColumns.setIcon(Builder.getIcon("showcolumns.png", 22));
		
		removeSelected.setText("Delete Row(s)");
		removeSelected.setIcon(Builder.getIcon("deleterow.png", 22));
		
		selectAll.setToolTipText("Select All");
		selectAll.setIcon(Builder.getIcon("selectall.png", 22));
		
		selectNone.setToolTipText("Select None");
		selectNone.setIcon(Builder.getIcon("selectnone.png", 22));

		importSelected.setText("Import Selected");
	}
}