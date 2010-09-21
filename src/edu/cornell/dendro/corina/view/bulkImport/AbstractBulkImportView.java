/**
 * Created on Jul 22, 2010, 2:15:56 AM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.cornell.dendro.corina.control.bulkImport.AddRowEvent;
import edu.cornell.dendro.corina.control.bulkImport.CopyRowEvent;
import edu.cornell.dendro.corina.control.bulkImport.CopySelectedRowsEvent;
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
	private JMenuItem addrow;
	private JMenuItem copyrow;
	private JMenuItem deleterow;

	
	public AbstractBulkImportView(IBulkImportSectionModel argModel){
		model = argModel;
		initComponents();
		initPopupMenu();
		linkModel();
		addListeners();
		populateLocale();
		addRowPressed();
	}

	private void initComponents(){
		table = new JTable();
		table.setCellSelectionEnabled(true);
		
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
				copySelectedPressed();
			}
		});
		
		table.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent evt) {
				
				// Right click
			    if (evt.getButton()==MouseEvent.BUTTON3)
				{		    	
			    	// Select the whole row
			    	table.setRowSelectionInterval(table.rowAtPoint(evt.getPoint()), table.rowAtPoint(evt.getPoint()));
			    	table.setColumnSelectionInterval(0, table.getColumnCount()-1);

			    	// Show popup
					tablePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY()); 
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
		
		table.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {

				if(table.getSelectedColumn()==table.getColumnCount()-1 && 
				   table.getSelectedRow()==table.getRowCount()-1)
				{
					// In last column of last row and tab or enter have been pressed
					if(e.getKeyCode()==KeyEvent.VK_TAB || e.getKeyCode()==KeyEvent.VK_ENTER)
					{
						// Add a new row
						addRowPressed();
						
						// Move selection to the first cell of new row
						table.changeSelection(table.getRowCount()-1, 0, false, false);
						table.requestFocus();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
			
		});
		
	}
	
	private void initPopupMenu()
	{
		/*if(table.getRowCount()==0 || table.getSelectedRows().length==0)
		{
			tablePopupMenu = null;
			return;
		}*/
		
		tablePopupMenu = new JPopupMenu(); 
		
		// Add row 
		addrow = new JMenuItem(I18n.getText("bulkimport.addrow")); 
		addrow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addRowPressed();	
			}
			
		}); 
		tablePopupMenu.add(addrow); 
		
		// Copy row 
		copyrow = new JMenuItem(I18n.getText("bulkimport.copyrow")); 
		copyrow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				copyRowPressed();	
			}
			
		}); 
		tablePopupMenu.add(copyrow); 
		
		// Delete row 
		deleterow = new JMenuItem(I18n.getText("bulkimport.deleterow")); 
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
	
	protected void deleteRowPressed(){
		DeleteRowEvent event = new DeleteRowEvent(model, table.getSelectedRow());
		event.dispatch();
	}
	
	protected void showHideColumnsPressed(){
		DisplayColumnChooserEvent event = new DisplayColumnChooserEvent(model, showHideColumns);
		event.dispatch();
	}
	
	protected void copySelectedPressed(){
		CopySelectedRowsEvent event = new CopySelectedRowsEvent(model);
		event.dispatch();
	}
	
	protected void copyRowPressed(){
		CopyRowEvent event = new CopyRowEvent(model, table.getSelectedRow());
		event.dispatch();
	}

	private void populateLocale() {
		addRow.setText(I18n.getText("bulkimport.addrow"));
		addRow.setIcon(Builder.getIcon("insertrow.png", 22));
		
		copyRow.setText(I18n.getText("bulkimport.copyrows"));
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