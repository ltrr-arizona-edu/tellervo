/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnModelExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.widgets.TridasTreeViewPanel;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.PopupListener;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIBox;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


/**
 * Implementation of the database browser
 * @author peterbrewer
 *
 */
public class DBBrowser extends DBBrowser_UI implements ElementListManager, TridasSelectListener {
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(DBBrowser.class);

	private ElementListTableSorter availableSorter;
	private ElementListTableSorter chosenSorter;
	private TableRowSorter<ElementListTableModel> rowFilter;
	protected TridasTreeViewPanel treepanel;
	
	private ElementList selectedElements;
    private boolean isMultiDialog;
    private int minimumSelectedElements = 1;
    
    private SearchPanel searchPanel;

    public DBBrowser(java.awt.Frame parent, boolean modal) {
    	this(parent, modal, false);	
    }
       

    /** Creates new form as child of Frame */    
    public DBBrowser(Window parent, boolean modal, boolean openMulti) {
        super(parent, modal);
        doInitComponents();
        
        initialize(openMulti);
        setIconImage(Builder.getApplicationIcon());
    }


    
    /**
     * Creates new gui as child of a dialog. Convenience constructor for a single 
     * file selector browser.
     * 
     * @param parent - parent dialog
     * @param modal - should it be modal?
     * @see DBBrowser(parent, modal, openMulti)
     * @wbp.parser.constructor
     */
    public DBBrowser(java.awt.Dialog parent, boolean modal) {
    	this(parent, modal, false);
    }
    
    /**
     * Creates new gui as child of a dialog.
     * 
     * @param parent - parent dialog
     * @param modal - should it be modal?
     * @param openMulti - should the user be able to pick multiple series?
     */
    public DBBrowser(java.awt.Dialog parent, boolean modal, boolean openMulti) {
        super(parent, modal);
        doInitComponents();
        
        initialize(openMulti);
    }
    
    public DBBrowser(boolean modal, boolean openMulti){
    	super((java.awt.Frame)null, modal);
    	doInitComponents();
        initialize(openMulti);
    }

