package edu.cornell.dendro.corina.admin;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;
import javax.xml.datatype.XMLGregorianCalendar;

import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.editor.ScanBarcodeUI;
import edu.cornell.dendro.corina.editor.EditorFactory.BarcodeDialogResult;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.util.labels.ui.TridasListCellRenderer;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

/**
 *
 * @author  peterbrewer
 */
public class BoxCuration extends javax.swing.JDialog implements KeyListener, ActionListener, MouseListener{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isNewRecord = false;
	
	public enum BoxCurationType{
		BROWSE,
		CHECKOUT,
		CHECKIN;
	
	}
	
	private WSIBox box = null;
	private BoxCurationType type = BoxCurationType.BROWSE;
	private ArrayListModel<WSIBox> boxModel;
	protected SampleListTableModel sampleTableModel = new SampleListTableModel();
	private TableRowSorter<SampleListTableModel> sampleTableSorter;

    /**
     * Standard constructor creates a GUI ready for user to pick a box
     * 
     * @param parent
     * @param modal
     */
    public BoxCuration(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setupGui();

    }
    
    /**
     * Constructor for creating a GUI with a box pre-selected 
     * 
     * @param parent
     * @param modal
     * @param box
     */
    public BoxCuration(java.awt.Frame parent, boolean modal, WSIBox box) {
        super(parent, modal);
        initComponents();
        setupGui();
        this.box = box;
        this.lblScanOrSelect.setVisible(false);
        this.txtBarcode.setVisible(false);
        this.cboBox.setVisible(false);
        updateBoxGui();
    }
    
    /**
     * Set the purpose 
     * @param type
     */
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
    
    private void updateSampleCount()
    {
    	if(box.getSamples().size()==1)
    	{
    		lblSampleCount.setText(box.getSamples().size()+" sample in box");
    	}
    	else
    	{
    		lblSampleCount.setText(box.getSamples().size()+" samples in box");
    	}
    	
    }
    
    private boolean saveBoxDetailsForSample(TridasSample s)
    {
    	// Update boxid in sample genericField
    	if(s.isSetGenericFields())
    	{
    		for(TridasGenericField gf: s.getGenericFields())
    		{
    			if(gf.getName().equals("corina.boxID"))
    			{
    				if(gf.getValue().equals(box.getIdentifier().getValue()))
    				{
    					// This sample already belongs in this box
    					JOptionPane.showConfirmDialog(null,
        						"This sample is already assigned to this box!",
        						"Information",
        						JOptionPane.OK_OPTION, 
        						JOptionPane.INFORMATION_MESSAGE);
    					return true;
    				}
    				
    				int response =  JOptionPane.showConfirmDialog(null,
    						"This sample is already assigned to a box.\n"
    						+"Do you want to reassign to box "+box.getTitle()+"?",
    						"Sample already assigned to box",
    						JOptionPane.YES_NO_CANCEL_OPTION, 
    						JOptionPane.ERROR_MESSAGE);
    				
    				switch(response)
    				{    					
    				case JOptionPane.NO_OPTION:
    				case JOptionPane.CANCEL_OPTION:
    					return false;  					
    				}
    				
    				gf.setValue(box.getIdentifier().getValue());
    			}
    		}
    	}
    	else
    	{
    		s.setGenericFields(new ArrayList<TridasGenericField>());
    		TridasGenericField gf= new TridasGenericField();
    		gf.setName("corina.boxID");
    		gf.setType("xs:string");
    		gf.setValue(box.getIdentifier().getValue());				
    		s.getGenericFields().add(gf);
    	}
    	
    	
		// Create resource
		 EntityResource<TridasSample> resource = new EntityResource<TridasSample>(s, CorinaRequestType.UPDATE, TridasSample.class);
		
		// set up a dialog...
		CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(this, resource);
		
		resource.query();
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			Alert.message("Error", "Error adding sample to box");
			return false;
		}
    	
