package org.tellervo.desktop.admin.curation;

import static java.text.DateFormat.LONG;
import static java.text.DateFormat.getDateInstance;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import net.miginfocom.swing.MigLayout;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.TridasFileListPanel;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.ui.I18n.TellervoLocale;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.labels.ui.SampleLabelPrintingUI;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.WSILoan;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

public class LoanPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtOrganisation;
	private JTextField txtIssueDate;
	private JTextField txtDueDate;
	private JTextArea txtNotes;
	private JList lstSamples;
	private JLabel lblperiod;
	private JTextField txtReturned;
	private TridasFileListPanel panelFiles;
	private final static Logger log = LoggerFactory.getLogger(LoanPanel.class);
	private ArrayListModel<TridasSample> sampleModel;
	private JPanel panelSummary;
	private JButton btnAddSample;
	private JButton btnRemoveSample;
	private JButton btnViewSample;
	/**
	 * Create the panel.
	 */
	public LoanPanel() {
		
		initGUI();
	}

	public void setEditable(Boolean b)
	{
		txtFirstName.setEditable(b);
		txtLastName.setEditable(b);
		txtOrganisation.setEditable(b);
		txtIssueDate.setEditable(b);
		txtDueDate.setEditable(b);
		txtReturned.setEditable(b);
		txtNotes.setEditable(b);
		btnAddSample.setVisible(!b);
		btnRemoveSample.setVisible(!b);
		
		panelFiles.setReadOnly(!b);
	}
	
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
		panelSummary.add(txtFirstName, "cell 1 0");
		txtFirstName.setColumns(10);
		
		txtLastName = new JTextField();
		panelSummary.add(txtLastName, "cell 2 0");
		txtLastName.setColumns(10);
		
		JLabel lblOrganisation = new JLabel("Organisation:");
		panelSummary.add(lblOrganisation, "cell 0 1");
		
		txtOrganisation = new JTextField();
		panelSummary.add(txtOrganisation, "cell 1 1 2 1");
		txtOrganisation.setColumns(10);
		
		JLabel lblIssued = new JLabel("Date issued");
		panelSummary.add(lblIssued, "cell 0 2");
		
		txtIssueDate = new JTextField();
		panelSummary.add(txtIssueDate, "cell 1 2");
		txtIssueDate.setEditable(false);
		txtIssueDate.setColumns(10);
		
		JLabel lblDateDue = new JLabel("Date due:");
		panelSummary.add(lblDateDue, "cell 0 3,alignx trailing");
		
		txtDueDate = new JTextField();
		panelSummary.add(txtDueDate, "flowx,cell 1 3");
		txtDueDate.setColumns(10);
		
		lblperiod = new JLabel("");
		panelSummary.add(lblperiod, "cell 2 3");
		
		JLabel lblDateReturned = new JLabel("Date returned:");
		panelSummary.add(lblDateReturned, "cell 0 4,alignx trailing");
		
		txtReturned = new JTextField();
		txtReturned.setColumns(10);
		panelSummary.add(txtReturned, "cell 1 4,growx");
		
		JLabel lblNotes = new JLabel("Notes:");
		panelSummary.add(lblNotes, "cell 0 5,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		panelSummary.add(scrollPane, "cell 1 5 2 1,grow");
		
		txtNotes = new JTextArea();
		scrollPane.setViewportView(txtNotes);
		
		JPanel panelSamples = new JPanel();
		tabbedPane.addTab("Samples", null, panelSamples, null);
		panelSamples.setLayout(new MigLayout("", "[grow][fill]", "[][][grow][]"));
		
		JScrollPane scrollPaneSamples = new JScrollPane();
		panelSamples.add(scrollPaneSamples, "cell 0 0 1 4,grow");
		
		lstSamples = new JList<TridasSample>();
		sampleModel = new ArrayListModel<TridasSample>();
		lstSamples.setModel(sampleModel);
		lstSamples.setCellRenderer(new TridasListCellRenderer());
		
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
		btnAddSample.setIcon(Builder.getIcon("edit_add.png", 16));
		panelSamples.add(btnAddSample, "cell 1 0");
		
		btnRemoveSample = new JButton();
		btnRemoveSample.setIcon(Builder.getIcon("cancel.png", 16));
		panelSamples.add(btnRemoveSample, "cell 1 1");
		
		btnViewSample = new JButton();
		btnViewSample.setIcon(Builder.getIcon("document_preview.png", 16));
		btnViewSample.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				showCurationHistoryForSelectedSample();
			}
			
		});
		panelSamples.add(btnViewSample, "cell 1 3");
		
		panelFiles = new TridasFileListPanel();
		tabbedPane.addTab("Associated Files", null, panelFiles, null);
		
	}
	
	private void showCurationHistoryForSelectedSample()
	{
		CurationDialog dialog = new CurationDialog((TridasSample) lstSamples.getSelectedValue(), this);
		dialog.setVisible(true);
	}
	
	private void setSamples(WSILoan loan)
	{
		// Set return type to loan
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
	
	public void setLoan(WSILoan loan)
	{
		if(loan==null) return;
		
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
						
			lblperiod.setText("Loan period: "+PeriodFormat.getDefault().print(period));
			
			
		}
		
		
		
		panelFiles.setFileList((ArrayList<TridasFile>) loan.getFiles());
		
		setSamples(loan);
		
	}


}