    /**
     * Initilize the gui components
     * 
     * @param openMulti - are we allowing picking of multiple files?
     */
    private void initialize(boolean openMulti) {
    	
    	
    	selectedElements = new ElementList();
        
        isMultiDialog = openMulti;

        listTableSplit.setOneTouchExpandable(true);
        
        setupSearch();
        setupTableArea();
        setupTree();
        populateComponents(); 
        
       
        // repack :)
        pack();

        
        // don't let it grow to distort our dialog!
        extraButtonPanel.setPreferredSize(new Dimension(btnOk.getWidth(), 1));
        extraButtonPanel.setMaximumSize(new Dimension(btnOk.getWidth(), Integer.MAX_VALUE));
       
        
        // Set size of window to 1024x700 or full screen whichever is the smaller
        int width=this.getToolkit().getScreenSize().width-10;
        int height = this.getToolkit().getScreenSize().height-40; 
        this.setSize(width, height);
        Center.center(this);
        
        btnTogMostRecent.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ev){
        		filterAvailMeas();
        	}
        });
        
        btnTogByMe.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ev){
        		filterAvailMeas();
        	}
        });
        
        cboSeriesType.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ev){
        		filterAvailMeas();
        	}
        });
        
        
        
        // Whenever the site list changes, make sure we repopulate our site list
 /*       App.tridasObjects.addResourceEventListener(new ResourceEventListener() {
        	public void resourceChanged(ResourceEvent re) {	
        		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE)
        			populateSiteList();
        	}
        });
   */     
        btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				// only dispose if finish succeeded (for overriding)
				if(finish())
					dispose();
			}
        });

        btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				dispose();
			}
        });
        
        btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(panelRibbon.isVisible()) {
					panelRibbon.setVisible(false);
					btnOptions.setText(I18n.getText("dbbrowser.showOptions"));
				} else {
					panelRibbon.setVisible(true);
					btnOptions.setText(I18n.getText("dbbrowser.hideOptions"));
				}
				
			}
        });        
        
       /* txtFilterInput.addKeyListener(new KeyListener() {
        	public void keyPressed(KeyEvent txt){
        		
     
        	}
        	
        	public void keyTyped(KeyEvent txt){

        		
        	}        	
        	public void keyReleased(KeyEvent txt){
        		         		
		    	
   		    	if(txtFilterInput.getText().length()==0) {
   		    	    // Filter text is empty so reset object list
   		    		lstSites.clearSelection();
   		    		populateSiteList("");
   		    		return;
   		    	}
        		
	       		if(txtFilterInput.getText().length()==24)
	       		{
	       			// A barcode was probably just scanned
	       			playBarcodeBeep();		
	       			String barcodeText = txtFilterInput.getText();
	       			txtFilterInput.setText("");
	       			
	       			// Decode the barcode string
	       			LabBarcode.DecodedBarcode barcode = LabBarcode.decode(barcodeText);
	       			
	       			// Do search
	       			searchByBarcode(barcode);
	       		}
	       		else {
	      	    	// User is typing a lab code
         			int key = txt.getKeyCode();
           		    if (key == KeyEvent.VK_ENTER)
           		    {
           		    	 // User typed lab code followed by enter so search for it!
           		    	 doLabCodeSearch(txtFilterInput.getText());
           		    }
	           		     
	           		// Enter has not been pressed so user is 
           		    // still typing lab code    
	           		return;
	      			 
	      		 }

        	}


        });
     
        txtFilterInput.requestFocusInWindow();
        */
        this.treepanel.setFocus();
    }
    
    /**
     * Search for series associated with an entity
     * 
     * @param entity
     * @return
     */
    public Boolean doSearchForAssociatedSeries(ITridas entity)
    {
    	SearchParameters param;
    	param = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
    	TridasObjectEx obj = null;
    	TridasElement elem = null;
    	TridasSample samp = null;
    	TridasRadius rad = null;
    	   	
    	if((entity instanceof TridasObjectEx) || (entity instanceof TridasObject) )
    	{
    		param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		obj = (TridasObjectEx) entity;
    		
        	if(obj.isSetElements())
        	{
        		param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, obj.getElements().get(0).getIdentifier().getValue());
        		elem = (TridasElement) obj.getElements().get(0);
        		
        	   	if(elem.isSetSamples() )
            	{
            		param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, elem.getSamples().get(0).getIdentifier().getValue());
            		samp = (TridasSample) elem.getSamples().get(0);
            		
                	if(samp.isSetRadiuses() )
                	{
                		param.addSearchConstraint(SearchParameterName.RADIUSID, SearchOperator.EQUALS, samp.getRadiuses().get(0).getIdentifier().getValue());
                		rad = (TridasRadius) samp.getRadiuses().get(0);
                		
                		if(rad.isSetMeasurementSeries())
                		{
                    		param.addSearchConstraint(SearchParameterName.SERIESID, SearchOperator.EQUALS, rad.getMeasurementSeries().get(0).getIdentifier().getValue());
                		}
                	} 
            	}    	

        	}
    	}
    	else if (entity instanceof TridasElement)
    	{
    		param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    	}    	
    	else if (entity instanceof TridasSample)
    	{
    		param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    	}
    	else if (entity instanceof TridasRadius)
    	{
    		param.addSearchConstraint(SearchParameterName.RADIUSID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    	}   
    	else if (entity instanceof ITridasSeries)
    	{
    		param.addSearchConstraint(SearchParameterName.SERIESID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    	}   
    	else
    	{
    		return false;
    	}
    	
		// Do the search 
    	final DBBrowser glue = this;
		SeriesSearchResource searchResource = new SeriesSearchResource(param);
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(glue, searchResource);
		searchResource.query();
		dlg.setVisible(true);
	
		if(!dlg.isSuccessful()) {
			// Search failed
			new Bug(dlg.getFailException());
			return false;
		} else {
			// Search successful
			
			ElementList elements = searchResource.getAssociatedResult();
			
			if(elements.size()==0)
			{
				Alert.message(null, I18n.getText("error"), I18n.getText("error.noRecordsFound"));
				return false;
			}
			else
			{	
				// Several found so show results in table
				((ElementListTableModel)tblAvailMeas.getModel()).setElements(elements);
				availableSorter.reSort();
			}
			return true;
		}
    	
    }
    
   
    
    /**
     * Fully loads all elements back into the list
     * Only works for multi dialogs
     * 
     * @return successful?
     */
    public boolean loadAllElements() {
    	if(!isMultiDialog)
    		return false;
  
    	int nloaded = 0;
    	
    	for(int i = 0; i < selectedElements.size(); i++) {
    		Element e = selectedElements.get(i);
    		Sample s;
    		
    		try {
    			s = e.load();
    			nloaded++;
    		} catch (IOException ioe) {
    			int ret = JOptionPane.showConfirmDialog(this, I18n.getText("error.loadingSample") + ":\n" + ioe + 
    					"\n\n"+I18n.getText("question.continue"), I18n.getText("error"), JOptionPane.YES_NO_OPTION);
    			
    			if(ret == JOptionPane.NO_OPTION)
    				return false;
    			
    			continue;
    		}
    		
    		// why would this be?
    		if(!(e instanceof CachedElement)) {
    			selectedElements.remove(i);
    			selectedElements.add(i, new CachedElement(s));
    		}
    	}
    	
    	// no, still can't work
    	if(nloaded < minimumSelectedElements)
    		return false;
    	
    	return true;
    }
    
    /**
     * Finish up and return series 
     * 
     * @return - successful?
     */
    protected boolean finish() {
    	if(!isMultiDialog) {
			// Convert from view to model rows
			int viewRows[] = tblAvailMeas.getSelectedRows();
			int modelRows[] = tblAvailMeas.getSelectedRows();
			for(int i = 0; i < viewRows.length; i++) {
				modelRows[i] = tblAvailMeas.convertRowIndexToModel(viewRows[i]);
			}    		
    	
    		// create a list of our selected elements (should only be one)
    		for(int i = 0; i < modelRows.length; i++)
    			selectedElements.add(((ElementListTableModel)tblAvailMeas.getModel()).
    					getElementAt(modelRows[i]));
    	}
    	
    	// Save the browse list type
//		App.prefs.setPref("tellervo.dbbrowser.listmode", 
//				((BrowseListMode) cboBrowseBy.getSelectedItem()).name());
    	
    	returnStatus = RET_OK;
    	
    	this.cacheSettings();
    	return true;
    }
    
    /**
     * Setup the search panel
     * 
     */
    private void setupSearch() {
    	searchPanel = new SearchPanel(new SearchSupport(), this);
    	this.tabbedPane.setComponentAt(1, searchPanel);
    	
    	//searchPanel2 = new StaticSearchPanel(new SearchSupport());
    	
    	//this.tabbedPane.setComponentAt(2, searchPanel2);
    	
    	//this.tabbedPane.setEnabledAt(1, false);
    }
    
    public void doSearch(SearchParameters params)
    {
    	this.tabbedPane.setSelectedIndex(1);
    	searchPanel.runQuery(params);
    }
    
    /** 
     * Update the filter used to remove series from the list which don't match
     * the requested criteria.  The filter works on the list returned from the 
     * search and is used for simple filtering to stop the need for extra 
     * requests to the webservice.  
     */
    private void filterAvailMeas() {       
        List<RowFilter<ElementListTableModel, Object>> filters = new ArrayList<RowFilter<ElementListTableModel, Object>>(2);
        RowFilter<ElementListTableModel, Object> seriesTypeFilter = null;         
        RowFilter<ElementListTableModel, Object> authorFilter = null; 
        RowFilter<ElementListTableModel, Object> versionFilter = null; 
        
        // Series Type
        if (cboSeriesType.getSelectedIndex()==1){
        	seriesTypeFilter = RowFilter.regexFilter("Raw", 3);
        } else if (cboSeriesType.getSelectedIndex()==2){
        	seriesTypeFilter = RowFilter.regexFilter("Raw", 3);
        	seriesTypeFilter = RowFilter.notFilter(seriesTypeFilter);
        } else {
        	seriesTypeFilter = RowFilter.regexFilter("", 3);
        }

        // Author Filter
        if (btnTogByMe.isSelected()){
        	authorFilter = RowFilter.regexFilter(App.currentUser.getFirstName() + " " + App.currentUser.getLastName(), 2);
        } else {
        	authorFilter = RowFilter.regexFilter("", 2);
        }
        
        if (this.btnTogMostRecent.isSelected()){
        	versionFilter = RowFilter.regexFilter("true", 11);
        } else {
        	versionFilter = RowFilter.regexFilter("", 11);
        }
        
        // Add all filters together
        filters.add(seriesTypeFilter);
        filters.add(authorFilter);
        filters.add(versionFilter);
        RowFilter<ElementListTableModel,Object> allFilters = RowFilter.andFilter(filters);
        
        
        // Apply filters
    	rowFilter.setRowFilter(allFilters);
    }

    /**
     * Setup the table(s) ready for the list of series.
     */
    private void setupTableArea() {
    	
		ElementListTableModel mdlAvailMeas = new ElementListTableModel();
		tblAvailMeas.setModel(mdlAvailMeas); // set model
		//tblAvailMeas.getColumnModel().removeColumn(tblAvailMeas.getColumn("hidden.MostRecentVersion"));
			
		availableSorter = new ElementListTableSorter(mdlAvailMeas, tblAvailMeas);
		availableSorter.sortOnColumn(0, false);
		tblAvailMeas.getTableHeader().addMouseListener(availableSorter); // add sorter & header renderer
		tblAvailMeas.setColumnSelectionAllowed(false);
		tblAvailMeas.setRowSelectionAllowed(true);
		setupTableColumns(tblAvailMeas, false);
		
		rowFilter = new TableRowSorter<ElementListTableModel>(mdlAvailMeas);
		tblAvailMeas.setRowSorter(rowFilter);
		
    	// hide irrelevent columns
		tblAvailMeas.setColumnControlVisible(true);
    	TableColumnModelExt colmodel = (TableColumnModelExt)tblAvailMeas.getColumnModel();
    	colmodel.getColumnExt(I18n.getText("hidden.MostRecentVersion")).setVisible(false);
    	colmodel.getColumnExt(I18n.getText("dbbrowser.n")).setVisible(false);
    	colmodel.getColumnExt(I18n.getText("dbbrowser.rec")).setVisible(false);
    	colmodel.getColumnExt(I18n.getText("dbbrowser.hash")).setVisible(false);
		
		ElementListTableModel mdlChosenMeas = new ElementListTableModel(selectedElements);
		tblChosenMeas.setModel(mdlChosenMeas);
		
		chosenSorter = new ElementListTableSorter(mdlChosenMeas, tblChosenMeas);
		chosenSorter.sortOnColumn(0, false);
		tblChosenMeas.getTableHeader().addMouseListener(chosenSorter); // add sorter & header renderer
		tblChosenMeas.setColumnSelectionAllowed(false);
		tblChosenMeas.setRowSelectionAllowed(true);
		setupTableColumns(tblChosenMeas, false);
		
    	// hide irrelevent columns
		tblChosenMeas.setColumnControlVisible(true);
    	TableColumnModelExt colmodel2 = (TableColumnModelExt)tblChosenMeas.getColumnModel();
    	colmodel2.getColumnExt(I18n.getText("hidden.MostRecentVersion")).setVisible(false);
    	colmodel2.getColumnExt(I18n.getText("dbbrowser.n")).setVisible(false);
    	colmodel2.getColumnExt(I18n.getText("dbbrowser.rec")).setVisible(false);
    	colmodel2.getColumnExt(I18n.getText("dbbrowser.hash")).setVisible(false);
    	
		
		if(!isMultiDialog) {
			// only single selection!
			
			extraButtonPanel.setVisible(false);
			
			tblAvailMeas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// easy; create a single scroll pane and jam it into the panel!
			workArea.setLayout(new BorderLayout());
    		workArea.add(new JScrollPane(tblAvailMeas), BorderLayout.CENTER);
    		
    		JTableStatusBar statusPanel = new JTableStatusBar(tblAvailMeas);
    		
    		workArea.add(statusPanel, BorderLayout.SOUTH);

    		// Selecting buttons not required for single selection mode
			btnSelectAll.setVisible(false);
			btnSelectNone.setVisible(false);
			btnInvertSelect.setVisible(false);
    		
    		// start out with initial disabled state, only allow when something is selected
			btnOk.setEnabled(false);
    		tblAvailMeas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
    			public void valueChanged(javax.swing.event.ListSelectionEvent lse) {
    				// ignore the first adjustment
    				if(lse.getValueIsAdjusting())
    					return;
    				
    				if(tblAvailMeas.getSelectedRowCount() == 0)
    					btnOk.setEnabled(false);
    				else
    					btnOk.setEnabled(true);
    			}
    		});
    		
    		tblAvailMeas.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent evt) {
					if(evt.getClickCount()>1)
					{
						// Load series on double click
						if(finish()) dispose();
					}
					
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {	}
				@Override
				public void mouseExited(MouseEvent arg0) { }
				@Override
				public void mousePressed(MouseEvent arg0) { }
				@Override
				public void mouseReleased(MouseEvent arg0) {}
    			
    		});
    		
    		
    	} else {
			tblAvailMeas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			tblChosenMeas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			
    		tblAvailMeas.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent evt) {
					if(evt.getClickCount()>1)
					{
						// Add series to chosen on double click
						addSelectedToChosen();
					}
					
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {	}
				@Override
				public void mouseExited(MouseEvent arg0) { }
				@Override
				public void mousePressed(MouseEvent arg0) { }
				@Override
				public void mouseReleased(MouseEvent arg0) {}
    			
    		});
			
    		workArea.setLayout(new BoxLayout(workArea, BoxLayout.Y_AXIS));
    		
    		JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
    		
    		Icon dn = Builder.getIcon("down.png", 22);
    		Icon up = Builder.getIcon("up.png", 22);
    		Icon all = Builder.getIcon("selectall.png", 22);
    		Icon none = Builder.getIcon("selectnone.png", 22);
    		Icon invert = Builder.getIcon("selectinvert.png", 22);

    		btnAdd.setIcon(dn);
    		btnRemove.setIcon(up);
    		btnSelectAll.setIcon(all);
    		btnSelectNone.setIcon(none);
    		btnInvertSelect.setIcon(invert);
    		
    		//btnAddAll.setIcon(downall);
    		//btnRemoveAll.setIcon(upall);
    		
    		//buttonBar.add(btnAddAll);
    		buttonBar.add(btnAdd);
    		buttonBar.add(btnRemove);
    		//buttonBar.add(btnRemoveAll);

    		// start out with initial disabled state, only allow when our element list has an element in it!
			btnOk.setEnabled(false);

    		// when adding, just add things to our selectedElements list
    		btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					addSelectedToChosen();
				}
    		});
    		
    		


    		// removing is similar...
    		btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					int rows[] = tblChosenMeas.getSelectedRows();
					List<Element> removeList = new ArrayList<Element>();
					
					for(int i = 0; i < rows.length; i++) {
						Element e = ((ElementListTableModel)tblChosenMeas.getModel()).getElementAt(rows[i]);					
						removeList.add(e);
					}

					// we have to do this atomically
					for(Element e : removeList)
						selectedElements.remove(e);
					
					// tell the table it's changed!
					((ElementListTableModel)tblChosenMeas.getModel()).fireTableDataChanged();
					// repaint to show non-disabled rows
					tblAvailMeas.repaint();

					// verify a selected element
					if(selectedElements.size() >= minimumSelectedElements)
						btnOk.setEnabled(true);
					else
						btnOk.setEnabled(false);
				}
    		});

    		// make it only 10 rows tall (+header)
    		tblChosenMeas.setPreferredScrollableViewportSize(
    				new Dimension(tblChosenMeas.getPreferredSize().width, tblAvailMeas.getRowHeight() * 11));
    		
    		workArea.add(new JScrollPane(tblAvailMeas));
    		workArea.add(buttonBar);
    		workArea.add(new JScrollPane(tblChosenMeas));
    	}	
    }
    
    private void addSelectedToChosen()
    {
		
		// Convert from view to model rows
		int viewRows[] = tblAvailMeas.getSelectedRows();
		int modelRows[] = tblAvailMeas.getSelectedRows();
		for(int i = 0; i < viewRows.length; i++) {
			modelRows[i] = tblAvailMeas.convertRowIndexToModel(viewRows[i]);
		}

		// Move selected into 'chosen' list
		for(int i = 0; i < modelRows.length; i++) {
			Element e = ((ElementListTableModel)tblAvailMeas.getModel()).getElementAt(modelRows[i]);		
			if(!selectedElements.contains(e)) selectedElements.add(e);
		}
		
		// tell the table it's changed!
		((ElementListTableModel)tblChosenMeas.getModel()).fireTableDataChanged();
		
		// remove the selection
		// repaint to show non-disabled rows
		tblAvailMeas.repaint();
		tblAvailMeas.clearSelection();

		// verify a selected element
		if(selectedElements.size() >= minimumSelectedElements)
			btnOk.setEnabled(true);
		else
			btnOk.setEnabled(false);
    }
    
    private void setupTableColumns(JXTable table, boolean disableSelections) {

    	// set our column widths
    	ElementListTableModel.setupColumnWidths(table);
		
		table.setDefaultRenderer(Object.class, new ElementListCellRenderer(this, disableSelections));
		table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer(this, disableSelections));
		
		// popup menu
		table.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				// only clicks on tables
				if(!(e.getSource() instanceof JXTable))
					return;
				
				JXTable table = (JXTable) e.getSource();
				ElementListTableModel model = (ElementListTableModel) table.getModel();
				
				// get the row and sanity check
				int row = table.rowAtPoint(e.getPoint());
				if(row < 0 || row >= model.getRowCount())
					return;
				
				// select it?
				table.setRowSelectionInterval(row, row);
				
				// get the element
				Element element = model.getElementAt(row);
				
				// create and show the menu
				JPopupMenu popup = new ElementListPopupMenu(element, DBBrowser.this);
				popup.show(table, e.getX(), e.getY());
			}

			
		});
    }
    
    private void setupTree(){
    	
    	try{
    		this.loadTreePanel();
    	} catch (Exception e)
    	{
    		
    	}
    	
    	if(treepanel==null)
    	{
    		treepanel = new TridasTreeViewPanel();
    	}
    	
    	treepanel.addTridasSelectListener(this);
    	browsePanel.setLayout(new BorderLayout());
    	this.browsePanel.add(treepanel, BorderLayout.CENTER);
    	//tabbedPane.add(treepanel, "Browse", 0);
 
    	treepanel.requestFocus();
    	//this.tabbedPane.insertTab("Browse", null, treepanel, null, 0);
    	
    	
        
    }
    

      
	private void populateComponents() {
    	// single selection on site list
/*    	lstSites.setModel(new ArrayListModel<TridasObjectEx>());
		lstSites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// make it nicely render cells
        lstSites.setCellRenderer(new SiteRenderer());
    	
    	// and make the regions combo box reflect it
		cboBrowseBy.setModel(new DefaultComboBoxModel(BrowseListMode.values()));
		
		// repopulate the site list when something is chosen...
        cboBrowseBy.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	populateSiteList();
            }
        });

        String browseModeStr = App.prefs.getPref("tellervo.dbbrowser.listmode", BrowseListMode.POPULATED.name());
        
        // handle old modes, maybe?
        BrowseListMode browseMode;
        try {
        	browseMode = BrowseListMode.valueOf(browseModeStr);
        }
        catch (Exception e) {
        	browseMode = BrowseListMode.POPULATED;
        }
        
        // set the mode, which triggers populating of our site list
		cboBrowseBy.setSelectedItem(browseMode);
		
		final DBBrowser glue = this;
		lstSites.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent lse) {
				// ignore the first adjustment
				if(lse.getValueIsAdjusting())
					return;
				
				Object[] selectedO = lstSites.getSelectedValues();
				
				if(selectedO.length  == 0) {
					// no selections. clear table!
					return;
				}

				// make it into a tridas object array
				//TridasObjectEx[] selected = Arrays.copyOf(selectedO., selectedO.length, 
				//		TridasObjectEx[].class);
				
				//TridasObjectEx[] selected = new TridasObjectEx[selectedO.length];
				
				
				TridasObjectEx[] selected = new TridasObjectEx[selectedO.length];
				System.arraycopy(selectedO, 0, selected, 0, selectedO.length);
				
				
				// sort the list so it's easy to compare
				Arrays.sort(selected, new TridasObjectList.SiteComparator());

				// arrays are exactly the same, don't do anything
				if(Arrays.equals(selected, lastSelectedObjects))
					return;
				
				// save this for later.
				lastSelectedObjects = selected;
				
				// set up our query...
				SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
				for(int i = 0; i < selected.length; i++) {
					TridasObjectEx site = (TridasObjectEx) selected[i];
					search.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, 
							SearchOperator.EQUALS, site.getIdentifier().getValue());
				}

				SeriesSearchResource searchResource = new SeriesSearchResource(search);
				CorinaResourceAccessDialog dlg = new CorinaResourceAccessDialog(glue, searchResource);
				
				// start our query (remotely)
				searchResource.query();
				dlg.setVisible(true);
				
				if(!dlg.isSuccessful()) {
					new Bug(dlg.getFailException());
				} else {
					((ElementListTableModel)tblAvailMeas.getModel()).setElements(searchResource.getAssociatedResult());
					availableSorter.reSort();
				}
			}
		});
        
		// double click: different action depending on what kind of dialog
		tblAvailMeas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if(isMultiDialog)
						btnAdd.doClick();
					else
						btnOk.doClick();
				}
			}
		});
		
		// now, set up our buttons
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tblAvailMeas.selectAll();
			}
		});

		btnSelectNone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tblAvailMeas.removeRowSelectionInterval(0, tblAvailMeas.getRowCount() - 1);
			}
		});

		btnInvertSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < tblAvailMeas.getRowCount(); i++) {
					if(tblAvailMeas.isRowSelected(i))
						tblAvailMeas.removeRowSelectionInterval(i, i);
					else
						tblAvailMeas.addRowSelectionInterval(i, i);
				}
			}
		});
		*/
	}
    