		return true;

    	
    }
    
    /**
     * Save any changes to the current box
     * 
     * @return
     */
    private Boolean saveChanges()
    {
		box.setTitle(txtName.getText());
		box.setCurationLocation(txtCurationLocation.getText());
		box.setTrackingLocation(txtTrackingLocation.getText());
		box.setComments(txtComments.getText());
		
		// Create resource
		EntityResource<WSIBox> resource;
		if(isNewRecord)
		{
			resource = new EntityResource<WSIBox>(box, CorinaRequestType.CREATE, WSIBox.class);
		}
		else
		{
			resource = new EntityResource<WSIBox>(box, CorinaRequestType.UPDATE, WSIBox.class);
		}
		
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
		btnApply.setEnabled(false);
		this.isNewRecord = false;

		return true;
    }
    
    /**
     * Setup the GUI
     */
    @SuppressWarnings("unchecked")
	public void setupGui()
    {
    	
    	
    	// Hide main gui components until populated
    	this.setComponentsEnabled(false);
    	
    	//this.cboBox.setVisible(false);
    	this.btnApply.setEnabled(false);
    	this.setTitle("Box details");
    	
    	// Set up box list model etc
    	ArrayList<WSIBox> boxList = (ArrayList<WSIBox>) Dictionary.getDictionaryAsArrayList("boxDictionary");
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(boxList, numSorter);
    	boxModel = new ArrayListModel<WSIBox>(boxList);
    	TridasListCellRenderer tlcr = new TridasListCellRenderer();
    	cboBox.setModel(boxModel);
    	cboBox.setRenderer(tlcr);
    	cboBox.setEditable(false);
	
    	// Key Listeners
    	this.txtName.addKeyListener(this);
    	this.txtCurationLocation.addKeyListener(this);
    	this.txtTrackingLocation.addKeyListener(this);
    	this.txtComments.addKeyListener(this);
    	this.txtBarcode.addKeyListener(this);
    	
    	// Action Listeners
    	this.btnAddSampleToBox.addActionListener(this);
    	this.btnCreateNewBox.addActionListener(this);
    	this.btnApply.addActionListener(this);
    	this.btnOk.addActionListener(this);
    	this.cboBox.addActionListener(this);
    	
    	// Update the GUI
    	updateBoxGui();
    }
    
    /**
     * Check whether the details have changed and enabled/disable apply button
     * accordingly
     */
    private void haveDetailsChanged()
    {
    	Boolean changed = false;
    
    	// Do not enable apply button for a new record until the name field has been 
    	// filled in
    	if(isNewRecord)
    	{
    		if(txtName.getText().length()==0)
    		{
    			btnApply.setEnabled(false);
    			return;
    		}
    	}
    	
    	// Check if name has changed
    	if(box.isSetTitle())
    	{
    		if(!txtName.getText().equals(box.getTitle())) changed = true;
    	}
    	else
    	{
    		if(!txtName.getText().equals("")) changed = true;
    	}
    	
    	// Check if curation location has changed
    	if(box.isSetCurationLocation())
    	{
    		if(!txtCurationLocation.getText().equals(box.getCurationLocation())) changed = true;
    	}
    	else
    	{
    		if(!txtCurationLocation.getText().equals("")) changed = true;
    	}
    	
    	// Check if tracking location has changed
    	if(box.isSetTrackingLocation())
    	{
    		if(!txtTrackingLocation.getText().equals(box.getTrackingLocation())) changed = true;
    	}
    	else
    	{
    		if(!txtTrackingLocation.getText().equals("")) changed = true;
    	}

    	// Check if comments have changed
    	if(box.isSetComments())
    	{
    		if(!txtComments.getText().equals(box.getComments())) changed = true;
    	}
    	else
    	{
    		if(!txtComments.getText().equals("")) changed = true;
    	}

    	
    	this.btnApply.setEnabled(changed);

    }
    
    private void getComprehensiveWSIBox(String idstr)
    {
		// Set return type to box
    	SearchParameters param = new SearchParameters(SearchReturnObject.BOX);
		
    	// Set box id
    	param.addSearchConstraint(SearchParameterName.BOXID, SearchOperator.EQUALS, idstr);

		TridasIdentifier id = new TridasIdentifier();
		id.setValue(idstr);
		
    	// we want a box returned here
		EntityResource<WSIBox> resource = new EntityResource<WSIBox>(id, EntityType.BOX, CorinaRequestType.READ, WSIBox.class);

		// Query db 
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("Error getting boxes");
			return;
		}
		
		box = resource.getAssociatedResult();
    }
    
    /**
     * Get box details from WS using barcode
     * 
     * @param barcode
     */
    private void doBarcodeSearch(LabBarcode.DecodedBarcode barcode)
    {
		if (barcode.uuidType == LabBarcode.Type.BOX){	
			this.getComprehensiveWSIBox(barcode.uuid.toString());
		}
   	
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
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("d mmm yyyy");
		addComment("Checked out by "+App.currentUser.getFirstName()+" "+App.currentUser.getLastName()+ " on " + dateFormat.format(c.getTime()) +".");
    }
    
    private void setFieldsForCheckin()
    {
		this.txtTrackingLocation.setText(" ");
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("d mmm yyyy");
		addComment("Checked in by "+App.currentUser.getFirstName()+" "+App.currentUser.getLastName()+ " on " + dateFormat.format(c.getTime()) +".");    }
    
    
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
    
    private void updateSampleTable()
    {        
    	if(box.isSetSamples())
    	{
    		ArrayList<TridasSample> samplelist = (ArrayList<TridasSample>) box.getSamples();
    		sampleTableModel = new SampleListTableModel(samplelist);
    	}
    	else
    	{
    		sampleTableModel = new SampleListTableModel(new ArrayList<TridasSample>());
    	}
    	
    	this.tblBoxContents.setModel(sampleTableModel);
    	tblBoxContents.removeColumn(tblBoxContents.getColumn("Box"));
    	tblBoxContents.removeColumn(tblBoxContents.getColumn("Box Curation Location"));
    	tblBoxContents.removeColumn(tblBoxContents.getColumn("Box Tracking Location"));
    	
    	updateSampleCount();
    }
    
    /**
     * Helper to enable or disable all GUI components
     * 
     * @param b
     */
    private void setComponentsEnabled(Boolean b)
    {
		this.tabbedPaneBox.setEnabled(b);
		this.panelBoxDetails.setEnabled(b);
		this.panelContents.setEnabled(b);
		this.txtName.setEnabled(b);
		this.txtLastUpdated.setEnabled(b);
		this.txtCurationLocation.setEnabled(b);
		this.txtTrackingLocation.setEnabled(b);
		this.txtComments.setEnabled(b);
		this.lblName.setEnabled(b);
		this.lblLastUpdated.setEnabled(b);
		this.lblComments.setEnabled(b);
		this.lblCurationLocation.setEnabled(b);
		this.lblTrackingLocation.setEnabled(b);
    }
    
    /**
     * Update the box details in the GUI 
     */
    private void updateBoxGui()
    {
    	// First clear components
		this.txtName.setText("");
		this.txtLastUpdated.setText("");
		this.txtCurationLocation.setText("");
		this.txtTrackingLocation.setText("");
		this.txtComments.setText("");
		
    	if(box==null)
    	{
    		// Box is null so hide and disable components
    		setComponentsEnabled(false);
			this.btnAddSampleToBox.setEnabled(false);
    		return;
    	}
    	 
    	// Box is populated so show and enable components
    	this.btnAddSampleToBox.setEnabled(true);
    	setComponentsEnabled(true);
    	
    	// Set components values
    	if(box.isSetTitle()) 				 this.txtName.setText(box.getTitle());
    	if(box.isSetLastModifiedTimestamp()) this.txtLastUpdated.setText(BoxCuration.formatXMLGregorianCalendar(box.getLastModifiedTimestamp().getValue()));
    	if(box.isSetCurationLocation()) 	 this.txtCurationLocation.setText(box.getCurationLocation());
    	if(box.isSetTrackingLocation()) 	 this.txtTrackingLocation.setText(box.getTrackingLocation());
    	if(box.isSetComments()) 			 this.txtComments.setText(box.getComments());
        
    	// Update table 
    	updateSampleTable();
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
        tabbedPaneBox = new javax.swing.JTabbedPane();
        panelBoxDetails = new javax.swing.JPanel();
        txtLastUpdated = new javax.swing.JTextField();
        lblLastUpdated = new javax.swing.JLabel();
        txtCurationLocation = new javax.swing.JTextField();
        txtTrackingLocation = new javax.swing.JTextField();
        lblCurationLocation = new javax.swing.JLabel();
        lblTrackingLocation = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtComments = new javax.swing.JTextPane();
        panelContents = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBoxContents = new javax.swing.JTable();
        btnAddSampleToBox = new javax.swing.JButton();
        lblSampleCount = new javax.swing.JLabel();
        txtBarcode = new javax.swing.JTextField();
        btnCreateNewBox = new javax.swing.JButton();
        lblSelectBox = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "abbb", "cbbbb", "ghhh", "jjjjj" }));

        lblScanOrSelect.setText("Scan barcode:");

        btnOk.setText("OK");

        btnApply.setText("Apply");

        txtLastUpdated.setEditable(false);

        lblLastUpdated.setText("Last updated:");

        lblCurationLocation.setText("Permanent location:");

        lblTrackingLocation.setText("Current location:");

        lblComments.setText("Comments:");

        lblName.setText("Name:");

        jScrollPane3.setViewportView(txtComments);

        org.jdesktop.layout.GroupLayout panelBoxDetailsLayout = new org.jdesktop.layout.GroupLayout(panelBoxDetails);
        panelBoxDetails.setLayout(panelBoxDetailsLayout);
        panelBoxDetailsLayout.setHorizontalGroup(
            panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelBoxDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                    .add(panelBoxDetailsLayout.createSequentialGroup()
                        .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblCurationLocation)
                            .add(lblLastUpdated)
                            .add(lblTrackingLocation)
                            .add(lblName))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelBoxDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                            .add(txtTrackingLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                            .add(txtLastUpdated, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                            .add(txtCurationLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)))
                    .add(lblComments))
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
                .add(18, 18, 18)
                .add(lblComments)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPaneBox.addTab("Details", panelBoxDetails);

        tblBoxContents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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

        btnAddSampleToBox.setText("Add sample to box");

        lblSampleCount.setText("0 samples in box");

        org.jdesktop.layout.GroupLayout panelContentsLayout = new org.jdesktop.layout.GroupLayout(panelContents);
        panelContents.setLayout(panelContentsLayout);
        panelContentsLayout.setHorizontalGroup(
            panelContentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelContentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelContentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panelContentsLayout.createSequentialGroup()
                        .add(lblSampleCount)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 202, Short.MAX_VALUE)
                        .add(btnAddSampleToBox)))
                .addContainerGap())
        );
        panelContentsLayout.setVerticalGroup(
            panelContentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelContentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelContentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnAddSampleToBox)
                    .add(lblSampleCount))
                .addContainerGap())
        );

        tabbedPaneBox.addTab("Contents", panelContents);

        btnCreateNewBox.setText("Create New Box");

        lblSelectBox.setText("...or select box:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, tabbedPaneBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 506, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(btnCreateNewBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 197, Short.MAX_VALUE)
                        .add(btnApply)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnOk)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(lblSelectBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(18, 18, 18))
                            .add(layout.createSequentialGroup()
                                .add(lblScanOrSelect, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                .add(23, 23, 23)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, txtBarcode, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, cboBox, 0, 390, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblScanOrSelect)
                    .add(txtBarcode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSelectBox)
                    .add(cboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(tabbedPaneBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnOk)
                    .add(btnCreateNewBox)
                    .add(btnApply))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    /**
     * Show the actual dialog empty ready for user to pick a box
     */
    public static void showDialog() {
        BoxCuration dialog = new BoxCuration(new javax.swing.JFrame(), false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    /**
     * Show the actual dialog pre-populated with a box
     */
    public static void showDialog(final BoxCurationType type) {
        BoxCuration dialog = new BoxCuration(new javax.swing.JFrame(), false);
        dialog.setCurationType(type);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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
    protected javax.swing.JButton btnAddSampleToBox;
    protected javax.swing.JButton btnApply;
    protected javax.swing.JButton btnCreateNewBox;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JComboBox cboBox;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JScrollPane jScrollPane3;
    protected javax.swing.JLabel lblComments;
    protected javax.swing.JLabel lblCurationLocation;
    protected javax.swing.JLabel lblLastUpdated;
    protected javax.swing.JLabel lblName;
    protected javax.swing.JLabel lblSampleCount;
    protected javax.swing.JLabel lblScanOrSelect;
    protected javax.swing.JLabel lblSelectBox;
    protected javax.swing.JLabel lblTrackingLocation;
    protected javax.swing.JPanel panelBoxDetails;
    protected javax.swing.JPanel panelContents;
    protected javax.swing.JTabbedPane tabbedPaneBox;
    protected javax.swing.JTable tblBoxContents;
    protected javax.swing.JTextField txtBarcode;
    protected javax.swing.JTextPane txtComments;
    protected javax.swing.JTextField txtCurationLocation;
    protected javax.swing.JTextField txtLastUpdated;
    protected javax.swing.JTextField txtName;
    protected javax.swing.JTextField txtTrackingLocation;
    // End of variables declaration//GEN-END:variables

    /**
     * Display warning message that there have been changes
     * that have not yet been saved.
     */
	private int confirmLossOfData()
	{
		return  JOptionPane.showConfirmDialog(null,
				"Changes have not been saved.\n"
				+"Do you want to continue without saving?",
				"Close without saving?",
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.ERROR_MESSAGE);
	}
    
	/**
	 * Format an XMLGregorianCalendar into a human readable date time string
	 * 
	 * @param xmlcal
	 * @return
	 */
	public static String formatXMLGregorianCalendar(XMLGregorianCalendar xmlcal)
	{
		GregorianCalendar c = xmlcal.toGregorianCalendar();
	    String DATE_FORMAT = "d MMMMM yyyy, kk:mm";
	    SimpleDateFormat sdf =
	          new SimpleDateFormat(DATE_FORMAT);
	    return sdf.format(c.getTime());
	}


	/**
	 *  LISTENERS
	 */
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnCreateNewBox))
		{
			if(btnApply.isEnabled())
			{
				int response = confirmLossOfData();
				switch(response)
				{
				case JOptionPane.NO_OPTION:
				case JOptionPane.CANCEL_OPTION:
					return;		
				}
			}
			isNewRecord = true;
			box = new WSIBox();
			this.btnAddSampleToBox.setEnabled(false);
			updateBoxGui();
		}
		else if (e.getSource().equals(btnOk))
		{
			if(btnApply.isEnabled())
			{					
				int response = confirmLossOfData(); 
				switch(response)
				{
				case JOptionPane.NO_OPTION:
				case JOptionPane.CANCEL_OPTION:
					return;		
				}
			}
			dispose();
		}
		else if (e.getSource().equals(btnApply))
		{
			saveChanges();
		}
		else if (e.getSource().equals(cboBox))
		{
			WSIBox bx = (WSIBox) cboBox.getSelectedItem();
			this.getComprehensiveWSIBox(bx.getIdentifier().getValue());
			updateBoxGui();
		}
		else if (e.getSource().equals(btnAddSampleToBox))
		{
			final JDialog dialog = new JDialog();
			final ScanBarcodeUI barcodeUI = new ScanBarcodeUI(dialog);
			
			dialog.setTitle(I18n.getText("barcode"));
			dialog.setIconImage(Builder.getApplicationIcon());
			dialog.setContentPane(barcodeUI);
			dialog.setResizable(false);
			dialog.pack();
			dialog.setModal(true);
			Center.center(dialog);
			dialog.setVisible(true);
			BarcodeDialogResult result = barcodeUI.getResult();
			
			// no success, so just ignore..
			if(!result.success){
				return;
			}
			
			// Add sample to our box and save
			if(!box.isSetSamples())
			{
				box.setSamples(new ArrayList<TridasSample>());
			}
			box.getSamples().add(result.sample);
			saveBoxDetailsForSample(result.sample);
						
			this.getComprehensiveWSIBox(box.getIdentifier().getValue());
			updateBoxGui();
		
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) {	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getSource().equals(txtBarcode))
		{
			String newSelection = txtBarcode.getText();
			
			
       		if(newSelection.length()==24)
    		{
	
    			// A barcode was probably just scanned	
				try {
					LabBarcode.DecodedBarcode barcode = LabBarcode.decode(newSelection);
					
					cboBox.setSelectedIndex(-1);
					txtBarcode.setText("");
					
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
		else
		{
			haveDetailsChanged();
		}
	}
 
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
