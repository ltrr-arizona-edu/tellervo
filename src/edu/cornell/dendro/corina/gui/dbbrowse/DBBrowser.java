package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.PopupListener;
import edu.cornell.dendro.corina.wsi.ResourceEvent;
import edu.cornell.dendro.corina.wsi.ResourceEventListener;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesSearchResource;

public class DBBrowser extends DBBrowser_UI {
	private static final long serialVersionUID = 1L;
	
	private ElementList selectedElements;
    private boolean isMultiDialog;
    private int minimumSelectedElements = 1;
    
    public DBBrowser(java.awt.Frame parent, boolean modal) {
    	this(parent, modal, false);	
    }
    
    /** Creates new form as child of Frame */    
    public DBBrowser(java.awt.Frame parent, boolean modal, boolean openMulti) {
        super(parent, modal);
        initComponents();
        
        initialize(openMulti);
    }

    public DBBrowser(java.awt.Dialog parent, boolean modal) {
    	this(parent, modal, false);
    }
    
    /** Creates new form  as child of dialog */
    public DBBrowser(java.awt.Dialog parent, boolean modal, boolean openMulti) {
        super(parent, modal);
        initComponents();
        
        initialize(openMulti);
    }

    private void initialize(boolean openMulti) {
        selectedElements = new ElementList();
        
        isMultiDialog = openMulti;

        listTableSplit.setOneTouchExpandable(true);

        cboBrowseBy.setVisible(false);
        
        setupTableArea();
        populateComponents();        
        
        // repack :)
        pack();

        // don't let it grow to distort our dialog!
        extraButtonPanel.setPreferredSize(new Dimension(btnOk.getWidth(), 1));
        extraButtonPanel.setMaximumSize(new Dimension(btnOk.getWidth(), Integer.MAX_VALUE));
               
        // Set size of window to 1024x700 or full screen whichever is the smaller
        int width;
        int height;
        if(this.getToolkit().getScreenSize().width<1024){
        	width = this.getToolkit().getScreenSize().width;
        }
        else{
        	width = 1024;
        }
        if(this.getToolkit().getScreenSize().height<700){
        	height = this.getToolkit().getScreenSize().height;
        }
        else{
        	height = 700;
        }        
        this.setSize(width, height);
        Center.center(this);
        
        // Whenever the site list changes, make sure we repopulate our site list
        App.tridasObjects.addResourceEventListener(new ResourceEventListener() {
        	public void resourceChanged(ResourceEvent re) {	
        		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE)
        			populateSiteList();
        	}
        });
        
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
        
        txtFilterInput.addKeyListener(new KeyListener() {
        	public void keyPressed(KeyEvent txt){
        		
     
        	}
        	public void keyTyped(KeyEvent txt){

        		
        	}        	
        	public void keyReleased(KeyEvent txt){
        		// Unselect any site selection then filter
        		// site list 
           		lstSites.clearSelection();
        		populateSiteList();
        	}
        });
     
