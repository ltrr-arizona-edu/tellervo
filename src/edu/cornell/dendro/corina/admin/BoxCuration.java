package edu.cornell.dendro.corina.admin;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.datatype.XMLGregorianCalendar;

import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

/**
 *
 * @author  peterbrewer
 */
public class BoxCuration extends javax.swing.JDialog implements KeyListener{
    
	public enum BoxCurationType{
		BROWSE,
		CHECKOUT,
		CHECKIN;
	
	}
	
	private WSIBox box;
	private WSIBox boxChanged;
	private BoxCurationType type = BoxCurationType.BROWSE;
	
    /** Creates new form BoxLookup */
    public BoxCuration(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setupGui();
    }
    
    public void setCurationType(BoxCurationType type)
    {
    	this.type = type;
    	
    	if ((type==BoxCurationType.CHECKIN) || (type==BoxCurationType.CHECKOUT))
    	{
    		this.tabbedPaneBox.setVisible(false);
    		this.btnApply.setVisible(false);
    		this.pack();
    	}
    	
    	if(type==BoxCurationType.CHECKIN)
    	{
    		this.setTitle("Check in box");
    	}
    	
    	if(type==BoxCurationType.CHECKOUT)
    	{
    		this.setTitle("Check out box");
    	}
    }
    
    private Boolean saveChanges()
    {
		box.setTitle(txtName.getText());
		box.setCurationLocation(txtCurationLocation.getText());
		box.setTrackingLocation(txtTrackingLocation.getText());
		box.setComments(txtComments.getText());
		box.setSamples(null);
		
    	// we want a box returned here
		EntityResource<WSIBox> resource = new EntityResource<WSIBox>(box, CorinaRequestType.UPDATE, WSIBox.class);
		
		// set up a dialog...
		Window parentWindow = SwingUtilities.getWindowAncestor(getParent());
		CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);
		
