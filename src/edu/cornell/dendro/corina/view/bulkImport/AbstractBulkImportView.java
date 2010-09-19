/**
 * Created on Jul 22, 2010, 2:15:56 AM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.cornell.dendro.corina.control.bulkImport.AddRowEvent;
import edu.cornell.dendro.corina.control.bulkImport.CopyRowEvent;
import edu.cornell.dendro.corina.control.bulkImport.DeleteRowEvent;
import edu.cornell.dendro.corina.control.bulkImport.DisplayColumnChooserEvent;
import edu.cornell.dendro.corina.control.bulkImport.RemoveSelectedEvent;
import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;
import edu.cornell.dendro.corina.model.bulkImport.IBulkImportTableModel;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

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
	protected JButton copyRow;
	protected JPopupMenu tablePopupMenu;
	
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
		copyRow = new JButton();
		showHideColumns = new JButton();
		removeSelected = new JButton();
		selectAll = new JButton();
		selectNone = new JButton();
		importSelected = new JButton();
		importSelected.putClientProperty("JButton.buttonType", "bevel");
		
		setLayout(new BorderLayout());
		
	
		add(setupHeaderElements(addRow, removeSelected, copyRow, showHideColumns), "North");
		//add(setupToolbar(showHideColumns, selectAll, selectNone), "West");

		JScrollPane panel = new JScrollPane(table);
		panel.setPreferredSize(new Dimension(500, 400));
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true); 
		// editors for combo box stuff
		setupTableCells(table);
		
		add(panel, "Center");
		
		add(setupFooterElements(selectAll, selectNone, importSelected), "South");
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
		
		copyRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				copyRowPressed();
			}
		});
		
		table.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent evt) {
				if(evt.getButton()==MouseEvent.BUTTON1)
				{
					// Left click
					if (evt.getClickCount()>1)
					{
						addRowPressed();
					}
				}
				else if (evt.getButton()==MouseEvent.BUTTON3)
				{
					// Right click
					initPopupMenu();
					
					if(tablePopupMenu!=null)
					{
						tablePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY()); 
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
	}
	
	protected void initPopupMenu()
	{
		if(table.getRowCount()==0 || table.getSelectedRows().length==0)
		{
			tablePopupMenu = null;
			return;
		}
		
		tablePopupMenu = new JPopupMenu(); 
		
		// Add row 
		JMenuItem addrow = new JMenuItem(I18n.getText("bulkimport.addrow")); 
		addrow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addRowPressed();	
			}
			
		}); 
		tablePopupMenu.add(addrow); 
		
		// Copy row 
		JMenuItem copyrow = new JMenuItem(I18n.getText("bulkimport.copyrow")); 
		copyrow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				copyRowPressed();	
			}
			
		}); 
		tablePopupMenu.add(copyrow); 
		
		// Delete row 
		JMenuItem deleterow = new JMenuItem(I18n.getText("bulkimport.deleterow")); 
		deleterow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteRowPressed();	
			}
			
		}); 
		tablePopupMenu.addSeparator();
		tablePopupMenu.add(deleterow); 
	}
	
	protected Box setupHeaderElements(JButton argAddRowButton, JButton argDeleteRowButton, 
			JButton argCopyButton, JButton argShowHideColumnButton){
		Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add(argCopyButton);
		box.add( Box.createHorizontalGlue());
		box.add(argShowHideColumnButton);
		
		return box;
	}
	
	protected Box setupFooterElements(JButton argSelectAll, JButton argSelectNone, JButton argImport){
		Box box = Box.createHorizontalBox();
		box.add(argSelectAll);
		box.add(argSelectNone);
		box.add(Box.createHorizontalGlue());
		box.add(argImport);
		return box;
	}
		
	protected abstract void setupTableCells(JTable argTable);
	
	protected void removeSelectedPressed(){
		
		// Confirm user is happy to continue
		int response;
		
		
		
		if(model.getTableModel().getSelectedCount()==0)
		{
			return;
		}
		else if (model.getTableModel().getSelectedCount()==1)
		{
			response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected row?");
		}
		else
		{
			response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the "
					+ model.getTableModel().getSelectedCount()
					+ " selected rows?");
		}
			
		if(response==JOptionPane.YES_OPTION)
		{
			RemoveSelectedEvent event = new RemoveSelectedEvent(model);
			event.dispatch();
		}
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
	
	protected void deleteRowPressed(){
		DeleteRowEvent event = new DeleteRowEvent(model, table.getSelectedRow());
		event.dispatch();
	}
	
	protected void showHideColumnsPressed(){
		DisplayColumnChooserEvent event = new DisplayColumnChooserEvent(model, showHideColumns);
		event.dispatch();
	}
	
	protected void copyRowPressed(){
		CopyRowEvent event = new CopyRowEvent(model, table.getSelectedRow());
		event.dispatch();
	}

	private void populateLocale() {
		addRow.setText(I18n.getText("bulkimport.addrow"));
		addRow.setIcon(Builder.getIcon("insertrow.png", 22));
		
		copyRow.setText(I18n.getText("bulkimport.copyrow"));
		copyRow.setIcon(Builder.getIcon("copyrow.png", 22));
		
		showHideColumns.setToolTipText(I18n.getText("bulkimport.showHideCols"));
		showHideColumns.setIcon(Builder.getIcon("showcolumns.png", 22));
		
		removeSelected.setText(I18n.getText("bulkimport.deleteroworrows"));
		removeSelected.setIcon(Builder.getIcon("deleterow.png", 22));
		
		selectAll.setToolTipText(I18n.getText("dbbrowser.selectAll"));
		selectAll.setIcon(Builder.getIcon("selectall.png", 22));
		
		selectNone.setToolTipText(I18n.getText("dbbrowser.selectNone"));
		selectNone.setIcon(Builder.getIcon("selectnone.png", 22));

		importSelected.setText(I18n.getText("bulkimport.importselected"));
	}
}