        txtFilterInput.requestFocusInWindow();
    }

    /**
     * Fully loads all elements back into the list
     * Only works for multi dialogs
     * @return
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
    			int ret = JOptionPane.showConfirmDialog(this, "Error loading:\n" + ioe + 
    					"\n\nWould you like to continue?", "Error", JOptionPane.YES_NO_OPTION);
    			
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
    
    protected boolean finish() {
    	if(!isMultiDialog) {
    		int selectedRows[] = tblAvailMeas.getSelectedRows();
    	
    		// create a list of our selected elements (should only be one)
    		for(int i = 0; i < selectedRows.length; i++)
    			selectedElements.add(((DBBrowserTableModel)tblAvailMeas.getModel()).
    					getElementAt(selectedRows[i]));
    	}
    	
    	// Save the Region used in preferences so that it can be default next time
		App.prefs.setPref("corina.region.lastused", Integer.toString(cboBrowseBy.getSelectedIndex()));
    	
    	returnStatus = RET_OK;
    	return true;
    }
        
    private void setupTableArea() {
    	
		DBBrowserTableModel mdlAvailMeas = new DBBrowserTableModel();
		tblAvailMeas.setModel(mdlAvailMeas); // set model
		tblAvailMeas.getTableHeader().addMouseListener(
				new DBBrowserSorter(mdlAvailMeas, tblAvailMeas)); // add sorter & header renderer
		tblAvailMeas.setColumnSelectionAllowed(false);
		tblAvailMeas.setRowSelectionAllowed(true);
		setupTableColumns(tblAvailMeas, true);

		DBBrowserTableModel mdlChosenMeas = new DBBrowserTableModel(selectedElements);
		tblChosenMeas.setModel(mdlChosenMeas);
		tblChosenMeas.getTableHeader().addMouseListener(
				new DBBrowserSorter(mdlChosenMeas, tblChosenMeas)); // add sorter & header renderer
		tblChosenMeas.setColumnSelectionAllowed(false);
		tblChosenMeas.setRowSelectionAllowed(true);
		setupTableColumns(tblChosenMeas, false);
		
		if(!isMultiDialog) {
			// only single selection!
			tblAvailMeas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// easy; create a single scroll pane and jam it into the panel!
			workArea.setLayout(new BorderLayout());
    		workArea.add(new JScrollPane(tblAvailMeas), BorderLayout.CENTER);

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
    		
    	} else {
			tblAvailMeas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			tblChosenMeas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    		workArea.setLayout(new BoxLayout(workArea, BoxLayout.Y_AXIS));
    		
    		JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
    		
    		Icon dn = Builder.getIcon("down.png", 22);
    		Icon up = Builder.getIcon("up.png", 22);
    		//Icon downall = Builder.getIcon("down.png", 22);
    		//Icon upall = Builder.getIcon("uparrow.png");

    		btnAdd.setIcon(dn);
    		btnRemove.setIcon(up);
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
					int rows[] = tblAvailMeas.getSelectedRows();
					
					for(int i = 0; i < rows.length; i++) {
						Element e = ((DBBrowserTableModel)tblAvailMeas.getModel()).getElementAt(rows[i]);
						
						if(!selectedElements.contains(e))
							selectedElements.add(e);
					}
					
					// tell the table it's changed!
					((DBBrowserTableModel)tblChosenMeas.getModel()).fireTableDataChanged();
					
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
    		});

    		// removing is similar...
    		btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					int rows[] = tblChosenMeas.getSelectedRows();
					List<Element> removeList = new ArrayList<Element>();
					
					for(int i = 0; i < rows.length; i++) {
						Element e = ((DBBrowserTableModel)tblChosenMeas.getModel()).getElementAt(rows[i]);
						
						removeList.add(e);
					}

					// we have to do this atomically
					for(Element e : removeList)
						selectedElements.remove(e);
					
					// tell the table it's changed!
					((DBBrowserTableModel)tblChosenMeas.getModel()).fireTableDataChanged();
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
    
    private void setupTableColumns(JTable table, boolean disableSelections) {
		FontMetrics fm = table.getFontMetrics(table.getFont());
		
		table.setShowGrid(false);
		table.setShowVerticalLines(true);
		table.setGridColor(Color.lightGray);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(fm.stringWidth("C-XXX-XX-XX-Xx-Xx"));
		table.getColumnModel().getColumn(1).setPreferredWidth(fm.stringWidth("VERSION12"));
		table.getColumnModel().getColumn(2).setPreferredWidth(fm.stringWidth("DirectX"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("Pinus Nigra X"));
		table.getColumnModel().getColumn(4).setPreferredWidth(fm.stringWidth("99"));
		table.getColumnModel().getColumn(5).setPreferredWidth(fm.stringWidth("2008-08-08"));
		table.getColumnModel().getColumn(6).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(7).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(8).setPreferredWidth(fm.stringWidth("123"));
		table.getColumnModel().getColumn(9).setPreferredWidth(fm.stringWidth("123")); // checkbox?
		
		table.setDefaultRenderer(Object.class, new DBBrowserCellRenderer(this, disableSelections));
		
		// popup menu
		table.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				// only clicks on tables
				if(!(e.getSource() instanceof JTable))
					return;
				
				JTable table = (JTable) e.getSource();
				DBBrowserTableModel model = (DBBrowserTableModel) table.getModel();
				
				// get the row and sanity check
				int row = table.rowAtPoint(e.getPoint());
				if(row < 0 || row >= model.getRowCount())
					return;
				
				// select it?
				table.setRowSelectionInterval(row, row);
				
				// get the element
				Element element = model.getElementAt(row);
				
				// create and show the menu
				JPopupMenu popup = new DBBrowserPopupMenu(element, DBBrowser.this);
				popup.show(table, e.getX(), e.getY());
			}
		});
    }
    
    // Placeholder until we re-implement regions?
    private class SiteRegion {
    	public SiteRegion(String a, String b) {
    		// blah
    	}
    	public String getInternalRepresentation() {
    		return "ALL";
    	}
    	public String toString() {
    		return "Regions are deprecated";
    	}
    }
    
	private void populateComponents() {
    	//List<SiteRegion> regions = (List<SiteRegion>) App.dictionary.getDictionary("Regions");
		List<SiteRegion> regions = new ArrayList<SiteRegion>();
    	List<SiteRegion> regionList = new ArrayList<SiteRegion>();

    	// single selection on site list
    	lstSites.setModel(new ArrayListModel<TridasObjectEx>());
		lstSites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// make it nicely render cells
        lstSites.setCellRenderer(new SiteRenderer());

    	// Duplicate the region list; create an 'All Regions' 
    	regionList.add(new SiteRegion("ALL", "All Regions"));
    	
    	// null regions shouldn't happen, but be safe
    	if(regions != null) 
        	regionList.addAll(regions);
    	
    	// and make the regions combo box reflect it
		cboBrowseBy.setModel(new DefaultComboBoxModel(regionList.toArray()));
		
		// repopulate the site list when something is chosen...
        cboBrowseBy.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	populateSiteList();
            }
        });
		
		// Select the last used region otherwise select the first thing in the list
		// populate our site list (done automatically by choosing an index)
		if(App.prefs.getPref("corina.region.lastused", null) != null) {
			Integer lastUsedIndex = App.prefs.getIntPref("corina.region.lastused", 0);
			if(lastUsedIndex<cboBrowseBy.getItemCount()) 
				cboBrowseBy.setSelectedIndex(lastUsedIndex);
        	populateSiteList();
		} else {
			cboBrowseBy.setSelectedIndex(0);
		}
		
		final DBBrowser glue = this;
		lstSites.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent lse) {
				// ignore the first adjustment
				if(lse.getValueIsAdjusting())
					return;
				
				Object[] selected = lstSites.getSelectedValues();
				
				if(selected.length  == 0) {
					// no selections. clear table!
					return;
				}
				
				// set up our query...
				SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
				for(int i = 0; i < selected.length; i++) {
					TridasObjectEx site = (TridasObjectEx) selected[i];
					search.addSearchConstraint(SearchParameterName.OBJECTID, 
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
					((DBBrowserTableModel)tblAvailMeas.getModel()).setElements(searchResource.getAssociatedResult());
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
	}
    
    @SuppressWarnings("unchecked")
	private void populateSiteList() {
    	Collection<TridasObjectEx> sites = App.tridasObjects.getObjectList();
    	SiteRegion region = (SiteRegion) cboBrowseBy.getSelectedItem();
    	TridasObjectEx selectedSite = (TridasObjectEx) lstSites.getSelectedValue();
    	
    	// have we selected 'all regions?'
    	if(region.getInternalRepresentation().equals("ALL")) { 
    		
    		// User has NOT entered filter text
        	if(txtFilterInput.getText().equals("")){
        		((ArrayListModel<TridasObjectEx>)lstSites.getModel()).replaceContents(sites);

        	// User HAS entered filter text
        	} else {
        		List<TridasObjectEx> filteredSites = new ArrayList<TridasObjectEx>();
        		
        		// Loop through master site list and check if filter matches
        		String filter = txtFilterInput.getText().toLowerCase();
        		for(TridasObjectEx s : sites){
	        		String search = s.toTitleString().toLowerCase();
	        		if(search.indexOf(filter) != -1)
	        			filteredSites.add(s);        				
        		}
        		((ArrayListModel<TridasObjectEx>)lstSites.getModel()).replaceContents(filteredSites);
        	}		
    		
    	
        // A particular region has been selected so limit list before applying filter
    	} else {
    		Alert.error("error", "regions are disabled, use all please");
    		/*
    		List<TridasObjectEx> filteredSites = new ArrayList<TridasObject>();
    		
    		// filter the sites...
    		for(TridasObjectEx s : sites) {
    			if(s.inRegion(region.getInternalRepresentation())){
 
    	    		// User has NOT entered filter text
    	        	if(txtFilterInput.getText().equals("")){	
    	        		filteredSites.add(s);   

    	        	// User HAS entered filter text
    	        	} else {
    	        		String search = s.toFullString().toLowerCase();
    	        		if(search.indexOf(txtFilterInput.getText().toLowerCase()) != -1)
    	        			filteredSites.add(s);        				
    	        	}	
    			}
    		}
    		
    		lstSites.setModel(new javax.swing.DefaultComboBoxModel(filteredSites.toArray()));
    		*/
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
    
    /**
     * adds an element to the list
     * @param e
     */
    public void addElement(Element e) {
		if(!selectedElements.contains(e))
			selectedElements.add(e);
		else
			return;
	
		// tell the table it's changed!
		((DBBrowserTableModel)tblChosenMeas.getModel()).fireTableDataChanged();

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
   		deleteElementFromModel(e, ((DBBrowserTableModel)tblChosenMeas.getModel()));
   		deleteElementFromModel(e, ((DBBrowserTableModel)tblAvailMeas.getModel()));    	
    }
    
    private void deleteElementFromModel(Element e, DBBrowserTableModel model) {
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
    		lstSites.setSelectedValue(site, true);
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
}