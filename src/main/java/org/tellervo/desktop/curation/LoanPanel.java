package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import net.miginfocom.swing.MigLayout;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.curation.LoanDialog.LoanDialogMode;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.ui.TridasFileListPanel;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.ui.I18n.TellervoLocale;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tellervo.desktop.util.labels.ui.SampleLabelPrintingUI;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSILoan;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import com.michaelbaranov.microba.calendar.DatePicker;

public class LoanPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtOrganisation;
	private DatePicker dpIssueDate;
	private DatePicker dpDueDate ;

	private JTextArea txtNotes;
	private JPanel panelSummary;
	private JButton btnAddSample;
	private JButton btnRemoveSample;
	private JButton btnViewSample;
	private JToolBar toolBar;
	private JButton btnNewLoan;
	private JButton btnReturnLoan;
	private JButton btnEditLoan;
	private JButton btnSave;
	private JLabel lblOverdue;
	private JLabel lblperiod;
	private DatePicker dpReturnDate;
	private JScrollPane scrollPaneSamples;
	private final static Logger log = LoggerFactory.getLogger(LoanPanel.class);
	
	
	private WSILoan loan;
	private TridasFileListPanel panelFiles;
	private SampleListTableModel sampleModel;
	private JPanel panelBarcodeInstructions;
	private JLabel lblInfoIcon;
	private JTextArea txtSampleInstructions;

	private WSILoan nonDirtyLoan;
	
	private String keyBuffer = "";
	private JButton btnCancelChanges;

	private static String barcodeInstructions = "To quickly add multiple samples to this loan, highlight the list above and scan sample barcodes.  You can also scan box barcodes to loan all samples from a specific box.";
	private static String samplesAreFinal = "The samples associated with a loan cannot be changed.  If changes are required this loan should be returned and a new loan set up.";
	private JLabel lblLoanId;
	private JTextField txtLoanID;
    private List<LoanListener> listeners = new ArrayList<LoanListener>();
    private JTable tblSamples;
    
    
	/**
	 * Create the panel.
	 */
	public LoanPanel() {
		
		initGUI();
	}

	public void addListener(LoanListener toAdd) {
	        listeners.add(toAdd);
	}
	
	public void fireChangesMadeToLoan() {
	        // Notify everybody that may be interested.
	        for (LoanListener l : listeners) l.changesMadeToLoan();
	}
	 
	 
	/**
	 * Set whether the GUI is editable or not.  Should be called *after* 
	 * the loan has been set.
	 * 
	 * @param b
	 */
	public void setEditable(Boolean b)
	{
		
		// First tab 
		txtFirstName.setEditable(b);
		txtFirstName.setFocusable(b);
		txtLastName.setEditable(b);
		txtLastName.setFocusable(b);
		txtOrganisation.setEditable(b);
		txtOrganisation.setFocusable(b);
		dpIssueDate.setEnabled(false);
		dpReturnDate.setEnabled(false);
		dpDueDate.setEnabled(b);
		btnCancelChanges.setEnabled(b);
		btnSave.setEnabled(b);
		btnNewLoan.setEnabled(!b);
		btnEditLoan.setEnabled(!b);
		btnReturnLoan.setEnabled(!b);
		txtNotes.setEditable(b);
		txtNotes.setFocusable(b);
		
		
		// Second tab
		btnAddSample.setVisible(b);
		btnRemoveSample.setVisible(b);
		panelBarcodeInstructions.setVisible(b);
		if(loan!=null && loan.isSetIdentifier() && b)
		{
			// Override and set false when editing an existing loan
			btnAddSample.setVisible(false);
			btnRemoveSample.setVisible(false);
			panelBarcodeInstructions.setVisible(true);
			txtSampleInstructions.setText(samplesAreFinal);
		}
		else
		{
			txtSampleInstructions.setText(barcodeInstructions);
		}
		handleSampleTableBorder();
		
		// Third tab
		panelFiles.setReadOnly(!b);
		
		

	}
	
	public void setGUIForMode(LoanDialogMode mode)
	{
		if(mode.equals(LoanDialogMode.NEW))
		{
			setLoan(new WSILoan());
			nonDirtyLoan = new WSILoan();
			setEditable(true);
			btnNewLoan.setEnabled(false);
			btnReturnLoan.setEnabled(false);
			btnEditLoan.setEnabled(false);
		}
		else
		{
			setEditable(false);
		}
	}
	
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		add(tabbedPane);
		
		panelSummary = new JPanel();
		tabbedPane.addTab("Summary", null, panelSummary, null);
		panelSummary.setLayout(new MigLayout("", "[right][135px,grow,fill][135px,grow,fill]", "[][][][][][][grow]"));
		
		lblLoanId = new JLabel("Loan ID:");
		panelSummary.add(lblLoanId, "cell 0 0,alignx trailing");
		
		txtLoanID = new JTextField();
		txtLoanID.setEnabled(false);
		txtLoanID.setEditable(false);
		panelSummary.add(txtLoanID, "cell 1 0 2 1,growx");
		txtLoanID.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		panelSummary.add(lblName, "cell 0 1");
		
		txtFirstName = new JTextField();
		txtFirstName.setToolTipText("First name of borrower");
		panelSummary.add(txtFirstName, "cell 1 1");
		txtFirstName.setColumns(10);
		
		txtLastName = new JTextField();
		txtLastName.setToolTipText("Last name of borrower");
		panelSummary.add(txtLastName, "cell 2 1");
		txtLastName.setColumns(10);
		
		JLabel lblOrganisation = new JLabel("Organisation:");
		panelSummary.add(lblOrganisation, "cell 0 2");
		
		txtOrganisation = new JTextField();
		txtOrganisation.setToolTipText("Borrowing organisation");
		panelSummary.add(txtOrganisation, "cell 1 2 2 1");
		txtOrganisation.setColumns(10);
		
		JLabel lblIssued = new JLabel("Date issued");
		panelSummary.add(lblIssued, "cell 0 3");
		
		dpIssueDate = new DatePicker();
		dpIssueDate.setToolTipText("Date the loan was issued");
		panelSummary.add(dpIssueDate, "cell 1 3");
		dpIssueDate.setEnabled(false);
		
		JLabel lblDateDue = new JLabel("Date due:");
		panelSummary.add(lblDateDue, "cell 0 4,alignx trailing");
		
		lblperiod = new JLabel("");
		panelSummary.add(lblperiod, "cell 2 4");
		
		JLabel lblDateReturned = new JLabel("Date returned:");
		panelSummary.add(lblDateReturned, "cell 0 5,alignx trailing");
		
		dpReturnDate = new DatePicker();
		dpReturnDate.setToolTipText("Date the loan was returned");
		panelSummary.add(dpReturnDate, "cell 1 5,growx");
		
		lblOverdue = new JLabel("");
		panelSummary.add(lblOverdue, "cell 2 5");
		
		JLabel lblNotes = new JLabel("Notes:");
		panelSummary.add(lblNotes, "cell 0 6,aligny top");
		
		final JScrollPane scrollPane = new JScrollPane();
		panelSummary.add(scrollPane, "cell 1 6 2 1,grow");
		
		txtNotes = new JTextArea();
		txtNotes.setWrapStyleWord(true);
		txtNotes.setLineWrap(true);
		txtNotes.setToolTipText("Additional notes about this loan");
		scrollPane.setViewportView(txtNotes);
		
		dpDueDate = new DatePicker();
		panelSummary.add(dpDueDate, "cell 1 4");
		
		JPanel panelSamples = new JPanel();
		tabbedPane.addTab("Samples", null, panelSamples, null);
		panelSamples.setLayout(new MigLayout("hidemode 3", "[grow][fill]", "[][][grow][][]"));
		
		scrollPaneSamples = new JScrollPane();
		panelSamples.add(scrollPaneSamples, "cell 0 0 1 4,grow");
		
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
		
		tblSamples = new JTable();
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
		btnAddSample.setToolTipText("Add sample to loan");
		btnAddSample.setIcon(Builder.getIcon("edit_add.png", 16));
		btnAddSample.addActionListener(this);
		panelSamples.add(btnAddSample, "cell 1 0");
		
		btnRemoveSample = new JButton();
		btnRemoveSample.setActionCommand("RemoveSample");
		btnRemoveSample.setToolTipText("Remove selected sample from loan");
		btnRemoveSample.setIcon(Builder.getIcon("cancel.png", 16));
		btnRemoveSample.addActionListener(this);
		panelSamples.add(btnRemoveSample, "cell 1 1");
		
		btnViewSample = new JButton();
		btnViewSample.setActionCommand("ViewSample");
		btnViewSample.setToolTipText("View details of selected sample");
		btnViewSample.setIcon(Builder.getIcon("document_preview.png", 16));
		btnViewSample.addActionListener(this);
		panelSamples.add(btnViewSample, "cell 1 3");
		
		panelBarcodeInstructions = new JPanel();
		panelSamples.add(panelBarcodeInstructions, "cell 0 4 2 1,grow");
		panelBarcodeInstructions.setLayout(new MigLayout("", "[][grow]", "[grow,top]"));
		
		lblInfoIcon = new JLabel("");
		lblInfoIcon.setIcon(Builder.getIcon("info.png", 16));
		panelBarcodeInstructions.add(lblInfoIcon, "cell 0 0");
		
		txtSampleInstructions = new JTextArea();
		txtSampleInstructions.setWrapStyleWord(true);
		txtSampleInstructions.setText(barcodeInstructions);
		txtSampleInstructions.setOpaque(false);
		txtSampleInstructions.setLineWrap(true);
		txtSampleInstructions.setFont(new Font("Dialog", Font.PLAIN, 9));
		txtSampleInstructions.setEditable(false);
		txtSampleInstructions.setBorder(new EmptyBorder(5, 5, 5, 5));
		txtSampleInstructions.setBackground(new Color(0,0,0,0));
				
	
		panelBarcodeInstructions.add(txtSampleInstructions, "cell 1 0,growx,wmin 10,aligny top");
		
		panelFiles = new TridasFileListPanel();
		tabbedPane.addTab("Associated Files", null, panelFiles, null);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);
		
		btnNewLoan = new JButton("New Loan");
		btnNewLoan.setActionCommand("NewLoan");
		btnNewLoan.setIcon(Builder.getIcon("filenew.png", 22));
		btnNewLoan.setToolTipText("Enter details of a new loan");
		btnNewLoan.setFocusable(false);
		btnNewLoan.addActionListener(this);
		toolBar.add(btnNewLoan);
		
		btnReturnLoan = new JButton("Check in");
		btnReturnLoan.setActionCommand("ReturnLoan");
		btnReturnLoan.setIcon(Builder.getIcon("checkin.png", 22));
		btnReturnLoan.setToolTipText("Mark loan as returned");
		btnReturnLoan.setFocusable(false);
		btnReturnLoan.addActionListener(this);
		toolBar.add(btnReturnLoan);
		
		btnEditLoan = new JButton("Edit");
		btnEditLoan.setActionCommand("EditLoan");
		btnEditLoan.setIcon(Builder.getIcon("edit.png", 22));
		btnEditLoan.setToolTipText("Edit the details of this loan");
		btnEditLoan.setFocusable(false);
		btnEditLoan.addActionListener(this);
		toolBar.add(btnEditLoan);
		
		btnSave = new JButton("Save changes");
		btnSave.setActionCommand("Save");
		btnSave.setIcon(Builder.getIcon("filesave.png", 22));
		btnSave.setToolTipText("Save changes");
		btnSave.setFocusable(false);
		btnSave.addActionListener(this);
		toolBar.add(btnSave);
		
		btnCancelChanges = new JButton("Cancel changes");
		btnCancelChanges.setIcon(Builder.getIcon("button_cancel.png", 22));
		btnCancelChanges.setActionCommand("CancelChanges");
		btnCancelChanges.setToolTipText("Cancel changes to loan");
		btnCancelChanges.setFocusable(false);
		btnCancelChanges.addActionListener(this);
		toolBar.add(btnCancelChanges);
		
	}
	
	private void handleSampleTableBorder()
	{
		if(btnSave.isEnabled())
		{
			scrollPaneSamples.setBorder(new StrokeBorder(Color.YELLOW, 4));
		}
		else
		{
			scrollPaneSamples.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, false));		
		}
	}
	
	private void showCurationHistoryForSelectedSample()
	{
		if(tblSamples.getSelectedRow()!=-1)
		{
			CurationDialog dialog = new CurationDialog(sampleModel.getSample(tblSamples.getSelectedRow()), this);
			dialog.setVisible(true);
		}
	}
	
	private void setSamples()
	{
		sampleModel.clear();

		if(loan==null) 
		{
			log.debug("Loan is null in setSamples() so returning");
			return;
		}
		
		
		if(!loan.isSetIdentifier())
		{
			// No identifier so this must be a fresh local loan
			log.debug("Loan has no identifier so must be a fresh local loan");
			return;
		}
		
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	param.addSearchConstraint(SearchParameterName.LOANID, SearchOperator.EQUALS, loan.getIdentifier().getValue() );

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
		
		java.util.List<TridasObject> objList = resource.getAssociatedResult();
		java.util.List<TridasSample> sampList = SampleLabelPrintingUI.getSamplesList(objList, null, null);
				
		addSamplesToList(sampList);
		
	}
	
	private void addSamplesToList(Collection<TridasSample> sampList)
	{
		addSamplesToList(sampList, false);
	}
	
	private void addSamplesToList(Collection<TridasSample> sampList, Boolean warnOnLoaned)
	{
		ArrayList<TridasSample> alreadyLoanedSamples = new ArrayList<TridasSample>();
		
		for(TridasSample s : sampList){
			
        	TridasGenericField curationStatus = GenericFieldUtils.findField(s, "tellervo.curationStatus");
        	if(curationStatus.isSetValue() && curationStatus.getValue().startsWith("On loan"))
        	{
        		alreadyLoanedSamples.add(s);
        	}
        	else
        	{
        		sampleModel.addSample(s);
        	}
		}
				
		if(alreadyLoanedSamples.size()>0)
		{
			
			if(!warnOnLoaned)
			{
				for (TridasSample s : alreadyLoanedSamples)
				{
					sampleModel.addSample(s);
				}
			}
			else
			{
			
				String question = "";
				if(alreadyLoanedSamples.size()==1)
				{
					question = "The sample that you selected is already on loan and cannot be included in this loan.\n" +
							   "Would you like this added to the list so that you can investigate it? Note it will \n"+
							   "need to be removed before you can confirm the loan.";
				}
				else
				{
					question = alreadyLoanedSamples.size()+ " samples that you selected are already on loan and cannot be included in this loan.\n" +
							   "Would you like these added to the list so that you can see which they are? Note they \n"+
							   "will need to be removed before you can confirm the loan.";
				}
				
				
				Object[] options = {"Yes", "No"};
	
				int n = JOptionPane.showOptionDialog(this, 
						question,
					"Confirm add to list",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					options,
					options[1]);
				
				if(n==JOptionPane.YES_OPTION)
				{
					for (TridasSample s : alreadyLoanedSamples)
					{
						sampleModel.addSample(s);
					}
				}
			}

		}
		
		tblSamples.clearSelection();

		
	}
	

    
    public void pack()
    {
    	panelFiles.hidePreviewPanel();
    }
	
    
    /**
     * Set the GUI for the specified loan
     * 
     * @param loan
     */
	public void setLoan(WSILoan loan)
	{
		this.loan= loan; 
		nonDirtyLoan = loan;
		
		
		setEditable(false);
		
		if(loan==null)
		{
			txtLoanID.setText("");
			txtFirstName.setText("");
			txtLastName.setText("");
			txtOrganisation.setText("");
			txtNotes.setText("");
			
			try {
				dpDueDate.setDate(null);
				dpIssueDate.setDate(null);
				dpReturnDate.setDate(null);

			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			
			lblperiod.setText("");
			lblOverdue.setText("");
			panelFiles.setFileList(new ArrayList<TridasFile>());
			setSamples();
			
			return;
		}
		

		
		String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
		String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
		TellervoLocale loc = I18n.getTellervoLocale(country, language);
		
		
		if(loan.isSetIdentifier())
		{
			txtLoanID.setText(loan.getIdentifier().getValue());
		}
		
		txtFirstName.setText(loan.getFirstname());
		txtLastName.setText(loan.getLastname());
		txtOrganisation.setText(loan.getOrganisation());
		
		txtNotes.setText(loan.getNotes());
		if(loan.isSetDuedate())
		{
			try {
				dpDueDate.setDate(loan.getDuedate().toGregorianCalendar().getTime());
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				dpDueDate.setDate(null);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		if(loan.isSetIssuedate())
		{
			try {
				dpIssueDate.setDate(loan.getIssuedate().toGregorianCalendar().getTime());
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		else if(!loan.isSetIdentifier())
		{
			// Must be a new local loan
			//txtIssueDate.setText(dateFormat.format(LocalDate.now()));
		}
		else 
		{
			try {
				dpIssueDate.setDate(null);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		if(loan.isSetReturndate())
		{
			try {
				dpReturnDate.setDate(loan.getReturndate().toGregorianCalendar().getTime());
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				dpReturnDate.setDate(null);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		
		if(loan.isSetIssuedate() && loan.isSetDuedate())
		{
			Period period = new Period(LocalDate.fromCalendarFields(loan.getIssuedate().toGregorianCalendar()), 
							 LocalDate.fromCalendarFields(loan.getDuedate().toGregorianCalendar()));
						
			lblperiod.setText("Loan period: "+PeriodFormat.wordBased(loc.getLocale()).print(period));
			
			
		}
		else
		{
			lblperiod.setText("");
		}
		
		if(loan.isSetDuedate() && !loan.isSetReturndate() )
		{
			if(LocalDate.fromCalendarFields(loan.getDuedate().toGregorianCalendar()).isBefore(LocalDate.now()))
			{
				Period period = new Period(LocalDate.fromCalendarFields(loan.getDuedate().toGregorianCalendar()), LocalDate.now());
					
				lblOverdue.setText("Overdue by: "+PeriodFormat.wordBased(loc.getLocale()).print(period));
				lblOverdue.setForeground(Color.RED);
			}
			else
			{
				lblOverdue.setText("");
			}
		}
		else
		{
			lblOverdue.setText("");
		}
		
		
		panelFiles.setFileList((ArrayList<TridasFile>) loan.getFiles());
		
		setSamples();
		
	}
	
	private WSILoan getLoanRepresentationFromGUI()
	{
		
		WSILoan guiRepOfLoan = new WSILoan();

		
		if(loan.isSetIdentifier())
		{
			guiRepOfLoan.setIdentifier(loan.getIdentifier());
		}
		
		if(txtFirstName.getText()!=null && txtFirstName.getText().length()>0)
		{
			guiRepOfLoan.setFirstname(txtFirstName.getText());
		}
		
		if(txtLastName.getText()!=null && txtLastName.getText().length()>0)
		{
			guiRepOfLoan.setLastname(txtLastName.getText());
		}
		
		if(txtOrganisation.getText()!=null && txtOrganisation.getText().length()>0)
		{
			guiRepOfLoan.setOrganisation(txtOrganisation.getText());
		}
		
		if(txtNotes.getText()!=null && txtNotes.getText().length()>0)
		{
			guiRepOfLoan.setNotes(txtNotes.getText());
		}
		

		try {
			GregorianCalendar c = new GregorianCalendar();
			
			if(dpDueDate.getDate()!=null)
			{
				c.setTime(dpDueDate.getDate());
				guiRepOfLoan.setDuedate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
			}
			
			if(dpReturnDate.getDate()!=null)
			{
				c.setTime(dpReturnDate.getDate());
				guiRepOfLoan.setReturndate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
			}
			
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		

		guiRepOfLoan.setSamples(sampleModel.getTridasSamples());
		
		guiRepOfLoan.setFiles(panelFiles.getFileList());
		
		return guiRepOfLoan;

	}
	
	
	/**
	 * Has the loan been changed in the GUI?
	 * 
	 * @return
	 */
	public Boolean isModified()
	{
		WSILoan currentRep = null;
		WSILoan oldRep = null;
		
		try{
			currentRep = getLoanRepresentationFromGUI();
			oldRep = (WSILoan) nonDirtyLoan.clone();
		} catch (NullPointerException e)
		{
			// Means one or other is not set 
			//log.debug("One or other copies of loan is not set so returning isModified as false");
			return false;
		}
		
		try{
			if(oldRep.isSetFirstname() || currentRep.isSetFirstname())	{
				if(!oldRep.getFirstname().equals(currentRep.getFirstname())) 
				{
					//log.debug("Firstname different");
					return true;
				}
			}
			if(oldRep.isSetLastname() || currentRep.isSetLastname()){
				if(!oldRep.getLastname().equals(currentRep.getLastname())) {
					//log.debug("Lastname different");
					return true;
				}
			}
			if(oldRep.isSetOrganisation() || currentRep.isSetOrganisation()){ 
				if(!oldRep.getOrganisation().equals(currentRep.getOrganisation())) {
					//log.debug("Organisation different");
					return true;
				}
			}
			if(oldRep.isSetDuedate() || currentRep.isSetDuedate()){ 	   
				if(!oldRep.getDuedate().equals(currentRep.getDuedate())) {
					//log.debug("Duedate different");
					return true;
				}
			}
			if(oldRep.isSetReturndate() || currentRep.isSetReturndate()){   
				if(!oldRep.getReturndate().equals(currentRep.getReturndate())){
					//log.debug("Returndate different");
					return true;
				}
			}
			if(oldRep.isSetNotes() || currentRep.isSetNotes()){
				if(!oldRep.getNotes().equals(currentRep.getNotes())) {
					//log.debug("Notes different");
					return true;
				}
			}
			if(oldRep.isSetFiles() || currentRep.isSetFiles()){
				if(!oldRep.getFiles().equals(currentRep.getFiles())) {
					//log.debug("Files different");
					return true;
				}
			}
					
			// All seems to be the same so return false
			return false;
		
			
		}
		catch (NullPointerException e)
		{
			// This means that one or other was null when the other was populated 
			//log.debug("One or other value was null when the other was populated");
			return true;
		}
		
		
	}
	
	public Boolean saveChanges()
	{
		if(!isModified()) {
			log.debug("Loan not modified, so no need to save changes");
			setEditable(false);
			return true;
		}
		
		if(tblSamples.getModel().getRowCount()==0)
		{
			Alert.error("No samples", "No samples have been included in this loan.");
			return false;
		}
		
		WSILoan guiRepOfloan = getLoanRepresentationFromGUI();

		// Create resource
		EntityResource<WSILoan> resource;
		
		Boolean isNewRecord = !guiRepOfloan.isSetIdentifier();
		if(isNewRecord)
		{
			resource = new EntityResource<WSILoan>(guiRepOfloan, TellervoRequestType.CREATE, WSILoan.class);
		}
		else
		{
			resource = new EntityResource<WSILoan>(guiRepOfloan, TellervoRequestType.UPDATE, WSILoan.class);
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
		
		loan = resource.getAssociatedResult();
				
		setLoan(loan);
		setEditable(false);
		fireChangesMadeToLoan();
		this.setGUIForMode(LoanDialogMode.BROWSE_EDIT);
		return true;
	}

	/**
	 * Check in the current loan marking all samples as 'archived'
	 * again.
	 * 
	 */
	private void checkInLoan()
	{
		
		if(loan.getReturndate()!=null){
			log.debug("Loan already returned");
			return;
		}
		
		Object[] options = {"Yes",
				"No",
                I18n.getText("general.cancel")};

		int n = JOptionPane.showOptionDialog(this, 
			"All samples covered by this loan and currently marked as 'on loan' will be marked as 'archived'.\n" +
			"If any samples are missing you should cancel and mark them as so on the samples tab before\n" +
			"proceeding." +
			"Are you sure you want to continue? ",
			"Confirm check in",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[0]);
		
		if(n==JOptionPane.CANCEL_OPTION || n==JOptionPane.NO_OPTION) return;
		
		Date now = new Date();
		try {
			dpReturnDate.setDate(now);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		
		saveChanges();
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if(event.getActionCommand().equals("ViewSample"))
		{
			showCurationHistoryForSelectedSample();
		}
		else if (event.getActionCommand().equals("NewLoan"))
		{
			setGUIForMode(LoanDialogMode.NEW);
		}
		else if (event.getActionCommand().equals("Save"))
		{
			saveChanges();
		}
		else if (event.getActionCommand().equals("EditLoan"))
		{
			if(loan!=null)
			{
				nonDirtyLoan = (WSILoan) loan.clone();
			}
			else
			{
				nonDirtyLoan = loan;
			}
			setEditable(true);
			txtFirstName.requestFocusInWindow();
		}
		else if (event.getActionCommand().equals("CancelChanges"))
		{
			setLoan(nonDirtyLoan);
			setEditable(false);
		}
		else if (event.getActionCommand().equals("AddSample"))
		{
			ITridas returned = TridasEntityPickerDialog.pickEntity(null, "Pick Sample", TridasSample.class, EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT);
			if(returned instanceof TridasSample)
			{
				ArrayList<TridasSample> list = new ArrayList<TridasSample>();
				list.add((TridasSample) returned);
				addSamplesToList(list, true);				
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
		else if (event.getActionCommand().equals("ReturnLoan"))
		{
			checkInLoan();
		}
		else
		{
			log.error("Unknown ActionCommnad: "+event.getActionCommand());
		}
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
		addSamplesToList(sampList, true);

	}
	
	
	interface LoanListener {
	    public void changesMadeToLoan();
	}

}
