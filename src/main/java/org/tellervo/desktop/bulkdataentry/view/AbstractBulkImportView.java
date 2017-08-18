/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
 * Created on Jul 22, 2010, 2:15:56 AM
 */
package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.tellervo.desktop.tridasv2.doc.Documentation;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.TableColumnExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.AddRowEvent;
import org.tellervo.desktop.bulkdataentry.control.CopyRowEvent;
import org.tellervo.desktop.bulkdataentry.control.CopySelectedRowsEvent;
import org.tellervo.desktop.bulkdataentry.control.DeleteODKInstancesEvent;
import org.tellervo.desktop.bulkdataentry.control.DeleteRowEvent;
import org.tellervo.desktop.bulkdataentry.control.DisplayColumnChooserEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.control.RemoveSelectedEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportTableModel;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.tridasv2.NumberThenStringComparator;
import org.tellervo.desktop.tridasv2.doc.Documentation;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.JTableRowHeader;
import org.tellervo.desktop.util.JTableSpreadsheetAdapter;

import net.miginfocom.swing.MigLayout;

import javax.swing.JSplitPane;
import javax.swing.JTextPane;


/**
 * @author Daniel Murphy
 *
 */
public abstract class AbstractBulkImportView extends JPanel{
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(AbstractBulkImportView.class);

	protected IBulkImportSectionModel model;
	protected JXTable table;
	private JButton addRow;
	protected JButton showHideColumns;
	private JButton removeSelected;
	protected JButton selectAll;
	protected JButton selectNone;
	private JButton importSelected;
	protected JButton copyRow;
	protected JButton populateFromDB;
	protected JButton populateFromGeonames;
	protected JButton btnCopy;
	protected JButton btnPaste;
	protected JButton btnPasteAppend;
	protected JButton btnDeleteODKInstances;
	protected JButton btnODKImport;

	protected JPopupMenu tablePopupMenu;
	private JMenuItem addrow;
	private JMenuItem duplicateRow;
	private JMenuItem deleterow;
	private JMenuItem copy;
	private JMenuItem paste;
	private JMenuItem pasteAppend;
	private JTableSpreadsheetAdapter adapter;
	
	// Used to track column reordering
	private boolean dragComplete = false;
	private int columnValue = -1; 
	private int columnNewValue = -1;
	private JSplitPane splitPane;
	private JPanel panelHelp;
	private JTextPane txtHelpText;
	
	public AbstractBulkImportView(IBulkImportSectionModel argModel){
		model = argModel;
		initPopupMenu();
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
		addRowPressed();
	}

