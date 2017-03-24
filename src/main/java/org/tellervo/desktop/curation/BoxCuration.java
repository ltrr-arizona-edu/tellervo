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
package org.tellervo.desktop.curation;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.xml.datatype.XMLGregorianCalendar;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.admin.SampleListTableModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.widgets.AutoCompleteComboDocument;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.gui.widgets.TridasMultiEntityPickerDialog;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.MVCArrayList;

import javax.swing.JButton;


/**
 *
 * @author  peterbrewer
 */
public class BoxCuration extends javax.swing.JDialog 
implements KeyListener, ActionListener, TableModelListener{

	private final static Logger log = LoggerFactory.getLogger(BoxCuration.class);

	private static final long serialVersionUID = 1L;
	private boolean isNewRecord = false;
	private WSIBox box = null;
	private BoxCurationType type = BoxCurationType.BROWSE;
	//private ArrayListModel<WSIBox> boxModel;
	private MVCArrayList<WSIBox> boxList;
	protected SampleListTableModel sampleTableModel = new SampleListTableModel();

	public enum BoxCurationType{
		BROWSE,
		CHECKOUT,
		CHECKIN;
	}

	/**
	 * Standard constructor creates a GUI ready for user to pick a box
	 * 
	 * @param parent
	 * @param modal
	 * @wbp.parser.constructor
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
	 * Set the purpose of this GUI
	 * 
	 * @param type
	 */
	public void setCurationType(BoxCurationType type)
	{
		this.type = type;

		if ((type==BoxCurationType.CHECKIN) || (type==BoxCurationType.CHECKOUT))
		{
			this.tabbedPaneBox.setVisible(false);
			this.btnApply.setVisible(false);
			this.btnCreateNewBox.setVisible(false);
			this.btnOk.setText("Cancel");
			this.pack();

		}

		if(type==BoxCurationType.CHECKIN)
		{
			this.setTitle(I18n.getText("menus.admin.checkinbox"));
		}

		if(type==BoxCurationType.CHECKOUT)
		{
			this.setTitle(I18n.getText("menus.admin.checkoutbox"));
		}
	}

	/**
	 * Update the sample count label
	 */
	private void updateSampleCount()
	{
		btnMarkMissing.setEnabled(false);

		if(box.getSamples().size()==1)
		{
			lblSampleCount.setText(box.getSamples().size()+" sample in box");
		}
		else
		{
			lblSampleCount.setText(box.getSamples().size()+" samples in box");
		}

		if(this.sampleTableModel.getCheckedCount()!=null)
		{
			if(sampleTableModel.getCheckedCount()>0)
			{
				btnMarkMissing.setEnabled(true);
			}
			lblSampleCount.setText(lblSampleCount.getText()+" ("+sampleTableModel.getCheckedCount()+" checked)");		
		}
	}

	/**
	 * Remove the specified sample from this box
	 * 
	 * @param s
	 * @return
	 */
	private boolean removeSampleFromBox(TridasSample s)
	{
		
		GenericFieldUtils.setField(s, "tellervo.boxID", "null");
		
		// Create resource
		EntityResource<TridasSample> resource = new EntityResource<TridasSample>(s, TellervoRequestType.UPDATE, TridasSample.class);

		// set up a dialog...
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(this, resource);

		resource.query();
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			Alert.error("Error", dialog.getFailException().getMessage());
			return false;
		}

		return true;
		
	}
	
	/**
	 * Save changes to the box info for a sample 
	 * 
	 * @param s
	 * @return
	 */
	private boolean saveBoxDetailsForSample(TridasSample s)
	{

		Boolean valueSet = false;
		// Update boxid in sample genericField
		if(!s.isSetGenericFields())
		{
			s.setGenericFields(new ArrayList<TridasGenericField>());
		}

		for(TridasGenericField gf: s.getGenericFields())
		{
			if(gf.getName().equals("tellervo.boxID"))
			{
				if(gf.getValue().equals(box.getIdentifier().getValue()))
				{
					// This sample already belongs in this box
					return true;
				}

				LabCode labcode = new LabCode();
				String str = s.getTitle();
	        	if(GenericFieldUtils.findField(s, "tellervo.objectLabCode")!=null)
	        	{
	        		labcode.appendSiteCode(GenericFieldUtils.findField(s, "tellervo.objectLabCode").getValue());
	            	if(GenericFieldUtils.findField(s, "tellervo.elementLabCode")!=null)
	            	{
	            		labcode.setElementCode(GenericFieldUtils.findField(s, "tellervo.elementLabCode").getValue());
	                	labcode.setSampleCode(s.getTitle());
	                	str = LabCodeFormatter.getRadiusPrefixFormatter().format(labcode);
	                	
	            	}
	        	}
	        	
				int response =  JOptionPane.showConfirmDialog(this,
						"The sample '"+str+"' is already assigned to a box.\n"
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
				valueSet = true;
			}
		}


		if(valueSet==false)
		{
			TridasGenericField gf= new TridasGenericField();
			gf.setName("tellervo.boxID");
			gf.setType("xs:string");
			gf.setValue(box.getIdentifier().getValue());				
			s.getGenericFields().add(gf);
		}


		// Create resource
		EntityResource<TridasSample> resource = new EntityResource<TridasSample>(s, TellervoRequestType.UPDATE, TridasSample.class);

		// set up a dialog...
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(this, resource);

		resource.query();
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			Alert.error("Error", dialog.getFailException().getMessage());
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
			resource = new EntityResource<WSIBox>(box, TellervoRequestType.CREATE, WSIBox.class);
		}
		else
		{
			resource = new EntityResource<WSIBox>(box, TellervoRequestType.UPDATE, WSIBox.class);
		}

		// set up a dialog...
		Window parentWindow = SwingUtilities.getWindowAncestor(getParent());
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parentWindow, resource);

		resource.query();
		dialog.setVisible(true);

		if(!dialog.isSuccessful()) 
		{ 
			Alert.error("Error", dialog.getFailException().getMessage());
			return false;
		}

		box = resource.getAssociatedResult();

		if(isNewRecord)
		{
			boxList.add(box);
		}
		else
		{
			for (WSIBox bx: boxList)
			{
				if(box.getIdentifier().equals(bx.getIdentifier()))
				{
					bx=box;
				}
			}
		}


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

		this.btnApply.setEnabled(false);
		this.setTitle("Box details");
		setIconImage(Builder.getApplicationIcon());

		// Set up box list model etc
		boxList = (MVCArrayList<WSIBox>) Dictionary.getMutableDictionary("boxDictionary");
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(boxList, numSorter);
	
		WSIBox[] arr = new WSIBox[boxList.size()];
		
		for (int i=0; i<boxList.size(); i++)
		{
			arr[i] = boxList.get(i);
		}
				
		
		
		cboBox =  new ComboBoxFilterable(arr);
		cboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try{
				WSIBox bx = (WSIBox) cboBox.getSelectedItem();
				getComprehensiveWSIBox(bx.getIdentifier().getValue());
				updateBoxGui();
				} catch (Exception e)
				{
					log.debug("Failed to get box to update gui");
				}
				
			}
			
		});
		getContentPane().add(cboBox, "cell 1 1,growx,aligny top");

		//boxModel = new ArrayListModel<WSIBox>(boxList);
		TridasListCellRenderer tlcr = new TridasListCellRenderer();
		//cboBox.setModel(boxModel);
		cboBox.setRenderer(tlcr);
		
		
		

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
		//this.cboBox.addActionListener(this);
		this.btnMarkMissing.addActionListener(this);

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

	/**
	 * Get the comprehensive details for a box from the webservice given
	 * the box id
	 * 
	 * @param idstr
	 */
	private void getComprehensiveWSIBox(String idstr)
	{

		log.debug("Getting details of box (id"+idstr+") from the webservice");
		// Set return type to box
		SearchParameters param = new SearchParameters(SearchReturnObject.BOX);

		// Set box id
		param.addSearchConstraint(SearchParameterName.BOXID, SearchOperator.EQUALS, idstr);

		TridasIdentifier id = new TridasIdentifier();
		id.setValue(idstr);

		// we want a box returned here
		EntityResource<WSIBox> resource = new EntityResource<WSIBox>(id, EntityType.BOX, TellervoRequestType.READ, WSIBox.class);

		// Query db 
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);

		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting boxes");
			return;
		}

		box = resource.getAssociatedResult();

		updateBoxGui();

		// If Checking in or out then set fields and close
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


		this.setModal(true);
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



	}

	/**
	 * Update the box fields automatically for checking out
	 */
	private void setFieldsForCheckout()
	{
		this.txtTrackingLocation.setText("Checked out");
		addComment("Checked out by "+App.currentUser.getFirstName()+" "+App.currentUser.getLastName()+ " on " + getTodaysDate() +".");
	}

	/**
	 * Update the box fields automatically for checking in
	 */
	private void setFieldsForCheckin()
	{
		this.txtTrackingLocation.setText(" ");
		addComment("Checked in by "+App.currentUser.getFirstName()+" "+App.currentUser.getLastName()+ " on " + getTodaysDate() +".");    
	}

	/**
	 * Get todays date as a formatted string
	 * 
	 * @return
	 */
	private String getTodaysDate()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy");
		return dateFormat.format(c.getTime());
	}

	/**
	 * Add a comment to the box comment field
	 * 
	 * @param comment
	 */
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
	 * Update the sample table GUI
	 */
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


		tblBoxContents.setModel(sampleTableModel);
		tblBoxContents.getModel().addTableModelListener(this);

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
		txtName.setText("");
		txtLastUpdated.setText("");
		txtCurationLocation.setText("");
		txtTrackingLocation.setText("");
		txtComments.setText("");

		if(box==null)
		{
			// Box is null so hide and disable components
			setComponentsEnabled(false);
			btnAddSampleToBox.setEnabled(false);
			return;
		}

		// Box is populated so show and enable components
		this.btnAddSampleToBox.setEnabled(true);
		setComponentsEnabled(true);

		// Set components values
		if(box.isSetTitle()) 				 txtName.setText(box.getTitle());
		if(box.isSetLastModifiedTimestamp()) txtLastUpdated.setText(BoxCuration.formatXMLGregorianCalendar(box.getLastModifiedTimestamp().getValue()));
		if(box.isSetCurationLocation()) 	 txtCurationLocation.setText(box.getCurationLocation());
		if(box.isSetTrackingLocation()) 	 txtTrackingLocation.setText(box.getTrackingLocation());
		if(box.isSetComments()) 			 txtComments.setText(box.getComments());



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
	@SuppressWarnings("serial")
	private void initComponents() {

		//cboBox = new ComboBoxFilterable();
		lblScanOrSelect = new javax.swing.JLabel();
		btnOk = new javax.swing.JButton();
		btnApply = new javax.swing.JButton();
		txtBarcode = new javax.swing.JTextField();
		btnCreateNewBox = new javax.swing.JButton();
		lblSelectBox = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		//cboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "abbb", "cbbbb", "ghhh", "jjjjj" }));

		lblScanOrSelect.setText("Scan barcode:");

		btnOk.setText("OK");

		btnApply.setText("Save changes");

		btnCreateNewBox.setText("Create New Box");

		lblSelectBox.setText("...or select box:");
		getContentPane().setLayout(new MigLayout("hidemode 3", "[127.00px][242px,grow][86px][54px]", "[][][347px,grow][25px]"));

		panel = new JPanel();
		panel.setBorder(null);
		getContentPane().add(panel, "cell 0 2 4 1,grow");
		panel.setLayout(new MigLayout("", "[127.00px][242px,grow][86px][54px]", "[347px,grow]"));
		tabbedPaneBox = new javax.swing.JTabbedPane();
		panel.add(tabbedPaneBox, "cell 0 0 4 1,grow");
		panelBoxDetails = new javax.swing.JPanel();
		panelBoxDetails.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtLastUpdated = new javax.swing.JTextField();
		lblLastUpdated = new javax.swing.JLabel();
		txtCurationLocation = new javax.swing.JTextField();
		txtTrackingLocation = new javax.swing.JTextField();
		lblCurationLocation = new javax.swing.JLabel();
		lblTrackingLocation = new javax.swing.JLabel();
		lblComments = new javax.swing.JLabel();
		txtName = new javax.swing.JTextField();
		lblName = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		txtComments = new javax.swing.JTextArea();
		panelContents = new javax.swing.JPanel();
		panelContents.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		jScrollPane2 = new javax.swing.JScrollPane();
		tblBoxContents = new JXTable();
		lblSampleCount = new javax.swing.JLabel();

		txtLastUpdated.setEditable(false);
		txtLastUpdated.setFocusable(false);

		lblLastUpdated.setText("Last updated:");

		lblCurationLocation.setText("Permanent location:");

		lblTrackingLocation.setText("Current location:");

		lblComments.setText("Comments:");

		lblName.setText("Name:");

		txtComments.setColumns(20);
		txtComments.setLineWrap(true);
		txtComments.setRows(5);
		jScrollPane1.setViewportView(txtComments);

		tabbedPaneBox.addTab("Details", panelBoxDetails);
		panelBoxDetails.setLayout(new MigLayout("", "[144px][12px][350px,grow]", "[19px][19px][19px][19px][15px][163px,grow]"));
		panelBoxDetails.add(jScrollPane1, "cell 0 5 3 1,grow");
		panelBoxDetails.add(lblCurationLocation, "cell 0 2,alignx left,aligny center");
		panelBoxDetails.add(lblLastUpdated, "cell 0 1,alignx left,aligny center");
		panelBoxDetails.add(lblTrackingLocation, "cell 0 3,alignx left,aligny center");
		panelBoxDetails.add(lblName, "cell 0 0,alignx left,aligny center");
		panelBoxDetails.add(txtName, "cell 2 0,growx,aligny top");
		panelBoxDetails.add(txtTrackingLocation, "cell 2 3,growx,aligny top");
		panelBoxDetails.add(txtLastUpdated, "cell 2 1,growx,aligny top");
		panelBoxDetails.add(txtCurationLocation, "cell 2 2,growx,aligny top");
		panelBoxDetails.add(lblComments, "cell 0 4,alignx left,aligny top");

		tblBoxContents.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {

				},
				new String [] {
						"Sample", "Check Presence"
				}
				) {
			@SuppressWarnings("rawtypes")
			Class[] types = new Class [] {
				java.lang.Object.class, java.lang.Boolean.class
			};
			boolean[] canEdit = new boolean [] {
					false, true
			};

			@SuppressWarnings({
				"unchecked",
				"rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		jScrollPane2.setViewportView(tblBoxContents);
		tblBoxContents.setAutoCreateRowSorter(true);
		lblSampleCount.setText("0 samples in box");

		tabbedPaneBox.addTab("Contents", panelContents);
		panelContents.setLayout(new MigLayout("", "[164px,grow,fill][]", "[256px,grow][][15px]"));
		panelContents.add(jScrollPane2, "cell 0 0,grow");
		
		panel_1 = new JPanel();
		panel_1.setBorder(null);
		panelContents.add(panel_1, "cell 1 0,grow");
		panel_1.setLayout(new MigLayout("", "[]", "[][]"));
		btnAddSampleToBox = new javax.swing.JButton();
		panel_1.add(btnAddSampleToBox, "cell 0 0");
		btnAddSampleToBox.setToolTipText("Add sample(s) to box");
		btnAddSampleToBox.setIcon(Builder.getIcon("edit_add.png", 16));
		
		btnRemove = new JButton("");
		btnRemove.setIcon(Builder.getIcon("edit_remove.png", 16));
		btnRemove.setActionCommand("RemoveSample");
		btnRemove.addActionListener(this);
		panel_1.add(btnRemove, "cell 0 1");
		btnMarkMissing = new javax.swing.JButton();
		
				btnMarkMissing.setText("Mark unchecked as missing from box");
				btnMarkMissing.setEnabled(false);
				panelContents.add(btnMarkMissing, "cell 0 1,alignx left,aligny top");
		panelContents.add(lblSampleCount, "cell 0 2,alignx left,aligny top");
		getContentPane().add(btnApply, "cell 1 3 2 1,alignx right,aligny top");
		getContentPane().add(btnOk, "cell 3 3,alignx left,aligny top");
		getContentPane().add(lblSelectBox, "cell 0 1,alignx left,aligny center");
		getContentPane().add(lblScanOrSelect, "cell 0 0,alignx left,aligny center");
		getContentPane().add(txtBarcode, "cell 1 0 3 1,growx,aligny top");
		//getContentPane().add(cboBox, "cell 1 1,growx,aligny top");
		getContentPane().add(btnCreateNewBox, "cell 2 1 2 1,alignx right,aligny top");



		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Show the actual dialog empty ready for user to pick a box
	 */
	public static void showDialog() {
		showDialog(false);
	}
	
	/**
	 * Show the actual dialog empty ready for user to pick a box
	 */
	public static void showDialog(boolean createNewBox) {
		BoxCuration dialog = new BoxCuration(new javax.swing.JFrame(), false);
		dialog.setLocationRelativeTo(null);		
		
		
		if(createNewBox)
		{
			dialog.createNewBox();
			dialog.setSearchVisible(false);
			dialog.setTitle("Create new box");
		}
		
		dialog.setVisible(true);
	}

	private void setSearchVisible(boolean b) {

		this.txtBarcode.setVisible(b);
		this.btnCreateNewBox.setVisible(b);
		this.lblSelectBox.setVisible(b);
		this.lblScanOrSelect.setVisible(b);
		this.cboBox.setVisible(b);
		
		
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
	protected javax.swing.JButton btnMarkMissing;
	protected javax.swing.JButton btnOk;
	protected javax.swing.JComboBox cboBox;
	protected javax.swing.JScrollPane jScrollPane1;
	protected javax.swing.JScrollPane jScrollPane2;
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
	protected JXTable tblBoxContents;
	protected javax.swing.JTextField txtBarcode;
	protected javax.swing.JTextArea txtComments;
	protected javax.swing.JTextField txtCurationLocation;
	protected javax.swing.JTextField txtLastUpdated;
	protected javax.swing.JTextField txtName;
	protected javax.swing.JTextField txtTrackingLocation;
	private JPanel panel;
	private JPanel panel_1;
	private JButton btnRemove;
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

	private TridasSample getSampleByID(String id)
	{
		// Set return type to samples
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
		
    	// Set search parameters
    	param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, id);
		
    	// we want a sample returned here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting samples");
			return null;
		}
		
		List<TridasSample> sampList = resource.getAssociatedResult();
		
		// Check to see if any samples were found
		if (sampList.size()==0) 
		{
			Alert.error("None found", "No samples were found");
			return null;
		}
		else if (sampList.size()>1)
		{
			log.error("This should be impossible");
			return null;
		}
		
		return sampList.get(0);

	}
	

	public void createNewBox()
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
		
		this.txtName.requestFocus();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnCreateNewBox))
		{
			createNewBox();
		}
		else if (e.getActionCommand().equals("RemoveSample"))
		{
			
			int response = JOptionPane.showConfirmDialog(null,
				"This option should only be used to permanently remove samples from a box\n"
			  + "when they have been erroneously added.  Samples that are missing should\n"
			  + "be marked as such.\n\n"
						+"Are you sure you want to remove the selected sample(s)?",
						"Remove samples confirmation",
						JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.WARNING_MESSAGE);
			
			if(response != JOptionPane.YES_OPTION)
			{
				return;
			}
			
			ArrayList<TridasSample> selectedSamples = new ArrayList<TridasSample>();
			for (int i : this.tblBoxContents.getSelectedRows())
			{
				selectedSamples.add(sampleTableModel.getElementAt(i));
			}
			
			for(TridasSample s : selectedSamples)
			{
				removeSampleFromBox(s);
			}
						
			this.getComprehensiveWSIBox(box.getIdentifier().getValue());
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
		else if (e.getActionCommand().equals("BoxSelected"))
		{
			WSIBox bx = (WSIBox) cboBox.getSelectedItem();
			this.getComprehensiveWSIBox(bx.getIdentifier().getValue());
			updateBoxGui();
		}
		else if (e.getSource().equals(btnAddSampleToBox))
		{

			TridasMultiEntityPickerDialog dialog = new TridasMultiEntityPickerDialog(null, "Pick Sample", TridasSample.class);
			ArrayList<ITridas> entities = dialog.getEntities();

			// Add sample to our box and save
			if(!box.isSetSamples())
			{
				box.setSamples(new ArrayList<TridasSample>());
			}

			for(ITridas returned : entities)
			{
				log.debug("Adding sample "+returned.getTitle()+" to box");
				box.getSamples().add((TridasSample)returned);
				saveBoxDetailsForSample((TridasSample)returned);
			}

			this.getComprehensiveWSIBox(box.getIdentifier().getValue());
			updateBoxGui();
		}
		else if (e.getSource().equals(btnMarkMissing))
		{
			String comment = "";
			if(this.sampleTableModel.getUncheckedCount()==null) return;
			if(this.sampleTableModel.getUncheckedCount()<1) return;

			if(this.sampleTableModel.getUncheckedCount()>1)
			{
				comment += "The following samples were";
			}
			else
			{
				comment += "The following sample was"; 
			}
			comment+=" found to be missing on "+ getTodaysDate() +": ";

			for (String s : this.sampleTableModel.getUncheckedSampleNames())
			{
				comment+= s+"; ";
			}

			comment = comment.substring(0, comment.length()-2)+".";
			addComment(comment);
			sampleTableModel.clearChecks();
			this.saveChanges();
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
						Alert.error("Barcode error", "This was not a valid box barcode.");
						return;
					}

					doBarcodeSearch(barcode);

				} catch (IllegalArgumentException iae) {
					Alert.error("Barcode error", "There was a problem with the barcode you scanned:\n"+iae.getMessage());
				}	
			}	
		}
		else
		{
			haveDetailsChanged();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		updateSampleCount();
	}

}
