package edu.cornell.dendro.corina.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.SiteRegion;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.webdbi.MeasurementSearchResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;

public class DBBrowser extends javax.swing.JDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
    private ElementList selectedElements;
    
    /** Creates new form */
    public DBBrowser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        selectedElements = new ElementList();
        
        populateComponents();
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
		
		// select the first thing in the list
		// TODO: Should we store the last region we used?
		// populate our site list (done automatically by choosing an index)
		cboBrowseBy.setSelectedIndex(0);
		
		lstSites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        
		tblAvailMeas.setColumnSelectionAllowed(false);
		tblAvailMeas.setRowSelectionAllowed(true);
		tblAvailMeas.setModel(new DBBrowserTableModel());
		
		tblAvailMeas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					doClose(RET_OK);
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
                "Elements", 
                "Last modified", 
                "Begin Date", 
                "End Date", 
                "ID"
            };

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
			case 0:
				return bs.hasMeta("title") ? bs.getMeta("title") : "[id: " + bs.getMeta("id") + "]";
				
			case 8:
				return bs.getMeta("id");
				
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
    	List<Site> sites = App.sites.getSites();
    	SiteRegion region = (SiteRegion) cboBrowseBy.getSelectedItem();
    	Site selectedSite = (Site) lstSites.getSelectedValue();

    	// have we selected 'all regions?'
    	if(region.getInternalRepresentation().equals("ALL")) {    		
    		lstSites.setModel(new javax.swing.DefaultComboBoxModel(sites.toArray()));
    	} else {
    		List<Site> filteredSites = new ArrayList<Site>();
    		
    		// filter the sites...
    		for(Site s : sites) {
    			if(s.inRegion(region.getInternalRepresentation()))
    				filteredSites.add(s);
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        panelBrowseBy = new javax.swing.JPanel();
        browseSearchPane = new javax.swing.JTabbedPane();
        browsePanel = new javax.swing.JPanel();
        cboBrowseBy = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSites = new javax.swing.JList();
        jTextField1 = new javax.swing.JTextField();
        searchPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        resultsScrollPane = new javax.swing.JScrollPane();
        tblAvailMeas = new javax.swing.JTable();
        btnSelectAll = new javax.swing.JButton();
        btnSelectNone = new javax.swing.JButton();
        btnInvertSelect = new javax.swing.JButton();

        setTitle("Measurement Browser");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setResizeWeight(0.05);
        jSplitPane1.setFocusable(false);

        browseSearchPane.setEnabled(false);

        cboBrowseBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Regions", "Mediterranean", "Greece", "Italy", "Turkey", "United States" }));
        cboBrowseBy.setToolTipText("Select a region to filter the sites list below");
        cboBrowseBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBrowseByActionPerformed(evt);
            }
        });

        lstSites.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Aezanoi", "Afyon", "Yenikapi", "Yumuktepe" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstSites.setToolTipText("List of sites.  Select one or more to see the available datasets in the right hand table");
        jScrollPane1.setViewportView(lstSites);

        jTextField1.setToolTipText("Begin typing to filter the sites list above");

        org.jdesktop.layout.GroupLayout browsePanelLayout = new org.jdesktop.layout.GroupLayout(browsePanel);
        browsePanel.setLayout(browsePanelLayout);
        browsePanelLayout.setHorizontalGroup(
            browsePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cboBrowseBy, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
            .add(jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );
        browsePanelLayout.setVerticalGroup(
            browsePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(browsePanelLayout.createSequentialGroup()
                .add(cboBrowseBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
        resultsScrollPane.setViewportView(tblAvailMeas);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel1);

        btnSelectAll.setText("All");
        btnSelectAll.setToolTipText("Select all measurements in the table");
        btnSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAllActionPerformed(evt);
            }
        });

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
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnInvertSelect)
                    .add(btnSelectNone)
                    .add(btnSelectAll)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancel))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {btnCancel, btnOk}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

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
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 332, Short.MAX_VALUE)
                        .add(btnOk)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        doClose(RET_OK);
}//GEN-LAST:event_btnOkActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        doClose(RET_CANCEL);
}//GEN-LAST:event_btnCancelActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void cboBrowseByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBrowseByActionPerformed
    	// repopulate our site list
    	populateSiteList();
    }//GEN-LAST:event_cboBrowseByActionPerformed

    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAllActionPerformed
    	tblAvailMeas.setRowSelectionInterval(0, tblAvailMeas.getRowCount() - 1);
    	
}//GEN-LAST:event_btnSelectAllActionPerformed
    
    private void doClose(int retStatus) {
    	int selectedRows[] = tblAvailMeas.getSelectedRows();
    	
    	// create a list of our selected elements
    	for(int i = 0; i < selectedRows.length; i++)
    		selectedElements.add(((DBBrowserTableModel)tblAvailMeas.getModel()).
    				getElementAt(selectedRows[i]));
    	
    	
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnInvertSelect;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnSelectNone;
    private javax.swing.JComboBox cboBrowseBy;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList lstSites;
    private javax.swing.JPanel panelBrowseBy;
    private javax.swing.JScrollPane resultsScrollPane;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTable tblAvailMeas;
    // End of variables declaration//GEN-END:variables
    
    private int returnStatus = RET_CANCEL;
}