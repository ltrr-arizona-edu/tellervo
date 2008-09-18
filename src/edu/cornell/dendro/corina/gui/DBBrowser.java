package edu.cornell.dendro.corina.gui;

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
import javax.swing.event.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.SiteRegion;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleSummary;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.webdbi.MeasurementSearchResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;
import edu.cornell.dendro.corina.webdbi.ResourceEvent;
import edu.cornell.dendro.corina.webdbi.ResourceEventListener;

public class DBBrowser extends javax.swing.JDialog{
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
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
    	
    	returnStatus = RET_OK;
    	return true;
    }
        
    private void setupTableArea() {
		tblAvailMeas.setColumnSelectionAllowed(false);
		tblAvailMeas.setRowSelectionAllowed(true);
		tblAvailMeas.setModel(new DBBrowserTableModel());
		setupTableColumns(tblAvailMeas);

		tblChosenMeas.setColumnSelectionAllowed(false);
		tblChosenMeas.setRowSelectionAllowed(true);
		tblChosenMeas.setModel(new DBBrowserTableModel(selectedElements));
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
		
		table.getColumnModel().getColumn(0).setPreferredWidth(fm.stringWidth("C-XXX-XX-XX-X-X"));
		table.getColumnModel().getColumn(1).setPreferredWidth(fm.stringWidth("DirectX"));
		table.getColumnModel().getColumn(2).setPreferredWidth(fm.stringWidth("ABCD"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("Pinus Nigra X"));
		table.getColumnModel().getColumn(4).setPreferredWidth(fm.stringWidth("99"));
		table.getColumnModel().getColumn(5).setPreferredWidth(fm.stringWidth("2008-08-08"));
		table.getColumnModel().getColumn(6).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(7).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(8).setPreferredWidth(fm.stringWidth("123"));
		
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
	public final static Color ODD_ROW_COLOR = new Color(236, 243, 254);


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
		
		// select the first thing in the list
		// TODO: Should we store the last region we used?
		// populate our site list (done automatically by choosing an index)
		cboBrowseBy.setSelectedIndex(0);
		
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
    
    public class DBBrowserTableModel extends AbstractTableModel {
    	private ElementList elements;
        private final String[] columnNames = {
                "Name", 
                "Type", 
                "Site name", 
                "Taxon", 
                "#", 
                "Modified", 
                "Begin Date", 
                "End Date", 
                "n"
                //"ID" //useful for debugging
            };
        
        private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    	/**
    	 * The default: no elements
    	 */
    	public DBBrowserTableModel() {
    		this(new ElementList());
    	}
  
    	/**
    	 * 
    	 * @param elements
    	 */
    	public DBBrowserTableModel(ElementList elements) {
    		this.elements = elements;
    	}    	
    	
    	public void setElements(ElementList elements) {
    		this.elements = elements;
    		fireTableDataChanged();
    	}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return elements.size();
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			Element e = elements.get(rowIndex);
			BaseSample bs;
			
			try {
				bs = e.loadBasic();
			} catch (IOException ioe) {
				return "<ERROR>";
			}
			
			switch(columnIndex) {
			case 0: {
				SampleSummary ss = (SampleSummary) bs.getMeta("::summary");

				if(ss != null)
					return ss.getLabCode();
				
				return bs.hasMeta("title") ? bs.getMeta("title") : "[id: " + bs.getMeta("id") + "]";
			}

			case 1:
				return bs.getSampleType();
				
			case 2: {
				SampleSummary ss = (SampleSummary) bs.getMeta("::summary");
				return ss == null ? ss : ss.siteDescription();
			}

			case 3: {
				SampleSummary ss = (SampleSummary) bs.getMeta("::summary");
				return ss == null ? ss : ss.taxonDescription();
			}

			case 4: {
				SampleSummary ss = (SampleSummary) bs.getMeta("::summary");
				return ss == null ? ss : ss.getMeasurementCount();
			}
			
			case 5: {
				Date date = (Date) bs.getMeta("::moddate");
				return date != null ? dateFormat.format(date) : date;
			}

			case 6:
				return bs.getRange().getStart();
				
			case 7:
				return bs.getRange().getEnd();
			
			case 8:
				return bs.getRange().span();
				
			default:
				return null;
			}
		}
		
        public Class<?> getColumnClass(int c) {
        	return String.class;
        }
        
		public String getColumnName(int index) {
			return columnNames[index];
		}
		
		public Element getElementAt(int rowIndex) {
			return elements.get(rowIndex);
		}
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
        		for(Site s : sites){
        			if(s.toString().indexOf(txtFilterInput.getText())!=-1){      					
        				filteredSites.add(s);        				
        			}	
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
    	        			if(s.toString().indexOf(txtFilterInput.getText())!=-1){      					
    	        				filteredSites.add(s);        				
    	        			}	
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
    
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }
    
    /**
     * Get the JPanel that comprises space between invert...OK
     * Not initialized; must be laid out manually
     * @return
     */
    public JPanel getExtraButtonPanel() {
    	return extraButtonPanel;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        panelBrowseBy = new javax.swing.JPanel();
        browseSearchPane = new javax.swing.JTabbedPane();
        browsePanel = new javax.swing.JPanel();
        extraButtonPanel = new javax.swing.JPanel();
        cboBrowseBy = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSites = new javax.swing.JList();
        txtFilterInput = new javax.swing.JTextField();
        searchPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        workArea = new javax.swing.JPanel();
        tblAvailMeas = new javax.swing.JTable();
        tblChosenMeas = new javax.swing.JTable();
        btnSelectAll = new javax.swing.JButton();
        btnSelectNone = new javax.swing.JButton();
        btnInvertSelect = new javax.swing.JButton();

        setTitle("Measurement Browser");

        btnOk.setText("OK");
        btnCancel.setText("Cancel");

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setResizeWeight(0.2);
        jSplitPane1.setFocusable(false);

        browseSearchPane.setEnabled(false);

        cboBrowseBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Regions", "Mediterranean", "Greece", "Italy", "Turkey", "United States" }));
        cboBrowseBy.setToolTipText("Select a region to filter the sites list below");

        lstSites.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Aezanoi", "Afyon", "Yenikapi", "Yumuktepe" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstSites.setToolTipText("List of sites.  Select one or more to see the available datasets in the right hand table");
        jScrollPane1.setViewportView(lstSites);

        txtFilterInput.setToolTipText("Begin typing to filter the sites list above");

        org.jdesktop.layout.GroupLayout browsePanelLayout = new org.jdesktop.layout.GroupLayout(browsePanel);
        browsePanel.setLayout(browsePanelLayout);
        browsePanelLayout.setHorizontalGroup(
            browsePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cboBrowseBy, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
            .add(txtFilterInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );
        browsePanelLayout.setVerticalGroup(
            browsePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(browsePanelLayout.createSequentialGroup()
                .add(cboBrowseBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtFilterInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        browseSearchPane.addTab("Browse", browsePanel);

        org.jdesktop.layout.GroupLayout searchPanelLayout = new org.jdesktop.layout.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 129, Short.MAX_VALUE)
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 443, Short.MAX_VALUE)
        );

        browseSearchPane.addTab("Search", searchPanel);

        org.jdesktop.layout.GroupLayout panelBrowseByLayout = new org.jdesktop.layout.GroupLayout(panelBrowseBy);
        panelBrowseBy.setLayout(panelBrowseByLayout);
        panelBrowseByLayout.setHorizontalGroup(
            panelBrowseByLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, browseSearchPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        panelBrowseByLayout.setVerticalGroup(
            panelBrowseByLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(browseSearchPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(panelBrowseBy);

        tblAvailMeas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Direct", "ABC", "Pinus nigra", "1", "26/3/2008", "1950", "1999"},
                {"Sum", "ABC", "Pinus nigra", "4", "26/3/2008", "1304", "2001"},
                {"Chronology", "[3 sites]", "Pinus nigra", "13", "26/3/2008", "1304", "2005"},
                {"Chronology", "[3 sites]", "[2 species]", "5", "20/3/2008", "1850", "1953"}
            },
            new String [] {
                "Type", "Site name", "Taxon", "No. of Measurements", "Last modified", "Begin Date", "End Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblAvailMeas.setDragEnabled(true);
        tblAvailMeas.setRowSelectionAllowed(false);
        //resultsScrollPane.setViewportView(tblAvailMeas);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(workArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(workArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel1);

        btnSelectAll.setText("All");
        btnSelectAll.setToolTipText("Select all measurements in the table");

        btnSelectNone.setText("None");
        btnSelectNone.setToolTipText("Unselected all the measurements in the table ");

        btnInvertSelect.setText("Invert");
        btnInvertSelect.setToolTipText("Invert the selected items in the table");        
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnInvertSelect)
                    .add(btnSelectNone)
                    .add(btnSelectAll)
                    .add(extraButtonPanel)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancel))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {btnCancel, btnOk, btnSelectAll, btnSelectNone, btnInvertSelect}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(btnSelectAll)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSelectNone)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnInvertSelect)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(extraButtonPanel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 332, Short.MAX_VALUE)
                        .add(btnOk)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    /**
     * @param args the command line arguments
     */
    public static void zzzmain(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DBBrowser dialog = new DBBrowser(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel browsePanel;
    private javax.swing.JTabbedPane browseSearchPane;
    private javax.swing.JPanel extraButtonPanel;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnInvertSelect;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnAddAll;
    private javax.swing.JButton btnRemoveAll;    
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnSelectNone;
    private javax.swing.JComboBox cboBrowseBy;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField txtFilterInput;
    private javax.swing.JList lstSites;
    private javax.swing.JPanel panelBrowseBy;
    private javax.swing.JPanel workArea;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTable tblAvailMeas;
    private javax.swing.JTable tblChosenMeas;
    // End of variables declaration//GEN-END:variables
    
    private int returnStatus = RET_CANCEL;
}