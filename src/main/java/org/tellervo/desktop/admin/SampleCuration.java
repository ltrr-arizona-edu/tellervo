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
package org.tellervo.desktop.admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.dbbrowse.TridasObjectRenderer;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;


/**
 * GUI class to allow users to find the physical location of the sample they are
 * interested in.   
 *
 * @author  peterbrewer
 */
public class SampleCuration extends javax.swing.JDialog implements ActionListener{
    

	private static final long serialVersionUID = 7814293298626840188L;
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

        			SoundUtil.playBarcodeBeep();
        			
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
           			  String prefix = App.getLabCodePrefix();
           			  if (text.substring(0, prefix.length()).compareToIgnoreCase(prefix)==0) text = text.substring(prefix.length(), text.length());
           			  
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
    	cboObject.setRenderer(new TridasObjectRenderer());
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
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
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
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
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
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
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
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
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
			
			if(obj.isSetGenericFields())
			{
				for(TridasGenericField gf : obj.getGenericFields())
				{
					if(gf.getName().equals("tellervo.objectLabCode"))
					{
						objcode = gf.getValue();
					}
				}
			}
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
    	if(obj!=null) param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTCODE, SearchOperator.EQUALS, objcode);
    	if(el!=null) param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, elcode);
    	if(samp!=null) param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, sampcode);

    	// we want a sample returned here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
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
    @SuppressWarnings("serial")
	private void initComponents() {

        setTitle("Find a sample...");
        
        panel = new JPanel();
        panel.setLayout(new MigLayout("", "[grow]", "[][][][][grow,fill][]"));
        
                radBarcode = new javax.swing.JRadioButton();
                panel.add(radBarcode, "cell 0 0");
                
                        radBarcode.setSelected(true);
                        radBarcode.setText("Lookup sample by barcode or labcode:");
                        txtBarcode = new javax.swing.JTextField();
                        panel.add(txtBarcode, "cell 0 1,growx");
                        radManual = new javax.swing.JRadioButton();
                        panel.add(radManual, "cell 0 2");
                        
                                radManual.setText("Manually choose sample(s) to locate:");
                                panelManualSearch = new javax.swing.JPanel();
                                panel.add(panelManualSearch, "cell 0 3,growx");
                                cboElement = new javax.swing.JComboBox();
                                cboObject = new javax.swing.JComboBox();
                                cboSample = new javax.swing.JComboBox();
                                btnPopulateElements = new javax.swing.JButton();
                                btnPopulateSamples = new javax.swing.JButton();
                                lblObject = new javax.swing.JLabel();
                                lblElement = new javax.swing.JLabel();
                                lblSample = new javax.swing.JLabel();
                                btnSearch = new javax.swing.JButton();
                                
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
                                                                                                                panelManualSearch.setLayout(new MigLayout("", "[69px][208px,grow][6px][98px]", "[24px][25px][25px][25px]"));
                                                                                                                panelManualSearch.add(lblObject, "cell 0 0,growx,aligny center");
                                                                                                                panelManualSearch.add(lblElement, "cell 0 1,growx,aligny center");
                                                                                                                panelManualSearch.add(lblSample, "cell 0 2,growx,aligny center");
                                                                                                                panelManualSearch.add(cboSample, "cell 1 2,growx,aligny top");
                                                                                                                panelManualSearch.add(cboElement, "cell 1 1,growx,aligny top");
                                                                                                                panelManualSearch.add(btnPopulateElements, "cell 3 1,alignx left,aligny top");
                                                                                                                panelManualSearch.add(btnPopulateSamples, "cell 3 2,alignx left,aligny top");
                                                                                                                panelManualSearch.add(btnSearch, "cell 3 3,growx,aligny top");
                                                                                                                panelManualSearch.add(cboObject, "cell 1 0 3 1,growx,aligny top");
                                                                                                                panelResults = new javax.swing.JPanel();
                                                                                                                panel.add(panelResults, "cell 0 4,growx");
                                                                                                                scrollResults = new javax.swing.JScrollPane();
                                                                                                                tblResults = new javax.swing.JTable();
                                                                                                                
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
                                                                                                                        panelResults.setLayout(new MigLayout("", "[314.00px,grow]", "[150px,grow]"));
                                                                                                                        panelResults.add(scrollResults, "cell 0 0,grow");
                                                                                                                        panelButtons = new javax.swing.JPanel();
                                                                                                                        panel.add(panelButtons, "cell 0 5,growx");
                                                                                                                        btnOk = new javax.swing.JButton();
                                                                                                                        sep = new javax.swing.JSeparator();
                                                                                                                        
                                                                                                                                btnOk.setText("Ok");
                                                                                                                                
                                                                                                                                        sep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                                                                                                                                        sep.setMaximumSize(new java.awt.Dimension(9999, 2));
                                                                                                                                        sep.setMinimumSize(new java.awt.Dimension(30, 2));
                                                                                                                                        sep.setPreferredSize(new java.awt.Dimension(50, 2));
                                                                                                                                        panelButtons.setLayout(new MigLayout("", "[410px,grow]", "[2px][25px]"));
                                                                                                                                        panelButtons.add(sep, "cell 0 0,growx,aligny top");
                                                                                                                                        panelButtons.add(btnOk, "cell 0 1,alignx right,aligny top");
                        
                                txtBarcode.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        txtBarcodeActionPerformed(evt);
                                    }
                                });
        getContentPane().setLayout(new BorderLayout(0, 0));
        getContentPane().add(panel);
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
    private JPanel panel;
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
