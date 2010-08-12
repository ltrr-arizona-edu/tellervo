package edu.cornell.dendro.corina.admin;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;

import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.dbbrowse.SiteRenderer;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.util.labels.ui.TridasListCellRenderer;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;

/**
 * GUI class to allow users to find the physical location of the sample they are
 * interested in.   
 *
 * @author  peterbrewer
 */
public class SampleCuration extends javax.swing.JDialog implements ActionListener{
    
	ButtonGroup group = new ButtonGroup();
	protected ArrayListModel<TridasObject> objModel = new ArrayListModel<TridasObject>();
	protected ArrayListModel<TridasElement> elModel = new ArrayListModel<TridasElement>();
	protected ArrayListModel<TridasSample> sampModel = new ArrayListModel<TridasSample>();
	protected SampleListTableModel resultModel = new SampleListTableModel();
	
	
    /** Creates new form SampleCuration */
    public SampleCuration(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();   


    }
    

    /**
     * Show the actual dialog
     */
    public static void showDialog() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final SampleCuration dialog = new SampleCuration(new javax.swing.JFrame(), false);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
        
                dialog.setupGui();
                dialog.internationalizeComponents();
                dialog.pack();
                dialog.txtBarcode.requestFocusInWindow();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            }
        });
    }

    private void internationalizeComponents()
    {
    	
    
    }
    
    /**
     * Setup the GUI.  Called from showDialog()
     */
    private void setupGui()
    {    	
        setIconImage(Builder.getApplicationIcon());
    	
    	// Default to barcode searching
    	setManualSearch(false);
    	
        // Group radio buttons
        group.add(this.radBarcode);
        group.add(this.radManual);
        
        // Add action listeners
        radBarcode.addActionListener(this);
        radManual.addActionListener(this);
        btnOk.addActionListener(this);
        btnPopulateElements.addActionListener(this);
        btnPopulateSamples.addActionListener(this);
        btnSearch.addActionListener(this);
        
        txtBarcode.addKeyListener(new KeyListener() {
        	public void keyPressed(KeyEvent txt){
        		
     
        	}
        	public void keyTyped(KeyEvent txt){

        		
        	}        	
        	public void keyReleased(KeyEvent txt){
        		
        		if(txtBarcode.getText().length()==24)
        		{

        			playBarcodeBeep();
        			
        			
   
        			// A barcode was probably just scanned	
        			String barcodeText = txtBarcode.getText();
    				try {
    					LabBarcode.DecodedBarcode barcode = LabBarcode.decode(barcodeText);
    					txtBarcode.setText("");
    					doBarcodeSearch(barcode);
    					
    				} catch (IllegalArgumentException iae) {
    					Alert.message("Barcode error", "There was a problem with the barcode you scanned:\n"+iae.getMessage());
    				}	
        		}
	       		else {
	      	    	// User is typing a lab code
         			int key = txt.getKeyCode();
           		    if (key == KeyEvent.VK_ENTER)
           		    {
           		    	 // User typed lab code followed by enter so search for it!
           		    
           		    	String text = txtBarcode.getText();
           			  String [] strarray = null;
           			  String objcode = null;
           			  String elemcode = null;
           			  String sampcode = null;
           			  
           			  // Remove the "C-" from beginning if present
           			  if (text.substring(0, 2).compareToIgnoreCase("C-")==0) text = text.substring(2, text.length());
           			  
           			  // Explode based on '-' delimiter
           			  strarray = text.split("-");
           			  
           			  // Get codes from array
           			  if (strarray.length==0) return;
           			  if (strarray.length>=1) objcode = strarray[0];
           			  if (strarray.length>=2) elemcode = strarray[1];
           			  if (strarray.length>=3) sampcode = strarray[2];
           		      doLabCodeSearch(objcode, elemcode, sampcode);
           		    }
	           		     
	           		// Enter has not been pressed so user is 
           		    // still typing lab code    
	           		return;
	      			 
	      		 }
 
        	}

        });
         
        // Set models 
        cboObject.setModel(objModel);	
        cboElement.setModel(elModel);
        cboSample.setModel(sampModel);
        tblResults.setModel(resultModel);
        
        
        // Set renderers
        cboElement.setRenderer(new TridasListCellRenderer());
        cboSample.setRenderer(new TridasListCellRenderer());
        
        
        // Temporary bodge so only populate combo (not free text) search works
        cboElement.setEditable(false);
        cboSample.setEditable(false);
            
        // Rmove the checklist column
        tblResults.removeColumn(tblResults.getColumn("Temporary Checklist"));
        
    }
    
    /**
     * Play a beep for when barcode reader has scanned a barcode
     */
    public void playBarcodeBeep(){
		AudioClip beep;
		try {	
			// play this to indicate measuring is on...
			beep = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/checkout.wav"));
			if(beep != null)
				beep.play();
		} catch (Exception ae) { 
			System.out.println("Failed to play sound");
			System.out.println(ae.getMessage());
			}
		
    }
    
    
    /**
     * Enable or disable manual sample searching
     * @param e 
     */
    private void setManualSearch(Boolean e)
    {
		// Enable/Disable all manual items
   		cboObject.setEnabled(e);
   		cboElement.setEnabled(e);
   		cboSample.setEnabled(e);
   		btnSearch.setEnabled(e);
   		lblObject.setEnabled(e);
   		lblElement.setEnabled(e);
   		lblSample.setEnabled(e);
    	btnPopulateElements.setEnabled(e);
    	btnPopulateSamples.setEnabled(e);
    	btnSearch.setEnabled(e);
   		
    	if(e)
    	{
    		// Manual items enabled
    		
    		// Populate object combo
    		populateObjectCombo();
    		
	   		// If no object is selected disable remaining manual items
	   		if(cboObject.getSelectedIndex()==0) 
	   		{
	   			cboElement.setEnabled(false);
	   			cboSample.setEnabled(false);
	   			btnSearch.setEnabled(false);
	   		}
	   		
	   		// Disable barcode 
	   		txtBarcode.setEnabled(false);
    	}
    	else
    	{
    		// Manual items disabled so enable barcode
    		txtBarcode.setEnabled(true);
    		txtBarcode.requestFocus();
    	}
 	
    }
    
    /**
     * Populate the combo box with available objects
     */
    private void populateObjectCombo()
    {
    	cboObject.setRenderer(new SiteRenderer());
    	objModel.replaceContents(App.tridasObjects.getObjectList());
    	objModel.setSelectedItem(null);
    	
    }

    /**
     * Populate the combo box with available elements
     */
    private void populateElementCombo()
    {
    	TridasObject obj = null;
    	
    	if (cboObject.getSelectedItem()!=null)
    	{
    		obj = (TridasObject) cboObject.getSelectedItem();
    	}
    	else
    	{
    		return;
    	}
    	    	    	
		// Find all elements for an object 
    	SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
    	param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, obj.getIdentifier().getValue().toString());

    	// we want an object return here, so we get a list of object->elements->samples when we use comprehensive
		EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
		
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting elements");
			return;
		}
		
		List<TridasElement> elList = resource.getAssociatedResult();
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(elList, numSorter);
		
		elModel.replaceContents(elList); 	
    	
		// Pick first in list
		if(elModel.size()>0) cboElement.setSelectedIndex(0);
    	
    }
    
    /**
     * Populate combo box with available samples
     */
    private void populateSampleCombo()
    {
    	TridasElement el = null;
    	el = (TridasElement) cboElement.getSelectedItem();
  	    	
		// Find all samples for an element 
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, el.getIdentifier().getValue().toString());

    	// we want a sample return here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
		
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting samples");
			return;
		}
		
		List<TridasSample> sampList = resource.getAssociatedResult();
		
		/*TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		sampModel.replaceContents(sampList); 	
    	
		// Pick first in list
		if(sampModel.size()>0) cboSample.setSelectedIndex(0);
    	
    	
    }
    
    /**
     * Search for samples using a barcode
     * 
     * @param barcode
     */
	private void doBarcodeSearch(LabBarcode.DecodedBarcode barcode) {

		// Set return type to samples
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
		
    	// Set search parameters
		if (barcode.uuidType == LabBarcode.Type.SAMPLE){	
	    	param.addSearchConstraint(SearchParameterName.SAMPLEDBID, SearchOperator.EQUALS, barcode.uuid.toString());
		}
		else if (barcode.uuidType == LabBarcode.Type.BOX){
	    	param.addSearchConstraint(SearchParameterName.SAMPLEBOXID, SearchOperator.EQUALS, barcode.uuid.toString());	
		}
		else if (barcode.uuidType == LabBarcode.Type.SERIES){
	    	param.addSearchConstraint(SearchParameterName.SERIESDBID, SearchOperator.EQUALS, barcode.uuid.toString());	
		}
		
    	// we want a sample returned here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.SUMMARY);
		
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting samples");
			return;
		}
		
		List<TridasSample> sampList = resource.getAssociatedResult();
		
		// Check to see if any samples were found
		if (sampList.size()==0) 
		{
			Alert.error("None found", "No samples were found");
			return;
		}
						
		resultModel.setSamples(sampList); 
		resultModel.fireTableDataChanged();		
		
	}
    
	/**
	 * Search for samples using portions of a lab code
	 * 
	 * @param objectcode
	 * @param elementcode
	 * @param samplecode
	 */
	private void doLabCodeSearch(String objectcode, String elementcode, String samplecode)
	{
    	
		// Set return type to samples
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	
    	// Set parameters
    	if(objectcode!=null) param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTCODE, SearchOperator.EQUALS, objectcode);
    	if(elementcode!=null) param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, elementcode);
    	if(samplecode!=null) param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, samplecode);

    	// we want a sample returned here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.SUMMARY);
		
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting samples");
			return;
		}
		
		List<TridasSample> sampList = resource.getAssociatedResult();
		
		// Check to see if any samples were found
		if (sampList.size()==0) 
		{
			Alert.error("None found", "No samples were found");
			return;
		}
		
	/*	TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		
		resultModel.setSamples(sampList); 
		resultModel.fireTableDataChanged();
    	
		
		
	}
	
	/**
	 * Search for samples using the selected records in the combo boxes
	 */
    private void doSearchFromCombos()
    {
    	String objcode = null;
    	String elcode = null;
    	String sampcode = null;
    	
    	TridasObject obj = null;
    	TridasElement el = null;
    	TridasSample samp = null;

    	// Get Object 
    	if(cboObject.getSelectedItem()!=null)
		{
			obj = (TridasObject) cboObject.getSelectedItem();
			objcode = obj.getTitle().toString();
		}
		else
		{
			return;
		}

    	
    	
    	// Get Element code
    	if (cboElement.isEditable())
    	{
    		if(cboElement.getSelectedItem()!=null) elcode = cboElement.getSelectedItem().toString();
    	}	
    	else
    	{
    		if(cboElement.getSelectedItem()!=null)
    		{
    			el = (TridasElement) cboElement.getSelectedItem();
    			elcode = el.getTitle().toString();
    		}

    	}
    	   
    	// Get Sample code
    	if (cboSample.isEditable())
    	{
    		if(cboSample.getSelectedItem()!=null) sampcode = cboSample.getSelectedItem().toString();
    	}	
    	else
    	{
    		if(cboSample.getSelectedItem()!=null)
    		{
    			samp = (TridasSample) cboSample.getSelectedItem();
    			sampcode = samp.getTitle().toString();
    		}

    	}
    	
		// Set return type to samples
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	
    	// Set parameters
    	if(obj!=null) param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, objcode);
    	if(el!=null) param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, elcode);
    	if(samp!=null) param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, sampcode);

    	// we want a sample returned here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.SUMMARY);
		
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting samples");
			return;
		}
		
		List<TridasSample> sampList = resource.getAssociatedResult();
		
		// Check to see if any samples were found
		if (sampList.size()==0) 
		{
			Alert.error("None found", "No samples were found");
			return;
		}
		
	/*	TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		
		resultModel.setSamples(sampList); 
		resultModel.fireTableDataChanged();
    	
    	
    }
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radBarcode = new javax.swing.JRadioButton();
        radManual = new javax.swing.JRadioButton();
        txtBarcode = new javax.swing.JTextField();
        panelManualSearch = new javax.swing.JPanel();
        cboElement = new javax.swing.JComboBox();
        cboObject = new javax.swing.JComboBox();
        cboSample = new javax.swing.JComboBox();
        btnPopulateElements = new javax.swing.JButton();
        btnPopulateSamples = new javax.swing.JButton();
        lblObject = new javax.swing.JLabel();
        lblElement = new javax.swing.JLabel();
        lblSample = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        panelResults = new javax.swing.JPanel();
        scrollResults = new javax.swing.JScrollPane();
        tblResults = new javax.swing.JTable();
        panelButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        sep = new javax.swing.JSeparator();

        setTitle("Find a sample...");

        radBarcode.setSelected(true);
        radBarcode.setText("Lookup sample by barcode or labcode:");

        radManual.setText("Manually choose sample(s) to locate:");

        txtBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarcodeActionPerformed(evt);
            }
        });

        panelManualSearch.setEnabled(false);

        cboElement.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "[--all--]" }));

        cboObject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "[--select--]" }));

        cboSample.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "[--all--]" }));

        btnPopulateElements.setText("Populate");

        btnPopulateSamples.setText("Populate");

        lblObject.setText("Object:");

        lblElement.setText("Element:");

        lblSample.setText("Sample:");

        btnSearch.setText("Search");

        org.jdesktop.layout.GroupLayout panelManualSearchLayout = new org.jdesktop.layout.GroupLayout(panelManualSearch);
        panelManualSearch.setLayout(panelManualSearchLayout);
        panelManualSearchLayout.setHorizontalGroup(
            panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelManualSearchLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblObject, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .add(lblElement, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblSample, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(panelManualSearchLayout.createSequentialGroup()
                        .add(panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(cboSample, 0, 270, Short.MAX_VALUE)
                            .add(cboElement, 0, 270, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(btnPopulateElements, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btnPopulateSamples, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, btnSearch, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(5, 5, 5))
                    .add(panelManualSearchLayout.createSequentialGroup()
                        .add(cboObject, 0, 373, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panelManualSearchLayout.setVerticalGroup(
            panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelManualSearchLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblObject)
                    .add(cboObject, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnPopulateElements)
                    .add(lblElement)
                    .add(cboElement, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelManualSearchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnPopulateSamples)
                    .add(lblSample)
                    .add(cboSample, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnSearch)
                .addContainerGap())
        );

        tblResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Sample", "Box", "Location"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollResults.setViewportView(tblResults);

        org.jdesktop.layout.GroupLayout panelResultsLayout = new org.jdesktop.layout.GroupLayout(panelResults);
        panelResults.setLayout(panelResultsLayout);
        panelResultsLayout.setHorizontalGroup(
            panelResultsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelResultsLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelResultsLayout.setVerticalGroup(
            panelResultsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelResultsLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnOk.setText("Ok");

        sep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sep.setMaximumSize(new java.awt.Dimension(9999, 2));
        sep.setMinimumSize(new java.awt.Dimension(30, 2));
        sep.setPreferredSize(new java.awt.Dimension(50, 2));

        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sep, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnOk))
                .addContainerGap())
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(sep, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnOk)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(35, 35, 35)
                .add(panelManualSearch, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(panelResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(radBarcode)
                            .add(radManual))
                        .add(100, 100, 100))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(29, 29, 29)
                        .add(txtBarcode, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                        .addContainerGap())))
            .add(layout.createSequentialGroup()
                .add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(radBarcode)
                .add(3, 3, 3)
                .add(txtBarcode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(radManual)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelManualSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarcodeActionPerformed
        // TODO add your handling code here:
    	
    	
    	
    }//GEN-LAST:event_txtBarcodeActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnOk;
    protected javax.swing.JButton btnPopulateElements;
    protected javax.swing.JButton btnPopulateSamples;
    protected javax.swing.JButton btnSearch;
    protected javax.swing.JComboBox cboElement;
    protected javax.swing.JComboBox cboObject;
    protected javax.swing.JComboBox cboSample;
    protected javax.swing.JLabel lblElement;
    protected javax.swing.JLabel lblObject;
    protected javax.swing.JLabel lblSample;
    protected javax.swing.JPanel panelButtons;
    protected javax.swing.JPanel panelManualSearch;
    protected javax.swing.JPanel panelResults;
    protected javax.swing.JRadioButton radBarcode;
    protected javax.swing.JRadioButton radManual;
    protected javax.swing.JScrollPane scrollResults;
    protected javax.swing.JSeparator sep;
    protected javax.swing.JTable tblResults;
    protected javax.swing.JTextField txtBarcode;
    // End of variables declaration//GEN-END:variables

    
    /**
     * Perform actions
     */
	public void actionPerformed(ActionEvent e) {

		Object btn = e.getSource();
		
		if(btn==radManual)
		{
			setManualSearch(true);			
		}
		else if (btn==radBarcode)
		{
			setManualSearch(false);
		}
		else if (btn==btnOk)
		{
			this.dispose();
		}
		else if (btn==btnPopulateElements)
		{
			/*if(btnPopulateElements.isSelected())
			{
			*/
				cboElement.setEditable(false);
				populateElementCombo();
			/*}
			else
			{
				elModel.clear();
				cboElement.setEditable(true);
			}*/
		}
		else if (btn==btnPopulateSamples)
		{
			/*if(btnPopulateSamples.isSelected())
			{*/
				cboSample.setEditable(false);
				populateSampleCombo();
			/*}
			else
			{
				//elModel.clear();
				//cboSample.setEditable(true);
			}*/			
			
		}
		else if (btn==btnSearch)
		{
			doSearchFromCombos();
		}
		
	}
    
}
