package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.SiteRegion;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.webdbi.MeasurementSearchResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;
import edu.cornell.dendro.corina.webdbi.ResourceEvent;
import edu.cornell.dendro.corina.webdbi.ResourceEventListener;

public class DBBrowser extends DBBrowser_UI{
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

        lstSites.setCellRenderer(new SiteRenderer());
        listTableSplit.setOneTouchExpandable(true);

        setupTableArea();
        populateComponents();        
        
        // repack :)
        pack();

        // don't let it grow to distort our dialog!
        extraButtonPanel.setPreferredSize(new Dimension(btnOk.getWidth(), 1));
        extraButtonPanel.setMaximumSize(new Dimension(btnOk.getWidth(), Integer.MAX_VALUE));
        
        Center.center(this);
        
        // Whenever the site list changes, make sure we repopulate our site list
        App.sites.addResourceEventListener(new ResourceEventListener() {
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
		setupTableColumns(tblAvailMeas);

		DBBrowserTableModel mdlChosenMeas = new DBBrowserTableModel(selectedElements);
		tblChosenMeas.setModel(mdlChosenMeas);
		tblChosenMeas.getTableHeader().addMouseListener(
				new DBBrowserSorter(mdlChosenMeas, tblChosenMeas)); // add sorter & header renderer
		tblChosenMeas.setColumnSelectionAllowed(false);
		tblChosenMeas.setRowSelectionAllowed(true);
		setupTableColumns(tblChosenMeas);
		
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
    		tblAvailMeas.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
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
    		
    		Icon dn = Builder.getIcon("downarrow.png");
    		Icon up = Builder.getIcon("uparrow.png");
    		Icon downall = Builder.getIcon("uparrow.png");
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
    
    private void setupTableColumns(JTable table) {
		FontMetrics fm = table.getFontMetrics(table.getFont());
		
		table.setShowGrid(false);
		table.setShowVerticalLines(true);
		table.setGridColor(Color.lightGray);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(fm.stringWidth("C-XXX-XX-XX-Xx-Xx"));
		table.getColumnModel().getColumn(1).setPreferredWidth(fm.stringWidth("DirectX"));
		table.getColumnModel().getColumn(2).setPreferredWidth(fm.stringWidth("ABCD"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("Pinus Nigra X"));
		table.getColumnModel().getColumn(4).setPreferredWidth(fm.stringWidth("99"));
		table.getColumnModel().getColumn(5).setPreferredWidth(fm.stringWidth("2008-08-08"));
		table.getColumnModel().getColumn(6).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(7).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(8).setPreferredWidth(fm.stringWidth("123"));
		table.getColumnModel().getColumn(9).setPreferredWidth(fm.stringWidth("123")); // checkbox?
		
		table.setDefaultRenderer(Object.class,
				new DefaultTableCellRenderer() {
					@Override
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						// get existing label
						JLabel c = (JLabel) super
								.getTableCellRendererComponent(table,
										value, isSelected, hasFocus, row,
										column);

						//c.setOpaque(true);

						// every-other-line colors
						if (!isSelected)
							c.setBackground(row % 2 == 0 ? ODD_ROW_COLOR
									: Color.white);

						return c;
					}
				});
    }
    
	private void populateComponents() {
    	List<SiteRegion> regions = (List<SiteRegion>) App.dictionary.getDictionary("Regions");
    	List<SiteRegion> regionList = new ArrayList<SiteRegion>();

    	// single selection on site list
		lstSites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	
    	// Duplicate the region list; create an 'All Regions' 
    	regionList.add(new SiteRegion("ALL", "All Regions"));
    	
    	// null regions shouldn't happen, but be safe
    	if(regions != null) 
        	regionList.addAll(regions);
    	
    	// and make the regions combo box reflect it
		cboBrowseBy.setModel(new javax.swing.DefaultComboBoxModel(regionList.toArray()));
		
		// repopulate the site list when something is chosen...
        cboBrowseBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	populateSiteList();
            }
        });
		
		// Select the last used region otherwise select the first thing in the list
		// populate our site list (done automatically by choosing an index)
		if(App.prefs.getPref("corina.region.lastused") != null){
			Integer lastUsedIndex = Integer.parseInt(App.prefs.getPref("corina.region.lastused"));
			if(lastUsedIndex<cboBrowseBy.getItemCount()) cboBrowseBy.setSelectedIndex(lastUsedIndex);
        	populateSiteList();
		} else {
			cboBrowseBy.setSelectedIndex(0);
		}
		
		lstSites.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
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
				MeasurementSearchResource ms = new MeasurementSearchResource();
				for(int i = 0; i < selected.length; i++) {
					Site site = (Site) selected[i];
					ms.getSearchParameters().addSearchConstraint("siteid", "=", site.getID());
				}

				PrototypeLoadDialog dlg = new PrototypeLoadDialog(ms);
				
				// start our query (remotely)
				ms.query();		
				
				dlg.setVisible(true);
				
				if(!dlg.isSuccessful()) {
					new Bug(dlg.getFailException());
				} else {
					((DBBrowserTableModel)tblAvailMeas.getModel()).setElements(ms.getObject());
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
    
    private void populateSiteList() {
    	Collection<Site> sites = App.sites.getSites();
    	SiteRegion region = (SiteRegion) cboBrowseBy.getSelectedItem();
    	Site selectedSite = (Site) lstSites.getSelectedValue();
    	
    	// have we selected 'all regions?'
    	if(region.getInternalRepresentation().equals("ALL")) { 
    		
    		// User has NOT entered filter text
        	if(txtFilterInput.getText().equals("")){	
        		lstSites.setModel(new javax.swing.DefaultComboBoxModel(sites.toArray()));

        	// User HAS entered filter text
        	} else {
        		List<Site> filteredSites = new ArrayList<Site>();
        		
        		// Loop through master site list and check if filter matches
        		String filter = txtFilterInput.getText().toLowerCase();
        		for(Site s : sites){
	        		String search = s.toFullString().toLowerCase();
	        		if(search.indexOf(filter) != -1)
	        			filteredSites.add(s);        				
        		}
        		lstSites.setModel(new javax.swing.DefaultComboBoxModel(filteredSites.toArray()));
        	}		
    		
    	
        // A particular region has been selected so limit list before applying filter
    	} else {
    		List<Site> filteredSites = new ArrayList<Site>();
    		
    		// filter the sites...
    		for(Site s : sites) {
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
    	}
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSite != null) {
    		for(int i = 0; i < lstSites.getModel().getSize(); i++) {
    			if(((Site)lstSites.getModel().getElementAt(i)).equals(selectedSite)) {
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
    	Site site = App.sites.findSite(code);
    	
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