package org.tellervo.desktop.curation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collection;

import org.jdesktop.swingx.JXTable;
import org.slf4j.LoggerFactory;

import javax.swing.JDialog;

import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.labelgen.LabBarcode;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.labels.ui.SampleLabelPrintingUI;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SampleStatus;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import javax.swing.JToolBar;



public class SampleStatusDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;

  	private JXTable tblSamples;
    private static String barcodeInstructions = "To quickly add multiple samples, highlight the list above and scan sample barcodes.  You can also scan box barcodes to include all samples from a specific box.";
	private SampleListTableModel sampleModel;
	private JButton btnAddSample;
	private JButton btnRemoveSample;
	private String keyBuffer = "";
	private JScrollPane scrollPaneSamples;
	private final static Logger log = LoggerFactory.getLogger(LoanPanel.class);
	private JToolBar toolBar;
	private JButton btnAddUnpreppedSamples;
	private JButton btnAddPreppedSamples;
	private JButton btnAddUndateableSamples;
	private JButton btnUpdateStatus;
	private JButton btnPrintTable;

  public SampleStatusDialog() {
	  this.setIconImage(Builder.getApplicationIcon());
	  this.setTitle("Sample Status");
  	getContentPane().setLayout(new MigLayout("", "[grow][fill]", "[][][][grow,fill][][]"));
  	
  	toolBar = new JToolBar();
  	toolBar.setFloatable(false);
  	
  	btnAddUnpreppedSamples = new JButton("Add Unprepped");
  	btnAddUnpreppedSamples.setActionCommand("addUnprepped");
  	btnAddUnpreppedSamples.addActionListener(this); 	
  	
  	btnAddPreppedSamples = new JButton("Add Prepped");
  	btnAddPreppedSamples.setActionCommand("addPrepped");
  	btnAddPreppedSamples.addActionListener(this);
  	
  	btnAddUndateableSamples = new JButton("Add Undateable");
  	btnAddUndateableSamples.setActionCommand("addUndateable");
  	btnAddUndateableSamples.addActionListener(this);
  	  	
  	btnUpdateStatus = new JButton("Update Status");
  	btnUpdateStatus.setActionCommand("updateStatus");
  	btnUpdateStatus.addActionListener(this);
  	
  	btnPrintTable = new JButton("Print");
  	btnPrintTable.setActionCommand("printTable");
  	btnPrintTable.addActionListener(this);
  	
  	toolBar.add(btnAddUnpreppedSamples);
  	toolBar.add(btnAddPreppedSamples);
  	toolBar.add(btnAddUndateableSamples);
  	toolBar.add(btnUpdateStatus);
  	toolBar.add(btnPrintTable);

 
  	
  	getContentPane().add(toolBar, "cell 0 0");
  	
  	scrollPaneSamples = new JScrollPane();
  	getContentPane().add(scrollPaneSamples, "cell 0 1 1 3,grow");
  	
  	scrollPaneSamples.addMouseListener(new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent arg0) {
			tblSamples.requestFocusInWindow();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			tblSamples.requestFocusInWindow();
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			tblSamples.requestFocusInWindow();
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			tblSamples.requestFocusInWindow();
			
		}
		
	});
	
	tblSamples = new JXTable();
	tblSamples.setShowGrid(false);
	scrollPaneSamples.setViewportView(tblSamples);
	sampleModel = new SampleListTableModel();
	tblSamples.setModel(sampleModel);
	tblSamples.setAutoCreateRowSorter(true);
	tblSamples.addKeyListener(new KeyListener(){

		@Override
		public void keyPressed(KeyEvent arg0) {	}

		@Override
		public void keyReleased(KeyEvent arg0) { }

		@Override
		public void keyTyped(KeyEvent e) {
			if(!btnAddSample.isVisible()) return;
			
			
			if(keyBuffer.length()==24)
			{
				// A barcode was probably just scanned

				try{
					// Decode the barcode string				
					LabBarcode.DecodedBarcode barcode = LabBarcode.decode(keyBuffer);
					
					if(barcode.uuidType.equals(LabBarcode.Type.BOX))
					{
				    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
				    	param.addSearchConstraint(SearchParameterName.BOXID, SearchOperator.EQUALS, barcode.uuid.toString());
						addSamplesByScan(param);
					}
					else if (barcode.uuidType.equals(LabBarcode.Type.SAMPLE))
					{
				    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
				    	param.addSearchConstraint(SearchParameterName.SAMPLEDBID, SearchOperator.EQUALS, barcode.uuid.toString());
						addSamplesByScan(param);
					}
					else
					{
						Alert.error("Error", "Invalid barcode type");
						return;
					}
					keyBuffer = "";
					return;
					
				} catch (IllegalArgumentException e2)
				{
					Alert.error("Error", "Erro reading barcode.  Please try again.");
					keyBuffer = "";
				}

			}
			else {
		    	// Clear buffer on certain keys
			    if (e.getKeyCode() == KeyEvent.VK_ENTER
			    		|| e.getKeyCode() == KeyEvent.VK_DELETE 
			    		|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE
			    		|| e.getKeyCode() == KeyEvent.VK_ESCAPE)
			    {
			    	keyBuffer = "";
			    }
			    else
			    {
			    	// Add char to buffer
			    	keyBuffer = keyBuffer+e.getKeyChar();
			    }
			 }
			
			
		}
		
	});
	
	tblSamples.addFocusListener(new FocusListener(){

		@Override
		public void focusGained(FocusEvent arg0) {
			handleSampleTableBorder();
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			handleSampleTableBorder();
		}
		
	});
  	
  	
  	
  	
  	btnAddSample = new JButton();
	btnAddSample.setActionCommand("AddSample");
	btnAddSample.setToolTipText("Add sample to list");
	btnAddSample.setIcon(Builder.getIcon("edit_add.png", 16));
	btnAddSample.addActionListener(this);
  	getContentPane().add(btnAddSample, "cell 1 1");
  	
  	btnRemoveSample = new JButton();
	btnRemoveSample.setActionCommand("RemoveSample");
	btnRemoveSample.setToolTipText("Remove selected sample from list");
	btnRemoveSample.setIcon(Builder.getIcon("cancel.png", 16));
	btnRemoveSample.addActionListener(this);
  	getContentPane().add(btnRemoveSample, "cell 1 2");
  	
  	JPanel panelBarcodeInstructions = new JPanel();
  	getContentPane().add(panelBarcodeInstructions, "cell 0 4,grow");
	panelBarcodeInstructions.setLayout(new MigLayout("", "[][grow]", "[grow,top]"));
	
	JLabel lblInfoIcon = new JLabel("");
	lblInfoIcon.setIcon(Builder.getIcon("info.png", 16));
	panelBarcodeInstructions.add(lblInfoIcon, "cell 0 0");
	
	JTextArea txtSampleInstructions = new JTextArea();
	txtSampleInstructions.setWrapStyleWord(true);
	txtSampleInstructions.setText(barcodeInstructions);
	txtSampleInstructions.setOpaque(false);
	txtSampleInstructions.setLineWrap(true);
	txtSampleInstructions.setFont(new Font("Dialog", Font.PLAIN, 9));
	txtSampleInstructions.setEditable(false);
	txtSampleInstructions.setBorder(new EmptyBorder(5, 5, 5, 5));
	txtSampleInstructions.setBackground(new Color(0,0,0,0));
			

	panelBarcodeInstructions.add(txtSampleInstructions, "cell 1 0,growx,wmin 10,aligny top");
	
  	
  	
  	
  	
  	
  	JButton btnOk = new JButton("OK");
  	btnOk.setActionCommand("OK");
  	btnOk.addActionListener(this);
  	getContentPane().add(btnOk, "cell 0 5 2 1,alignx right");
  
  	
  	this.setModal(true);
  	this.pack();
  	this.setSize(new Dimension(500,400));
  	this.setLocationRelativeTo(null);
  	this.setVisible(true);
  	
  	

    }

	private void addSamplesByScan(SearchParameters param)
	{
		
		if(!param.getReturnObject().equals(SearchReturnObject.SAMPLE))
		{
			log.error("Return object type passed to addSamplesByScan() must be a sample");
			return;
		}
		

		// we want an object returned here
		EntitySearchResource<TridasObject> resource = new EntitySearchResource<TridasObject>(param, TridasObject.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting loans");
			return;
		}
		java.util.List<TridasSample> sampList = SampleLabelPrintingUI.getSamplesList(resource.getAssociatedResult(), null, null);
		addSamplesToList(sampList);

	}
	
	
	private void handleSampleTableBorder()
	{
		if(btnAddSample.isVisible())
		{
			scrollPaneSamples.setBorder(new StrokeBorder(Color.YELLOW, 4));
		}
		else
		{
			scrollPaneSamples.setBorder(BorderFactory.createLineBorder(Color.GRAY));		
		}
	}
  

	private void addSamplesToList(Collection<TridasSample> sampList)
	{
		
		for(TridasSample s : sampList){
			
            sampleModel.addSample(s);
        	
		}
				
		tblSamples.clearSelection();

	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("AddSample"))
		{
			ITridas returned = TridasEntityPickerDialog.pickEntity(
					null, "Pick Sample", TridasSample.class, EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT);
			if(returned instanceof TridasSample)
			{
		    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
		    	param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, returned.getIdentifier().getValue());
				addSamplesByScan(param);	
			}
			else if (returned instanceof TridasElement)
			{
		    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
		    	param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, returned.getIdentifier().getValue());
				addSamplesByScan(param);
			}
			else if (returned instanceof TridasObject)
			{
		    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
		    	param.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, returned.getIdentifier().getValue());
				addSamplesByScan(param);
			}
		}
		else if (event.getActionCommand().equals("RemoveSample"))
		{
			if(this.tblSamples.getSelectedRows().length > 0) {
		          int[] selectedIndices = tblSamples.getSelectedRows();
		          for (int i = selectedIndices.length-1; i >=0; i--) {
		              sampleModel.removeSample(selectedIndices[i]);
		          } 
		    } 
		}
		else if (event.getActionCommand().equals("addUnprepped"))
		{
			addUnpreppedSamples();
		}
		else if (event.getActionCommand().equals("addPrepped"))
		{
			addPreppedSamples();
		}
		else if (event.getActionCommand().equals("addUndateable"))
		{
			addUndateableSamples();
		}
		else if (event.getActionCommand().equals("updateStatus"))
		{
			updateStatus();
		}
		else if (event.getActionCommand().equals("OK"))
		{
			dispose();
		}
		else if (event.getActionCommand().equals("printTable"))
		{
		 	try {
		  	    boolean complete = tblSamples.print();
		  	    if (complete) {
		  	       
		  	    } else {
		  	        Alert.message("Printing", "Printing cancelled");
		  	    }
		  	} catch (PrinterException pe) {
		  		Alert.error("Printing", "Printing failed.  "+pe.getLocalizedMessage());
		  	}
		}
	}
    
	private void addUnpreppedSamples()
	{
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
    	param.addSearchConstraint(SearchParameterName.SAMPLESTATUS, SearchOperator.EQUALS, SampleStatus.UNPREPPED.value());
		addSamplesByScan(param);
	}
	
	private void addPreppedSamples()
	{
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
    	param.addSearchConstraint(SearchParameterName.SAMPLESTATUS, SearchOperator.EQUALS, SampleStatus.PREPPED.value());
		addSamplesByScan(param);
		
	}
	
	private void addUndateableSamples()
	{
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);    	
    	param.addSearchConstraint(SearchParameterName.SAMPLESTATUS, SearchOperator.EQUALS, SampleStatus.UNDATEABLE.value());
		addSamplesByScan(param);
	}
	
	private void updateStatus()
	{
		Object[] possibilities = {SampleStatus.DATED.value(), SampleStatus.MEASURED.value(), SampleStatus.PREPPED.value(), SampleStatus.TOO___FEW___RINGS.value(), SampleStatus.UNDATEABLE.value(), SampleStatus.UNPREPPED.value()};
		String s = (String)JOptionPane.showInputDialog(
		                    this,
		                    "Change status of all samples in the list to:\n",
		                    "Set sample status",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    possibilities,
		                    SampleStatus.PREPPED.value());

		//If a string was returned, say so.
		if (s == null) {
		    log.debug("No choice made");
			return;
		}
		
		ArrayList<TridasSample> updatedList = new ArrayList<TridasSample>();
		
		for(TridasSample sample: sampleModel.getTridasSamples())
		{
			TridasGenericField sampleStatusField = TridasUtils.getGenericFieldByName(sample, "tellervo.sampleStatus");
			
			if(sampleStatusField==null) continue;
			
			sampleStatusField.setValue(s);
			
			// Create resource
			EntityResource<TridasSample> resource;
			
			resource = new EntityResource<TridasSample>(sample, TellervoRequestType.UPDATE, TridasSample.class);
		
			TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(this, resource);
			
			resource.query();
			dialog.setVisible(true);
			
			if(!dialog.isSuccessful()) 
			{ 
				Alert.error("Error", dialog.getFailException().getMessage());
				return;
			}
			
			
			updatedList.add(resource.getAssociatedResult());

		}

		// Replace model with updated entries
		//sampleModel.clear();
		//addSamplesToList(updatedList);
		
	}

} 