		resource.query();
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			Alert.message("Error", "Error updating box");
			return false;
		}
		
		box = resource.getAssociatedResult();
		updateBoxGui();
		return true;
    }
    
    /**
     * Setup the GUI
     */
    public void setupGui()
    {
    	this.btnApply.setEnabled(false);
    	this.cboBox.setSelectedIndex(-1);
    	this.setTitle("Box details");
    	this.tblBoxContents.setEnabled(false);
    	
    	
    	// Apply changes to box details
    	this.btnApply.addActionListener(new ActionListener()
    	{
			@Override
			public void actionPerformed(ActionEvent e) {
					
				saveChanges();
			}
    	});
    	
    	// Close dialog
    	this.btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(btnApply.isEnabled())
				{					
					int response  = JOptionPane.showConfirmDialog(null,
							"Changes have not been saved.\n"
							+"Do you want to close without saving?",
							"Close without saving?",
							JOptionPane.YES_NO_CANCEL_OPTION, 
							JOptionPane.ERROR_MESSAGE);
					switch(response)
					{
					case JOptionPane.NO_OPTION:
					case JOptionPane.CANCEL_OPTION:
						return;		
					}
				}
				dispose();
			}
    	});
    	
    	this.txtName.addKeyListener(this);
    	this.txtCurationLocation.addKeyListener(this);
    	this.txtTrackingLocation.addKeyListener(this);
    	this.txtComments.addKeyListener(this);
    	
    	this.cboBox.addActionListener(new ActionListener(){
    		@Override
			public void actionPerformed(ActionEvent e) {
				String newSelection = (String)cboBox.getSelectedItem();
				
				
	       		if(newSelection.length()==24)
        		{
		
        			// A barcode was probably just scanned	
    				try {
    					LabBarcode.DecodedBarcode barcode = LabBarcode.decode(newSelection);
    					
    					cboBox.setSelectedIndex(-1);
    					
    					if (!(barcode.uuidType == LabBarcode.Type.BOX))
    					{
        					Alert.message("Barcode error", "This was not a valid box barcode.");
        					return;
    					}
    					    					
    					doBarcodeSearch(barcode);
    					
    				} catch (IllegalArgumentException iae) {
    					Alert.message("Barcode error", "There was a problem with the barcode you scanned:\n"+iae.getMessage());
    				}	
        		}
			}
    	});
    	
    }
    
    /**
     * Check whether the details have changed and enabled/disable apply button
     * accordingly
     */
    private void haveDetailsChanged()
    {
    	if (   (!this.txtName.getText().equals(box.getTitle()))
    		|| (!this.txtCurationLocation.getText().equals(box.getCurationLocation()))
    		|| (!this.txtTrackingLocation.getText().equals(box.getTrackingLocation()))
    		|| (!this.txtComments.getText().equals(box.getComments()))
    	    )
		{
    		this.btnApply.setEnabled(true);
		}
    	else
    	{
    		this.btnApply.setEnabled(false);
    	}
    	
    	
    }
    
    /**
     * Get box details from WS using barcode
     * 
     * @param barcode
     */
    private void doBarcodeSearch(LabBarcode.DecodedBarcode barcode)
    {
		// Set return type to box
    	SearchParameters param = new SearchParameters(SearchReturnObject.BOX);
		
    	// Set box id
		if (barcode.uuidType == LabBarcode.Type.BOX){	
	    	param.addSearchConstraint(SearchParameterName.BOXID, SearchOperator.EQUALS, barcode.uuid.toString());
		}
		else
		{
			return;
		}
		TridasIdentifier id = new TridasIdentifier();
		id.setValue(barcode.uuid.toString());
		
    	// we want a box returned here
		EntityResource<WSIBox> resource = new EntityResource<WSIBox>(id, EntityType.BOX, CorinaRequestType.READ, WSIBox.class);

		// Query db 
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting boxes");
			return;
		}
		
		box = resource.getAssociatedResult();
		
		updateBoxGui();

		if (type.equals(BoxCurationType.CHECKOUT))
		{
			setFieldsForCheckout();
			if(saveChanges())
			{
				dispose();
			}
		}
		else if (type.equals(BoxCurationType.CHECKIN))
		{
			setFieldsForCheckin();

			if(saveChanges())
			{
				dispose();
			}
			
		}
			
		
    }
    
    private void setFieldsForCheckout()
    {
		this.txtTrackingLocation.setText("Checked out");
		addComment("Checked out by "+App.currentUser.getFirstName()+" "+App.currentUser.getLastName()+ " on ");
    }
    
    private void setFieldsForCheckin()
    {
		this.txtTrackingLocation.setText(" ");
		addComment("Checked in by "+App.currentUser.getFirstName()+" "+App.currentUser.getLastName()+ " on ");
    }
    
    
    private void addComment(String comment)
    {
    	if(this.txtComments.getText().length()>0)
    	{
    		this.txtComments.setText(this.txtComments.getText()+System.getProperty( "line.separator" )+comment);
    	}
    	else
    	{
    		this.txtComments.setText(comment);
    	}
    }
    
    /**
     * Update the box details in the GUI 
     */
    private void updateBoxGui()
    {
    	if(box==null)
    	{
    		this.txtName.setText("");
    		this.txtLastUpdated.setText("");
    		this.txtCurationLocation.setText("");
    		this.txtTrackingLocation.setText("");
    		this.txtComments.setText("");
    		this.btnApply.setEnabled(false);
    		boxChanged = null;
    		return;
    	}
    	    	
    	String val = box.getTitle();
    	
    	this.txtName.setText(box.getTitle());
    	this.txtLastUpdated.setText(BoxCuration.formatXMLGregorianCalendar(box.getLastModifiedTimestamp().getValue()));
    	this.txtCurationLocation.setText(box.getCurationLocation());
    	this.txtTrackingLocation.setText(box.getTrackingLocation());
    	this.txtComments.setText(box.getComments());
    	boxChanged = box;
    	haveDetailsChanged();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboBox = new javax.swing.JComboBox();
        lblScanOrSelect = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        tabbedPaneBox = new javax.swing.JTabbedPane();
        panelBoxDetails = new javax.swing.JPanel();
        txtLastUpdated = new javax.swing.JTextField();
        lblLastUpdated = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtComments = new javax.swing.JTextArea();
        txtCurationLocation = new javax.swing.JTextField();
        txtTrackingLocation = new javax.swing.JTextField();
        lblCurationLocation = new javax.swing.JLabel();
        lblTrackingLocation = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        panelContents = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBoxContents = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cboBox.setEditable(true);
        cboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "abbb", "cbbbb", "ghhh", "jjjjj" }));

        lblScanOrSelect.setText("Scan barcode or select box:");

        btnOk.setText("OK");

        btnApply.setText("Apply");

        btnSearch.setText("Search");

        txtLastUpdated.setEditable(false);

        lblLastUpdated.setText("Last updated:");

        txtComments.setColumns(20);
        txtComments.setRows(5);
        jScrollPane1.setViewportView(txtComments);

        lblCurationLocation.setText("Permanent location:");

        lblTrackingLocation.setText("Current location:");

        lblComments.setText("Comments:");

        lblName.setText("Name:");

        org.jdesktop.layout.GroupLayout panelBoxDetailsLayout = new org.jdesktop.layout.GroupLayout(panelBoxDetails);
        panelBoxDetails.setLayout(panelBoxDetailsLayout);
        panelBoxDetailsLayout.setHorizontalGroup(
            panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelBoxDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblCurationLocation)
                    .add(lblLastUpdated)
                    .add(lblTrackingLocation)
                    .add(lblComments)
                    .add(lblName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .add(txtTrackingLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .add(txtLastUpdated, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .add(txtCurationLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelBoxDetailsLayout.setVerticalGroup(
            panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelBoxDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblName)
                    .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblLastUpdated)
                    .add(txtLastUpdated, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCurationLocation)
                    .add(txtCurationLocation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTrackingLocation)
                    .add(txtTrackingLocation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblComments)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPaneBox.addTab("Details", panelBoxDetails);

        panelContents.setEnabled(false);

        tblBoxContents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"C-XYZ-1-A", null}
            },
            new String [] {
                "Sample", "Check Presence"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblBoxContents);

        org.jdesktop.layout.GroupLayout panelContentsLayout = new org.jdesktop.layout.GroupLayout(panelContents);
        panelContents.setLayout(panelContentsLayout);
        panelContentsLayout.setHorizontalGroup(
            panelContentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelContentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelContentsLayout.setVerticalGroup(
            panelContentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelContentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPaneBox.addTab("Contents", panelContents);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, tabbedPaneBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 459, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(btnApply)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnOk)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblScanOrSelect, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(cboBox, 0, 362, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnSearch)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblScanOrSelect)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnSearch)
                    .add(cboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(tabbedPaneBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(btnOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    /**
     * Show the actual dialog
     */
    public static void showDialog() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BoxCuration dialog = new BoxCuration(new javax.swing.JFrame(), true);

                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                
            }

        });
    }
    
    /**
     * Show the actual dialog
     */
    public static void showDialog(final BoxCurationType type) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BoxCuration dialog = new BoxCuration(new javax.swing.JFrame(), true);
                dialog.setCurationType(type);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                
            }

        });
    }
    
    /**
     * Checkout the box from store
     */
    public static void checkoutBox(){
    	
    	showDialog(BoxCurationType.CHECKOUT);
    	
    }
    
    /**
     * Checkin the box to store
     */
    public static void checkinBox(){
    	
    	showDialog(BoxCurationType.CHECKIN);
    	
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnApply;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JButton btnSearch;
    protected javax.swing.JComboBox cboBox;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JLabel lblComments;
    protected javax.swing.JLabel lblCurationLocation;
    protected javax.swing.JLabel lblLastUpdated;
    protected javax.swing.JLabel lblName;
    protected javax.swing.JLabel lblScanOrSelect;
    protected javax.swing.JLabel lblTrackingLocation;
    protected javax.swing.JPanel panelBoxDetails;
    protected javax.swing.JPanel panelContents;
    protected javax.swing.JTabbedPane tabbedPaneBox;
    protected javax.swing.JTable tblBoxContents;
    protected javax.swing.JTextArea txtComments;
    protected javax.swing.JTextField txtCurationLocation;
    protected javax.swing.JTextField txtLastUpdated;
    protected javax.swing.JTextField txtName;
    protected javax.swing.JTextField txtTrackingLocation;
    // End of variables declaration//GEN-END:variables

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) {	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		haveDetailsChanged();
	}
 
	public static String formatXMLGregorianCalendar(XMLGregorianCalendar xmlcal)
	{
		 GregorianCalendar c = xmlcal.toGregorianCalendar();
		
		    String DATE_FORMAT = "dd MMM yyyy, kk:mm";
		    SimpleDateFormat sdf =
		          new SimpleDateFormat(DATE_FORMAT);

		    return sdf.format(c.getTime());


	}
}