	private void initComponents(){
		
		btnCopy = new JButton();
		btnCopy.setIcon(Builder.getIcon("editcopy.png", 22));
		btnCopy.setToolTipText("Copy selection");
		btnPaste = new JButton();
		btnPaste.setIcon(Builder.getIcon("editpaste.png", 22));
		btnPaste.setToolTipText("Paste");
		btnPasteAppend = new JButton();
		btnPasteAppend.setIcon(Builder.getIcon("editpasteappend.png", 22));
		btnPasteAppend.setToolTipText("Paste append to new rows");

		btnDeleteODKInstances = new JButton("");
		btnDeleteODKInstances.setIcon(Builder.getIcon("odk-delete.png", 22));
		btnDeleteODKInstances.setToolTipText("Delete ODK form data from server");
		
		addRow = new JButton();
		copyRow = new JButton();
		showHideColumns = new JButton();
		

		
	
		
		removeSelected = new JButton();
		selectAll = new JButton();
		selectNone = new JButton();
		importSelected = new JButton();
		populateFromDB = new JButton();
		populateFromGeonames = new JButton();
		btnODKImport = new JButton();
		
		btnODKImport.setIcon(Builder.getIcon("odk-logo.png", 22));
		btnODKImport.setToolTipText("Import ODK data from server");
		
		importSelected.putClientProperty("JButton.buttonType", "bevel");
		importSelected.setIcon(Builder.getIcon("importtodatabase.png", 22));
		setLayout(new MigLayout("", "[450px,grow]", "[38px][107.00,grow][32px]"));
		
	
		add(setupToolbar(btnCopy, btnPaste, btnPasteAppend, addRow, removeSelected, copyRow, showHideColumns, populateFromDB, populateFromGeonames, btnDeleteODKInstances, btnODKImport), "cell 0 0,growx,aligny top");
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.9);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, "cell 0 1,grow");
		table = new JXTable();
		
		//table.setCellSelectionEnabled(true);
		table.setColumnControlVisible(true);
		
		ColumnControlButton btn = new ColumnControlButton(table);
		table.setColumnControl(btn);
		showHideColumns.setAction(btn.getAction());
		
		
		table.getColumnModel().addColumnModelListener(new TableColumnModelListener(){

			@Override
			public void columnAdded(TableColumnModelEvent e) {
	

			}

			@Override
			public void columnRemoved(TableColumnModelEvent e) {
				
			}

			@Override
			public void columnMoved(TableColumnModelEvent e) {
				if (columnValue == -1) 
		            columnValue = e.getFromIndex(); 

		        columnNewValue = e.getToIndex(); 
		        dragComplete = true;
		   	}

			@Override
			public void columnMarginChanged(ChangeEvent e) {
			}

			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {	
				
				setHelpText();
				
			}
			
		});
		
		table.getTableHeader().addMouseListener(new MouseAdapter(){
			
			@Override
            public void mouseReleased(MouseEvent e) {
                	
				
				// Make sure the first two columns are not moved		
				if (columnValue != -1 && (columnValue <=1 || columnNewValue <=1)){ 
					table.moveColumn(columnNewValue, columnValue); 
					Alert.error("Error", "The selected and imported columns must be the first columns in the table");
				}
				columnValue = -1; 
				columnNewValue = -1; 
			
				// Save order of columns when column order changes
				if (dragComplete) {                  
                    saveColumnOrderToPrefs();
                    saveColumnWidthsToPrefs();
                }
				
				dragComplete=false;

            }
		});
		
		
		
		table.setShowGrid(true);
		table.setGridColor(Color.GRAY);

		
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		//add(setupToolbar(showHideColumns, selectAll, selectNone), "West");


		JScrollPane panelTable = new JScrollPane(table);
		splitPane.setLeftComponent(panelTable);
		panelTable.getViewport().setBackground(Color.WHITE);	
		panelTable.setPreferredSize(new Dimension(500, 400));
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true); 
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		
				
				//table.setColumnSelectionAllowed(true);
				
				// Enable copynpaste
				adapter = new JTableSpreadsheetAdapter(table);
				// editors for combo box stuff
				setupTableCells(table);
				
						JTable rowTable = new JTableRowHeader(table, tablePopupMenu);
						panelTable.setRowHeaderView(rowTable);
						panelTable.setCorner(JScrollPane.UPPER_LEFT_CORNER,
						    rowTable.getTableHeader());
						
						panelHelp = new JPanel();
						splitPane.setRightComponent(panelHelp);
						panelHelp.setLayout(new BorderLayout(0, 0));
						
						txtHelpText = new JTextPane();
						txtHelpText.setEditable(false);
						txtHelpText.setContentType("text/html");
						panelHelp.add(txtHelpText);
		
		add(setupFooterElements(selectAll, selectNone, importSelected), "cell 0 2,growx,aligny top");
		
	}
	
	protected abstract void saveColumnWidthsToPrefs();
	
	protected abstract void saveColumnOrderToPrefs();
	
	protected abstract void restoreColumnOrderFromPrefs();
	
	protected abstract void restoreColumnWidthsFromPrefs();
	
	private void linkModel() {
		table.setModel(model.getTableModel());
		
		TableRowSorter sorter = new TableRowSorter();
		table.setRowSorter(sorter);
		sorter.setModel(table.getModel());
		
		NumberThenStringComparator comparator = new NumberThenStringComparator();
		
		for(int c=0; c<model.getTableModel().getColumnCount(); c++)
		{
			sorter.setComparator(c, comparator);
		}

		setUnhideableColumns();
		restoreColumnOrderFromPrefs();		
		restoreColumnWidthsFromPrefs();
		
	}
	
	/**
	 * Call setHideable() for all mandatory columns 
	 */
	public abstract void setUnhideableColumns();
	
	protected void addListeners() {
		showHideColumns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//showHideColumnsPressed();
			}
		});
		
		addRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				addRowPressed();
			}
		});
		
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				copyPressed();
			}
		});
		
		btnPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				pastePressed();
			}
		});
		
		btnPasteAppend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				pasteAppendPressed();
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
		
		populateFromDB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				populateFromDatabase();
				
			}
		});
		
		populateFromGeonames.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				populateFromGeonames();
				
			}
		});
		
		btnDeleteODKInstances.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteODKInstancesEvent event = new DeleteODKInstancesEvent();
				event.dispatch();
				
			}
			
		});
		
		btnODKImport.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ObjectModel omodel = BulkImportModel.getInstance().getObjectModel();
				ElementModel emodel = BulkImportModel.getInstance().getElementModel();
				SampleModel smodel = BulkImportModel.getInstance().getSampleModel();
				
				PopulateFromODKFileEvent event = new PopulateFromODKFileEvent(omodel, emodel, smodel);
				event.dispatch();
				
			}
			
		});
		
		table.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent arg0) {
				mousePopupHandler(arg0);
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				mousePopupHandler(arg0);
			}
			@Override
			public void mouseClicked(MouseEvent evt) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}

			/**
			 * Handles windows and mac style popup events
			 * @param evt
			 */
			private void mousePopupHandler(MouseEvent evt)
			{
				// Right click
			    if (evt.isPopupTrigger())
				{		    	
			    	// Select the whole row
			    	//table.setRowSelectionInterval(table.rowAtPoint(evt.getPoint()), table.rowAtPoint(evt.getPoint()));
			    	//table.setColumnSelectionInterval(0, table.getColumnCount()-1);

			    	// Show popup
					tablePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY()); 
				}
			}
			
		});
		
		table.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {

				if(table.getSelectedColumn()==table.getColumnCount()-1 && 
				   table.getSelectedRow()==table.getRowCount()-1)
				{
					if(e.isShiftDown())
					{
						return;
					}
					
					// In last column of last row and tab or enter have been pressed
					if(e.getKeyCode()==KeyEvent.VK_TAB || e.getKeyCode()==KeyEvent.VK_ENTER)
					{
						// Add a new row
						addRowPressed();
						
						// Oh man... this is just plain nasty.  Send thread to sleep
						// for 1/10th sec to make sure the row is finished adding before
						// we continue.  Yeah yeah I know... I'm going to programmer hell.
						try {
							Thread.currentThread();
							Thread.sleep(100);
						} catch (InterruptedException e1) {
						}
						
						// Move selection to the first cell of new row
						table.changeSelection(table.getRowCount()-1, 0, false, false);
						table.requestFocus();
					}
				}
				
				if(!table.isEditing())
				{					
					if(e.getKeyCode()==KeyEvent.VK_DELETE || e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
					{
						table.getModel().setValueAt(null, table.getSelectedRow(), table.getSelectedColumn());						
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
		addrow.setIcon(Builder.getIcon("insertrow.png", 22));
		
		// Duplicate row 
		duplicateRow = new JMenuItem(I18n.getText("bulkimport.copyrow")); 
		duplicateRow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				copyRowPressed();	
			}
		}); 
		duplicateRow.setIcon(Builder.getIcon("copyrow.png", 22));
		
		
		// Delete row 
		deleterow = new JMenuItem(I18n.getText("bulkimport.deleterow")); 
		deleterow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteRowPressed();	
			}
		}); 
		deleterow.setIcon(Builder.getIcon("deleterow.png", 22));

		
		// Copy
		copy = new JMenuItem(I18n.getText("menus.edit.copy"));
		copy.setActionCommand("Copy");
		copy.setIcon(Builder.getIcon("editcopy.png", 22));
		copy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				log.debug("about to call adapter.doCopy");
				adapter.doCopy();	
			}
			
		});
		
		// Paste
		paste = new JMenuItem(I18n.getText("menus.edit.paste"));
		paste.setActionCommand("Paste");
		paste.setIcon(Builder.getIcon("editpaste.png", 22));
		paste.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				log.debug("about to call adapter.doPaste");
				adapter.doPaste();	
			}
			
		});
		
		pasteAppend = new JMenuItem(I18n.getText("menus.edit.pasteappend"));
		pasteAppend.setActionCommand("Paste append");
		pasteAppend.setIcon(Builder.getIcon("editpaste.png", 22));
		pasteAppend.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				adapter.doPasteAppend();
				
			/*	log.debug("1. Table row count = " +table.getRowCount());
				Integer originalRowCount = table.getRowCount();
				Integer rowCountToPaste = adapter.getRowCountFromClipboard();
				
				if(rowCountToPaste!=null && rowCountToPaste>0)
				{
					log.debug(rowCountToPaste +" rows about to be pasted");

					AddRowEvent event = new AddRowEvent(model, rowCountToPaste);
					event.dispatch();
					
				}
			
				// Oooo this is so naughty... 
				/*while (table.getRowCount()< originalRowCount+rowCountToPaste){
				    //log.debug("Waiting... row count: "+table.getRowCount());
				}*/
				
				/*log.debug("2. Table row count = " +table.getRowCount());
				
				try{
					table.setRowSelectionInterval(originalRowCount, originalRowCount);
					table.setColumnSelectionInterval(0, 0);
				} catch (IllegalArgumentException ex)
				{
					log.error(ex.getLocalizedMessage());
				}
				
				adapter.doPaste();	*/
			}
			
		});
		
		
		
		tablePopupMenu.add(copy);
		tablePopupMenu.add(paste);
		tablePopupMenu.add(pasteAppend);
		tablePopupMenu.addSeparator();
		
		tablePopupMenu.add(addrow);
		tablePopupMenu.add(duplicateRow); 
		tablePopupMenu.add(deleterow); 
		tablePopupMenu.setLightWeightPopupEnabled(false);
	}
	
	protected JToolBar setupToolbar(JButton argCopyButton, JButton argPasteButton, JButton argPasteAppendButton, JButton argAddRowButton, JButton argDeleteRowButton, 
			JButton argCopyRowButton, JButton argShowHideColumnButton, JButton argPopulateFromDB, JButton argPopulateFromGeonames, JButton argDeleteODKInstances, JButton argODKImport){
		
		 JToolBar toolbar = new JToolBar();
		 
		 toolbar.add(argCopyButton);
		 toolbar.add(argPasteButton);
		 toolbar.add(argPasteAppendButton);
		 toolbar.add(argCopyRowButton);
		 toolbar.add(argAddRowButton);
		 toolbar.add(argDeleteRowButton);
		 toolbar.add(argDeleteODKInstances);
		 toolbar.add(argShowHideColumnButton);
		 toolbar.add(argPopulateFromDB);
		 toolbar.add(argPopulateFromGeonames);

		 
		 
		 return toolbar;
		/*
		Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add(argCopyButton);
		box.add( Box.createHorizontalGlue());
		box.add(argShowHideColumnButton);
		
		return box;*/
	}
		
	protected Box setupFooterElements(JButton argSelectAll, JButton argSelectNone, JButton argImport){
		Box box = Box.createHorizontalBox();
		//box.add(argSelectAll);
		//box.add(argSelectNone);
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
	
	protected abstract void populateFromDatabase();
	
	protected abstract void populateFromGeonames();
	
	
	protected void copyPressed(){
		adapter.doCopy();
	}
	
	protected void pastePressed()
	{
		adapter.doPaste();
	}
			
	protected void pasteAppendPressed(){
		adapter.doPasteAppend();
	}
		
	
	protected void addRowPressed(){
		AddRowEvent event = new AddRowEvent(model);
		event.dispatch();
	}
	
	protected void deleteRowPressed(){
		DeleteRowEvent event = new DeleteRowEvent(model, table.getSelectedRows());
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
		addRow.setToolTipText(I18n.getText("bulkimport.addrow"));
		addRow.setIcon(Builder.getIcon("insertrow.png", 22));
		
		copyRow.setToolTipText(I18n.getText("bulkimport.copyrows"));
		copyRow.setIcon(Builder.getIcon("copyrow.png", 22));
		
		showHideColumns.setToolTipText(I18n.getText("bulkimport.showHideCols"));
		showHideColumns.setIcon(Builder.getIcon("showcolumns.png", 22));
		
		removeSelected.setToolTipText(I18n.getText("bulkimport.deleteroworrows"));
		removeSelected.setIcon(Builder.getIcon("deleterow.png", 22));
		
		selectAll.setToolTipText(I18n.getText("dbbrowser.selectAll"));
		selectAll.setIcon(Builder.getIcon("selectall.png", 22));
		
		selectNone.setToolTipText(I18n.getText("dbbrowser.selectNone"));
		selectNone.setIcon(Builder.getIcon("selectnone.png", 22));

		populateFromDB.setToolTipText("Populate table from database");
		populateFromDB.setIcon(Builder.getIcon("database.png", 22));
		
		populateFromGeonames.setToolTipText("Populate country and town from Geonames");
		populateFromGeonames.setIcon(Builder.getIcon("georef.png", 22));
		
		importSelected.setText(I18n.getText("bulkimport.importselected"));
		
		
	}
	
	protected void restoreColumnOrderFromArray(ArrayList<String> prefs)
	{
		int i=2;
		int colsmoved=0;
		
		if(prefs==null || prefs.size()==0)
		{
			return;
		}
		
		
		
		for(String pref : prefs)
		{
			if(pref.equals("Imported") || pref.equals("Selected")) continue;
			
			
			/*log.debug("Current column (n="+table.getColumnCount(true)+") order is:");
			for(int k=0; k<table.getColumnCount(true); k++)
			{
				TableColumn columnplain = table.getColumns(true).get(k);
				TableColumnExt column = table.getColumnExt(columnplain.getIdentifier());
				
				if(column.isVisible())
				{
					log.debug(k+" - "+table.getColumnExt(k).getIdentifier().toString());
				}
				else
				{
					log.debug(k+" - "+table.getColumnExt(k).getIdentifier().toString()+" [hidden]");
				}			
			}*/
			
			TableColumnExt columnext = table.getColumnExt(pref);
			
			if(columnext==null)
			{
				log.error("Column specified in prefs does not exist.  Ignoring");
			}
			else
			{
			
				columnext.setVisible(true);
				
				int currentIndex = columnext.getModelIndex()+colsmoved;
				
				//log.debug("Column name   = "+pref);
				//log.debug("Current index = "+currentIndex);
				//log.debug("New index     = "+i);
				
				
				if(currentIndex!=i) {
					
					//log.debug("Moving column from "+currentIndex+" to "+i);

					if(currentIndex>table.getColumnCount())
					{
						//log.debug("Index out of range.  Not moving");
					
					}
					else
					{
						
						try{
							table.moveColumn(currentIndex, i);
							colsmoved++;
						} catch (Exception e)
						{
							e.printStackTrace();
							log.error("Error moving column position");
						}
					}
				}
				i++;
			}
			
		}
		

		
		//log.debug("Column status before hiding others (n="+table.getColumnCount()+") order is:");
		for(int k=0; k<table.getColumns(true).size(); k++)
		{
			TableColumn c = table.getColumns(true).get(k);
		
			/*if(table.getColumnExt(c.getIdentifier()).isVisible())
			{
				log.debug(k+" - "+c.getIdentifier().toString());
			}
			else
			{
				log.debug(k+" - "+c.getIdentifier().toString()+" [hidden]");
			}*/
		}
		
		
		// Hide any other columns
		for(int k=2; k<table.getColumns(true).size(); k++)
		{
			TableColumn c = table.getColumns(true).get(k);
			
			TableColumnExt column = table.getColumnExt(c.getIdentifier());
			
			if(!prefs.contains(column.getIdentifier()))
			{
				//log.debug("Hiding column "+column.getHeaderValue());
				column.setVisible(false);
			}
		}
		

		
		/*log.debug("Columns after restoring prefs and hiding remainder (n="+table.getColumnCount()+") order is:");
		for(int k=0; k<table.getColumns(true).size(); k++)
		{
			TableColumn c = table.getColumns(true).get(k);
		
			if(table.getColumnExt(c.getIdentifier()).isVisible())
			{
				log.debug(k+" - "+c.getIdentifier().toString());
			}
			else
			{
				log.debug(k+" - "+c.getIdentifier().toString()+" [hidden]");
			}
		}*/
		

	}
	
	private void setHelpText()
	{
		log.debug("Setting help text");
		TableColumnExt column = table.getColumnExt(table.getSelectedColumn());
				
		this.txtHelpText.setText("");
		
		HashMap<String,String> hashmap = new HashMap<String,String>();
		hashmap.put("Imported", "Whether this row has been imported into the database yet or not.  Note this <i>doesn't</i> indicate whether there are unsaved edits to a row or not.");
		hashmap.put("Selected", "Checkbox that allows you to select which rows are selected ready to be imported into the database.");
		
		
		hashmap.put("Object code", "Short code name for object, traditionally three letters");
		hashmap.put("Object Code", "Short code name for object, traditionally three letters");
		hashmap.put("Element code", "Short code name for this element, traditionally this is a numerical identifier");
		hashmap.put("Sample Code", "Short code name for this sample, traditionally this is a alphabetical identifier");
		hashmap.put("File references", "Array of references to files that contain information about this entity");
		hashmap.put("Waypoint", "Pick list of GPS waypoints.  You need to specify a GPX data file using the button on the toolbar to populate this list.");
		hashmap.put("Latitude", "Latitude in digital decimal degrees for the location of this entity.  If present, then Longitude must also be filled in.  Latitude must be between -90 and 90.");
		hashmap.put("Longitude", "Longitude in digital decimal degrees for the location of this entity.  If present, then Latitude must also be filled in.  Longitude must be between -180 and 180.");
		hashmap.put("City/Town", "City/Town where this entity was collected");
		hashmap.put("State/Province/Region", "State, Province or Region where this entity was collected");
		hashmap.put("Postal Code", "Postal or Zip code where this entity was collected");
		hashmap.put("Country", "Country where this entity was collected");
		hashmap.put("Parent Object", "When the current object is a sub-object, specify the object to which it belongs.  ");
		hashmap.put("Box", "Name of the box to which this sample belongs");
		hashmap.put("Unit", "Pick list of measurement units used for the dimensions fields");

		
		hashmap.put("Project", Documentation.getDocumentation("project"));
		hashmap.put("Location type", Documentation.getDocumentation("locationType"));
		hashmap.put("Location precision", Documentation.getDocumentation("locationPrecision"));
		hashmap.put("Location comment", Documentation.getDocumentation("locationComment"));
		hashmap.put("Address 1", Documentation.getDocumentation("address"));
		hashmap.put("Address 2", Documentation.getDocumentation("address"));
		hashmap.put("Owner", Documentation.getDocumentation("owner"));
		hashmap.put("Creator", Documentation.getDocumentation("creator"));

		hashmap.put("Type", Documentation.getDocumentation("type"));
		hashmap.put("Title", Documentation.getDocumentation("title"));
		hashmap.put("Comments", Documentation.getDocumentation("comments"));
		hashmap.put("Description", Documentation.getDocumentation("description"));
		
		hashmap.put("Shape", Documentation.getDocumentation("shape"));
		hashmap.put("Shape", Documentation.getDocumentation("shape"));
		hashmap.put("Height", Documentation.getDocumentation("height"));
		hashmap.put("Width", Documentation.getDocumentation("width"));
		hashmap.put("Depth", Documentation.getDocumentation("depth"));
		hashmap.put("Diameter", Documentation.getDocumentation("diameter"));
		hashmap.put("Authenticity", Documentation.getDocumentation("authenticity"));
		hashmap.put("Marks", Documentation.getDocumentation("marks"));
		hashmap.put("Altitude", Documentation.getDocumentation("altitude"));
		hashmap.put("Slope Angle", Documentation.getDocumentation("slope.angle"));
		hashmap.put("Slope Azimuth", Documentation.getDocumentation("slope.azimuth"));
		hashmap.put("Soil Depth", Documentation.getDocumentation("soil.depth"));
		hashmap.put("Soil Description", Documentation.getDocumentation("soil.description"));
		hashmap.put("Bedrock Description", Documentation.getDocumentation("bedrock.description"));
		hashmap.put("Sampling Date", Documentation.getDocumentation("samplingDate"));
		hashmap.put("Knots", Documentation.getDocumentation("knots"));
		hashmap.put("State", Documentation.getDocumentation("state"));
		hashmap.put("Position", Documentation.getDocumentation("position"));
		
		
		


		
		
		hashmap.put("Taxon", Documentation.getDocumentation("element.taxon"));
		
		try{
			String doc = hashmap.get(column.getHeaderValue().toString());
			
			if(doc!=null)
			{
				this.txtHelpText.setText("<html><b>"+column.getHeaderValue().toString()+"</b><br/>"+doc);
			} else
			{
				this.txtHelpText.setText("<html><b>"+column.getHeaderValue().toString()+"</b><br/>No documentation available");
			}

		} catch (Exception e)
		{
			this.txtHelpText.setText("<html><b>"+column.getHeaderValue().toString()+"</b><br/>No documentation available");

		}
	}
}
