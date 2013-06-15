package org.tellervo.desktop.admin.curation;

import static java.text.DateFormat.LONG;
import static java.text.DateFormat.getDateInstance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.TridasFileListPanel;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.ui.I18n.TellervoLocale;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.labels.ui.SampleLabelPrintingUI;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
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
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSILoan;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.xml.datatype.XMLGregorianCalendar;

import java.awt.Font;

public class LoanPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtOrganisation;
	private JTextField txtIssueDate;
	private JTextField txtDueDate;
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
	private JTextField txtReturned;
	private final static Logger log = LoggerFactory.getLogger(LoanPanel.class);
	
	
	private WSILoan loan;
	private Boolean modified = false;
	private TridasFileListPanel panelFiles;
	private JList<TridasSample> lstSamples;
	private ArrayListModel<TridasSample> sampleModel;
	private JPanel panel;
	private JLabel lblInfoIcon;
	private JTextArea txtrToQuicklyAdd;

	/**
	 * Create the panel.
	 */
	public LoanPanel() {
		
		initGUI();
	}

	public void setEditable(Boolean b)
	{
		log.debug("Setting LoanPanel to be editable: " +b);
		txtFirstName.setEditable(b);
		txtLastName.setEditable(b);
		txtOrganisation.setEditable(b);
		txtIssueDate.setEditable(b);
		
		txtReturned.setEditable(b);
		txtNotes.setEditable(b);
		btnAddSample.setVisible(b);
		btnRemoveSample.setVisible(b);
		
		panelFiles.setReadOnly(!b);
	}
	
	@SuppressWarnings("unchecked")
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		add(tabbedPane);
		
		panelSummary = new JPanel();
		tabbedPane.addTab("Summary", null, panelSummary, null);
		panelSummary.setLayout(new MigLayout("", "[right][135px,grow,fill][135px,grow,fill]", "[][][][][][grow]"));
		
		JLabel lblName = new JLabel("Name:");
		panelSummary.add(lblName, "cell 0 0");
		
		txtFirstName = new JTextField();
		txtFirstName.setToolTipText("First name of borrow");
		panelSummary.add(txtFirstName, "cell 1 0");
		txtFirstName.setColumns(10);
		
		txtLastName = new JTextField();
		txtLastName.setToolTipText("Last name of borrow");
		panelSummary.add(txtLastName, "cell 2 0");
		txtLastName.setColumns(10);
		
		JLabel lblOrganisation = new JLabel("Organisation:");
		panelSummary.add(lblOrganisation, "cell 0 1");
		
		txtOrganisation = new JTextField();
		txtOrganisation.setToolTipText("Borrowing organisation");
		panelSummary.add(txtOrganisation, "cell 1 1 2 1");
		txtOrganisation.setColumns(10);
		
		JLabel lblIssued = new JLabel("Date issued");
		panelSummary.add(lblIssued, "cell 0 2");
		
		txtIssueDate = new JTextField();
		txtIssueDate.setToolTipText("Date the loan was issued");
		panelSummary.add(txtIssueDate, "cell 1 2");
		txtIssueDate.setEditable(false);
		txtIssueDate.setColumns(10);
		
		JLabel lblDateDue = new JLabel("Date due:");
		panelSummary.add(lblDateDue, "cell 0 3,alignx trailing");
		
		txtDueDate = new JTextField();
		txtDueDate.setEditable(false);
		txtDueDate.setToolTipText("Date the loan is due");
		panelSummary.add(txtDueDate, "flowx,cell 1 3");
		txtDueDate.setColumns(10);
		
		lblperiod = new JLabel("");
		panelSummary.add(lblperiod, "cell 2 3");
		
		JLabel lblDateReturned = new JLabel("Date returned:");
		panelSummary.add(lblDateReturned, "cell 0 4,alignx trailing");
		
		txtReturned = new JTextField();
		txtReturned.setToolTipText("Date the loan was returned");
		txtReturned.setColumns(10);
		panelSummary.add(txtReturned, "cell 1 4,growx");
		
		lblOverdue = new JLabel("");
		panelSummary.add(lblOverdue, "cell 2 4");
		
		JLabel lblNotes = new JLabel("Notes:");
		panelSummary.add(lblNotes, "cell 0 5,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		panelSummary.add(scrollPane, "cell 1 5 2 1,grow");
		
		txtNotes = new JTextArea();
		txtNotes.setWrapStyleWord(true);
		txtNotes.setLineWrap(true);
		txtNotes.setToolTipText("Additional notes about this loan");
		scrollPane.setViewportView(txtNotes);
		
		JPanel panelSamples = new JPanel();
		tabbedPane.addTab("Samples", null, panelSamples, null);
		panelSamples.setLayout(new MigLayout("", "[grow][fill]", "[][][grow][][]"));
		
		JScrollPane scrollPaneSamples = new JScrollPane();
		panelSamples.add(scrollPaneSamples, "cell 0 0 1 4,grow");
		
		lstSamples = new JList<TridasSample>();
		sampleModel = new ArrayListModel<TridasSample>();
		lstSamples.setModel(sampleModel);
		lstSamples.setCellRenderer(new TridasListCellRenderer());
		
		
		lstSamples.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		lstSamples.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()>1)
				{
					showCurationHistoryForSelectedSample();
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {		
			}

			@Override
			public void mouseExited(MouseEvent arg0) {				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {				
			}
			
		});
		
		scrollPaneSamples.setViewportView(lstSamples);
		
		JLabel lblSamplesIncludedIn = new JLabel("Samples included in this loan:");
		scrollPaneSamples.setColumnHeaderView(lblSamplesIncludedIn);
		
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
		
		panel = new JPanel();
		panelSamples.add(panel, "cell 0 4 2 1,grow");
		panel.setLayout(new MigLayout("", "[][grow]", "[grow,top]"));
		
		lblInfoIcon = new JLabel("");
		lblInfoIcon.setIcon(Builder.getIcon("info.png", 16));
		panel.add(lblInfoIcon, "cell 0 0");
		
		txtrToQuicklyAdd = new JTextArea();
		txtrToQuicklyAdd.setWrapStyleWord(true);
		txtrToQuicklyAdd.setText("To quickly add multiple samples to this loan, highlight the list above and scan sample barcodes.  You can also scan box barcodes to loan all samples from a specific box.");
		txtrToQuicklyAdd.setOpaque(false);
		txtrToQuicklyAdd.setLineWrap(true);
		txtrToQuicklyAdd.setFont(new Font("Dialog", Font.PLAIN, 9));
		txtrToQuicklyAdd.setEditable(false);
		txtrToQuicklyAdd.setBorder(new EmptyBorder(5, 5, 5, 5));
		txtrToQuicklyAdd.setBackground(new Color(0,0,0,0));
				
	
		panel.add(txtrToQuicklyAdd, "cell 1 0,growx,wmin 10,aligny top");
		
		panelFiles = new TridasFileListPanel();
		tabbedPane.addTab("Associated Files", null, panelFiles, null);
		
		toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.WEST);
		
		btnNewLoan = new JButton("");
		btnNewLoan.setActionCommand("NewLoan");
		btnNewLoan.setIcon(Builder.getIcon("filenew.png", 22));
		btnNewLoan.setToolTipText("Enter details of a new loan");
		btnNewLoan.setFocusable(false);
		btnNewLoan.addActionListener(this);
		toolBar.add(btnNewLoan);
		
		btnReturnLoan = new JButton("");
		btnReturnLoan.setActionCommand("ReturnLoan");
		btnReturnLoan.setIcon(Builder.getIcon("checkin.png", 22));
		btnReturnLoan.setToolTipText("Mark loan as returned");
		btnReturnLoan.setFocusable(false);
		btnReturnLoan.addActionListener(this);
		toolBar.add(btnReturnLoan);
		
		btnEditLoan = new JButton("");
		btnEditLoan.setActionCommand("EditLoan");
		btnEditLoan.setIcon(Builder.getIcon("edit.png", 22));
		btnEditLoan.setToolTipText("Edit the details of this loan");
		btnEditLoan.setFocusable(false);
		btnEditLoan.addActionListener(this);
		toolBar.add(btnEditLoan);
		
		btnSave = new JButton("");
		btnSave.setActionCommand("Save");
		btnSave.setIcon(Builder.getIcon("filesave.png", 22));
		btnSave.setToolTipText("Save changes");
		btnSave.setFocusable(false);
		btnSave.addActionListener(this);
		toolBar.add(btnSave);
		
	}
	
	private void showCurationHistoryForSelectedSample()
	{
		CurationDialog dialog = new CurationDialog(lstSamples.getSelectedValue(), this);
		dialog.setVisible(true);
	}
	
	private void setSamples()
	{

		if(loan==null) 
		{
			sampleModel.replaceContents(new ArrayList<TridasSample>());
			return;
		}
		
		
		if(!loan.isSetIdentifier())
		{
			// No identifier so this must be a fresh local loan
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
		
		
		sampleModel.replaceContents(sampList);	
		sampleModel.setSelectedItem(null);
		sortAvailableSamples();
		
	}
	
    private void sortAvailableSamples(){
		// Sort list intelligently
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		
		Collections.sort(sampleModel, numSorter);
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
		setEditable(false);
		
		this.loan= loan; 
		
		if(loan==null)
		{
			txtFirstName.setText("");
			txtLastName.setText("");
			txtOrganisation.setText("");
			txtNotes.setText("");
			txtDueDate.setText("");
			txtIssueDate.setText("");
			txtReturned.setText("");
			lblperiod.setText("");
			lblOverdue.setText("");
			panelFiles.setFileList(new ArrayList<TridasFile>());
			setSamples();
			
			return;
		}
		

		
		String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
		String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
		TellervoLocale loc = I18n.getTellervoLocale(country, language);
		
		DateFormat dateFormat =  getDateInstance(LONG, loc.getLocale());
		
		txtFirstName.setText(loan.getFirstname());
		txtLastName.setText(loan.getLastname());
		txtOrganisation.setText(loan.getOrganisation());
		
		txtNotes.setText(loan.getNotes());
		if(loan.isSetDuedate())
		{
			txtDueDate.setText(dateFormat.format(loan.getDuedate().toGregorianCalendar().getTime()));
		}
		else
		{
			txtDueDate.setText("");
		}
		if(loan.isSetIssuedate())
		{
			txtIssueDate.setText(dateFormat.format(loan.getIssuedate().toGregorianCalendar().getTime()));
		}
		else if(!loan.isSetIdentifier())
		{
			// Must be a new local loan
			//txtIssueDate.setText(dateFormat.format(LocalDate.now()));
		}
		else 
		{
			txtIssueDate.setText("");
		}
		if(loan.isSetReturndate())
		{
			txtReturned.setText(dateFormat.format(loan.getReturndate().toGregorianCalendar().getTime()));
		}
		else
		{
			txtReturned.setText("");
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


	public Boolean hasUnsavedEdits()
	{
		return true;
	}
	
	public Boolean saveChanges()
	{
		if(!modified) return true;
		
		if(lstSamples.getModel().getSize()==0)
		{
			Alert.error("No samples", "No samples have been included in this loan.");
			return false;
		}
		
		
		loan.setFirstname(txtFirstName.getText());
		loan.setLastname(txtLastName.getText());
		loan.setOrganisation(txtOrganisation.getText());
		loan.setNotes(txtNotes.getText());
		
		
		
		// Create resource
		EntityResource<WSILoan> resource;
		
		Boolean isNewRecord = !loan.isSetIdentifier();
		if(isNewRecord)
		{
			resource = new EntityResource<WSILoan>(loan, TellervoRequestType.CREATE, WSILoan.class);
		}
		else
		{
			resource = new EntityResource<WSILoan>(loan, TellervoRequestType.UPDATE, WSILoan.class);
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
		
		/*if(isNewRecord)
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
		}*/
		
		
		setLoan(loan);
		modified = false;

		return true;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		if(event.getActionCommand().equals("ViewSample"))
		{
			showCurationHistoryForSelectedSample();

		}
		else if (event.getActionCommand().equals("NewLoan"))
		{
			setLoan(new WSILoan());
			modified = true;
			setEditable(true);
		}
		else if (event.getActionCommand().equals("Save"))
		{
			saveChanges();
		}
		else if (event.getActionCommand().equals("AddSample"))
		{
			ITridas returned = TridasEntityPickerDialog.pickEntity(null, "Pick Sample", TridasSample.class, EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT);
			
			if(returned instanceof TridasSample)
			{
				sampleModel.add((TridasSample) returned);
			}

			
		}
		else if (event.getActionCommand().equals("RemoveSample"))
		{
			if(lstSamples.getSelectedIndex()>-1)
			{
				sampleModel.remove(lstSamples.getSelectedIndex());
			}
		}
		
		
	}
}