/*	private void populateSiteList(){
		populateSiteList(txtFilterInput.getText());
	}
	
    @SuppressWarnings("unchecked")
	private void populateSiteList(String searchStr) {
    	Collection<TridasObjectEx> sites; // = App.tridasObjects.getObjectList();
    	TridasObjectEx selectedSite = (TridasObjectEx) lstSites.getSelectedValue();
    	
    	BrowseListMode browseMode = (BrowseListMode) cboBrowseBy.getSelectedItem();
    	
    	// populate from the right list...
    	switch(browseMode) {
    	case ALL:
    	default:
    		sites = App.tridasObjects.getObjectList();
    		break;
    		
    	case POPULATED:
    		sites = App.tridasObjects.getPopulatedObjectList();
    		break;
    		
    	case POPULATED_FIRST:
    		sites = App.tridasObjects.getPopulatedFirstObjectList();
    		break;
    	}
    	
		// User has NOT entered filter text
		if (searchStr.equals("")) {
			((ArrayListModel<TridasObjectEx>) lstSites.getModel())
					.replaceContents(sites);

		// User HAS entered filter text
		} else {
			List<TridasObjectEx> filteredSites = new ArrayList<TridasObjectEx>();

			// Loop through master site list and check if filter matches
			String filter = searchStr.toLowerCase();
			for (TridasObjectEx s : sites) {
				String search = s.toTitleStringWithParentCode().toLowerCase();
				if (search.indexOf(filter) != -1)
					filteredSites.add(s);
			}
			((ArrayListModel<TridasObjectEx>) lstSites.getModel())
					.replaceContents(filteredSites);
			
		}
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSite != null) {
    		for(int i = 0; i < lstSites.getModel().getSize(); i++) {
    			if(((TridasObjectEx)lstSites.getModel().getElementAt(i)).equals(selectedSite)) {
    				lstSites.setSelectedIndex(i);
    				lstSites.ensureIndexIsVisible(i);
    				break;
    			}
    		}
    	}
    	
    	// If there is only one item in list then select it!
    	if (lstSites.getModel().getSize()==1) lstSites.setSelectedIndex(0);

    }
    */
    /**
     * adds an element to the list
     * @param e
     */
    public void addElement(Element e) {
		if(!selectedElements.contains(e))
			selectedElements.add(e);
		else
			return;
	
		chosenSorter.reSort();
		
		// tell the table it's changed!
		((ElementListTableModel)tblChosenMeas.getModel()).fireTableDataChanged();

		// verify a selected element
		if(selectedElements.size() > minimumSelectedElements)
			btnOk.setEnabled(true);
		else
			btnOk.setEnabled(false);    	
    }
    
    /**
     * Remove the element from all lists that I have
     * @param e
     */
    public void deleteElement(Element e) {
   		deleteElementFromModel(e, ((ElementListTableModel)tblChosenMeas.getModel()));
   		deleteElementFromModel(e, ((ElementListTableModel)tblAvailMeas.getModel()));    	
    }
    
    private void deleteElementFromModel(Element e, ElementListTableModel model) {
    	ElementList elements = model.getElements();
    	ListIterator<Element> iterator = elements.listIterator();
    	
    	while(iterator.hasNext()) {
    		int idx = iterator.nextIndex();
    		Element element = iterator.next();
    		
    		if(element.equals(e)) {
    			// remove the current element
    			iterator.remove();
    			
    			// notify the table
    			model.fireTableRowsDeleted(idx, idx);
    		}
    	}
    }
    
    /**
     * Is this element in the selected list?
     * @param e
     * @return true if the selected elements table contains this element
     */
    public boolean isSelectedElement(Element e) {
    	return selectedElements.contains(e);
    }
    
    /**
     * How many elements need to be selected before we allow continuing?
     * (defaults to 1, increase for sums?)
     * @param value
     */
    public void setMinimumSelectedElements(int value) {
    	minimumSelectedElements = value;
    }
 
    /**
     * Selects the site indicated by "code"
     * No effect if code is invalid
     * @param code
     */
    public void selectSiteByCode(String code) {
    	TridasObjectEx site = App.tridasObjects.findObjectBySiteCode(code);

    	if(site != null)
    		this.treepanel.entitySelected(new TridasSelectEvent(this, TridasSelectEvent.ENTITY_SELECTED, site));
    }
  
    
    /** @return an ElementList of selected elements!
     */
    public ElementList getSelectedElements() {
    	return selectedElements;
    }
    
    /**
     * Get the JPanel that comprises space between invert...OK
     * Not initialized; must be laid out manually
     * @return
     */
    public JPanel getExtraButtonPanel() {
    	return extraButtonPanel;
    }
    
    public JButton getPreviewButton(){
    	return btnPreview;
    }

	public boolean isElementDisabled(Element e) {
		// an element is disabled in our table model if it's already been selected...
		return isSelectedElement(e);
	}

	@SuppressWarnings("unused")
	private enum BrowseListMode {
		ALL(I18n.getText("dbbrowser.showAllObjects")),
		POPULATED(I18n.getText("dbbrowser.showPopulatedObjects")),
		POPULATED_FIRST(I18n.getText("dbbrowser.showPopulatedObjectsFirst"));
		
		private final String value;
		
		private BrowseListMode(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	/**
	 * Search for series using a Series, Sample or Box barcode
	 * 
	 * @param barcode
	 * @return boolean 
	 */
	@SuppressWarnings("unused")
	private boolean searchByBarcode(LabBarcode.DecodedBarcode barcode)
	{
		String reason = null;
		final DBBrowser glue = this;
		
		// set up our query...
		SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
		

		try {				
			// Add search constraints depending on barcode type
			if(barcode.uuidType == LabBarcode.Type.SERIES) {
				search.addSearchConstraint(SearchParameterName.SERIESDBID, SearchOperator.EQUALS, barcode.uuid.toString());
			}
			else if (barcode.uuidType == LabBarcode.Type.SAMPLE){
				search.addSearchConstraint(SearchParameterName.SAMPLEDBID, SearchOperator.EQUALS, barcode.uuid.toString());
			}
			else if (barcode.uuidType == LabBarcode.Type.BOX){
				search.addSearchConstraint(SearchParameterName.SAMPLEBOXID, SearchOperator.EQUALS, barcode.uuid.toString());
			}

		} catch (IllegalArgumentException iae) {
			reason = iae.getMessage();
		}
		
		// Do the search 
		SeriesSearchResource searchResource = new SeriesSearchResource(search);
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(glue, searchResource);
		searchResource.query();
		dlg.setVisible(true);
		
		
		if(!dlg.isSuccessful()) {
			// Search failed
			new Bug(dlg.getFailException());
			return false;
		} else {
			// Search successful
			
			ElementList elements = searchResource.getAssociatedResult();
			
			if(elements.size()==0)
			{
				Alert.message(I18n.getText("error"), I18n.getText("noRecordsFoundForBarcode"));
				return false;
			}
			else if(elements.size()==1 & barcode.uuidType == LabBarcode.Type.SERIES & !isMultiDialog) 
			{
				// Series scanned and found, and not a multi dialog so go ahead and load
				selectedElements = elements;
				returnStatus = RET_OK;
				dispose();	
			}
			else if (elements.size()==1 & barcode.uuidType == LabBarcode.Type.SERIES & isMultiDialog)
			{
				// Series scanned and found, but it's a multi dialog so just add to selected list
				
				for (Element e : elements)
				{
					if(!selectedElements.contains(e)) selectedElements.add(e);
				}
				
				((ElementListTableModel)tblChosenMeas.getModel()).fireTableDataChanged();				

			}
			else
			{	
				// Several found so show results in table
				((ElementListTableModel)tblAvailMeas.getModel()).setElements(elements);
				availableSorter.reSort();
			}
			return true;
		}		
	}

	
	/**
	 * Class implementing search support for DB browser
	 * 
	 * @author Lucas Madar
	 */
	private class SearchSupport implements SearchResultManager, ChangeListener {

		public SearchSupport() {
			
			DBBrowser.this.tabbedPane.addChangeListener(this);
		}


		public void notifySearchFinished(ElementList elements) {
			tblAvailMeas.setEnabled(true);

			if (elements != null && !elements.isEmpty()) {
				((ElementListTableModel) tblAvailMeas.getModel()).setElements(elements);
				availableSorter.reSort();

			} else {

				((ElementListTableModel) tblAvailMeas.getModel()).setElements(null);

				Alert.message("No matches", "There are no matches");
			}
		}

		public void notifySearchStarting() {
			System.out.println("SEARCH STARTING");

		}

		public void stateChanged(ChangeEvent e) {
			int index = DBBrowser.this.tabbedPane.getSelectedIndex();
			
			if(index == 0) {
				DBBrowser.this.searchPanel.cancelSearch();
			}
		}
	}


	@Override
	public void entitySelected(TridasSelectEvent event) {
		ITridas entity;
		
		try {
			entity = event.getEntityList().get(0);
			if(entity instanceof WSIBox)
			{
				Alert.message("Unsupported", "Box barcodes are not supported in this context");
				return;
			}
			
			this.doSearchForAssociatedSeries(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void cacheSettings()
	{
	     FileOutputStream fos = null;
	     ObjectOutputStream out = null;
	     try
	     {
	       fos = new FileOutputStream("/tmp/treepanel");
	       out = new ObjectOutputStream(fos);
	       out.writeObject(this.treepanel);
	       out.close();
	     }
	     catch(IOException ex)
	     {
				log.debug("Unable to cache tree panel");

	     }
	}
	
	private void loadTreePanel()
	{
		String filename = "/tmp/treepanel";
		
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
		    treepanel = (TridasTreeViewPanel)in.readObject();
		    in.close();
		}
		catch(IOException ex)
		{
			log.debug("IOException when trying to load tree panel cache");
		}
		catch(ClassNotFoundException ex)
		{
			log.debug("ClassNotFoundException when trying to load tree panel cache");
		}
	}
	

}
