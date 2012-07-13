package org.tellervo.desktop.gui.dbbrowse;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;

public class StaticSearchPanel extends JPanel implements ResourceEventListener, ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField txtObjectCode;
	private JTextField txtElementCode;
	private JTextField txtSampleCode;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
	private JCheckBox chkElementCode;
	private JCheckBox chkObjectCode;
	private JCheckBox chkSampleCode;
	
	private JComboBox opObjectCode;
	private JComboBox opElementCode;
	private JComboBox opSampleCode;
	
	private JSpinner spinLimit;
	private JSpinner spinSkip;
	
	private JButton btnSearch;
	private JButton btnClear;
	
	/** The thing that cares about our results */
	private final SearchResultManager manager;
	
	/** The search in progress, or null if no search is in progress */
	private SeriesSearchResource searchResource;
	private JCheckBox chkCheckAllNone;

	
	/**
	 * Create the panel.
	 */
	public StaticSearchPanel(SearchResultManager manager) {
		
		this.manager = manager;
		initGui();

	}
	
	
	private SearchParameters getSearchParamsFromGui()
	{
		SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
		
		// Set paging parameters
		search.setLimit((Integer) spinLimit.getValue());
		search.setSkip((Integer) spinSkip.getValue());
		
		if(this.chkObjectCode.isSelected())
		{
			search.addSearchConstraint(
					SearchParameterName.ANYPARENTOBJECTCODE, 
					(SearchOperator) opObjectCode.getSelectedItem(), 
					txtObjectCode.getText());
		}
		if(this.chkElementCode.isSelected())
		{
			search.addSearchConstraint(
					SearchParameterName.ELEMENTCODE, 
					(SearchOperator) opElementCode.getSelectedItem(), 
					txtElementCode.getText());
		}
		if(this.chkSampleCode.isSelected())
		{
			search.addSearchConstraint(
					SearchParameterName.SAMPLECODE, 
					(SearchOperator) opSampleCode.getSelectedItem(), 
					txtSampleCode.getText());
		}
		
		
		
		return search;
	}
	
	/**
	 * Start a search on the given parameters
	 * If one already exists, it is cancelled (unless it's the same search)
	 * @param params
	 */
	private void startSearch(SearchParameters params) {
		// we already have a search resource?
		if(searchResource != null) {
			// the parameters are the same? don't search again
			if(searchResource.getSearchParameters().equals(params))
				return;
			
			// abort the current query, being careful to not do this if we're in the process of getting results
			synchronized(this) {
				searchResource.removeResourceEventListener(this);
				searchResource.abortQuery();
				searchResource = null;
			}
		}

		// notify we have a new search
		manager.notifySearchStarting();
				
		// lock again, to make sure (not necessary?) 
		synchronized(this) {
			searchResource = new SeriesSearchResource(params);
		}
		
		// start the query...
		searchResource.addResourceEventListener(this);
		
		searchResource.query();
	}

	public void resetGui()
	{
		cancelSearch();
	}
	
	public void cancelSearch() {
		// abort the current query, being careful to not do this if we're in the process of getting results
		synchronized(this) {
			if(searchResource == null)
				return;
			
			searchResource.removeResourceEventListener(this);
			searchResource.abortQuery();
			searchResource = null;
		}		
	}
	
	public void initGui()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelFooter = new JPanel();
		panelFooter.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		add(panelFooter, BorderLayout.SOUTH);
		panelFooter.setLayout(new MigLayout("", "[][][][][grow]", "[][grow]"));
		
		JLabel lblLimit = new JLabel("Limit response to ");
		panelFooter.add(lblLimit, "cell 0 0");
		
		spinLimit = new JSpinner();
		spinLimit.setModel(new SpinnerNumberModel(100, 1, 200, 1));
		panelFooter.add(spinLimit, "cell 1 0");
		
		JLabel lblAndSkipThe = new JLabel("records, and skip the first ");
		panelFooter.add(lblAndSkipThe, "cell 2 0");
		
		spinSkip = new JSpinner();
		spinSkip.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		panelFooter.add(spinSkip, "cell 3 0");
		
		JPanel panelButtons = new JPanel();
		FlowLayout fl_panelButtons = (FlowLayout) panelButtons.getLayout();
		fl_panelButtons.setAlignment(FlowLayout.RIGHT);
		panelFooter.add(panelButtons, "cell 0 1 5 1,grow");
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				startSearch(getSearchParamsFromGui());
			}
		});
		panelButtons.add(btnSearch);
		
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				resetGui();
			}
		});
		panelButtons.add(btnClear);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new MigLayout("", "[][][grow]", "[][][][][][][]"));
		
		chkObjectCode = new JCheckBox("Object code:");
		chkObjectCode.addActionListener(this);

		panel.add(chkObjectCode, "cell 0 0");
		
		opObjectCode = new JComboBox();
		opObjectCode.setModel(new DefaultComboBoxModel(new SearchOperator[] {SearchOperator.EQUALS, SearchOperator.NOT_EQUALS, SearchOperator.LIKE}));
		panel.add(opObjectCode, "cell 1 0,growx");
		
		txtObjectCode = new JTextField();
		panel.add(txtObjectCode, "cell 2 0,growx");
		txtObjectCode.setColumns(10);
		
		chkElementCode = new JCheckBox("Element code:");
		panel.add(chkElementCode, "cell 0 1");
		
		opElementCode = new JComboBox();
		opElementCode.setModel(new DefaultComboBoxModel(new SearchOperator[] {SearchOperator.EQUALS, SearchOperator.NOT_EQUALS, SearchOperator.LIKE}));
		panel.add(opElementCode, "cell 1 1,growx");
		
		txtElementCode = new JTextField();
		txtElementCode.setColumns(10);
		panel.add(txtElementCode, "cell 2 1,growx");
		
		chkSampleCode = new JCheckBox("Sample code:");
		panel.add(chkSampleCode, "cell 0 2");
		
		opSampleCode = new JComboBox();
		opSampleCode.setModel(new DefaultComboBoxModel(new SearchOperator[] {SearchOperator.EQUALS, SearchOperator.NOT_EQUALS, SearchOperator.LIKE}));
		panel.add(opSampleCode, "cell 1 2,growx");
		
		txtSampleCode = new JTextField();
		panel.add(txtSampleCode, "cell 2 2,growx");
		txtSampleCode.setColumns(10);
		
		JCheckBox chckbxSpecies = new JCheckBox("Species:");
		panel.add(chckbxSpecies, "cell 0 3");
		
		JComboBox comboBox_1 = new JComboBox();
		panel.add(comboBox_1, "cell 1 3,growx");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		panel.add(textField_1, "cell 2 3,growx");
		
		JCheckBox chckbxStartDate = new JCheckBox("Start date:");
		panel.add(chckbxStartDate, "cell 0 4");
		
		JComboBox comboBox_2 = new JComboBox();
		panel.add(comboBox_2, "cell 1 4,growx");
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		panel.add(textField_2, "cell 2 4,growx");
		
		JCheckBox chckbxEndDate = new JCheckBox("End date:");
		panel.add(chckbxEndDate, "cell 0 5");
		
		JComboBox comboBox_3 = new JComboBox();
		panel.add(comboBox_3, "cell 1 5,growx");
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		panel.add(textField_3, "cell 2 5,growx");
		
		JCheckBox chckbxRingCount = new JCheckBox("Ring count:");
		panel.add(chckbxRingCount, "cell 0 6");
		
		JComboBox comboBox_4 = new JComboBox();
		panel.add(comboBox_4, "cell 1 6,growx");
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		panel.add(textField_4, "cell 2 6,growx");
		
		JPanel panelHeader = new JPanel();
		FlowLayout fl_panelHeader = (FlowLayout) panelHeader.getLayout();
		fl_panelHeader.setHgap(7);
		fl_panelHeader.setAlignment(FlowLayout.LEFT);
		add(panelHeader, BorderLayout.NORTH);
		
		chkCheckAllNone = new JCheckBox("Check all");
		
		chkCheckAllNone.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				setAllParametersSelected(chkCheckAllNone.isSelected());
				
			}
			
		});
		
		panelHeader.add(chkCheckAllNone);

	}
	
	public void setAllParametersSelected(Boolean b)
	{
		chkObjectCode.setSelected(b);
		chkElementCode.setSelected(b);
		chkSampleCode.setSelected(b);
	}

	

	@Override
	public void resourceChanged(final ResourceEvent re) {
		SeriesSearchResource resource = (SeriesSearchResource) re.getSource();
		
		// ignore if this info comes from a resource we don't care about
		synchronized(this) {
			if(resource != searchResource)
				return;
		}
		
		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
			// get rid of the search resource
			synchronized(this) {
				searchResource = null;
			}
			
			// run this in the UI thread
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					manager.notifySearchFinished(null);
					
					JOptionPane.showMessageDialog(StaticSearchPanel.this, "Search failed: " + 
							re.getAttachedException().getLocalizedMessage(), 
								"Search results", JOptionPane.ERROR_MESSAGE);					
				}
				
			});
			
			return;
		}
		
		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
			// get rid of the search resource
			synchronized(this) {
				searchResource = null;
			}
			
			final ElementList elements = resource.getAssociatedResult();
			
			// store the result (getAssociatedResult must return a value)
			//searchCacheMap.put(resource.getSearchParameters(), elements);
			
			// run this in the UI thread
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					manager.notifySearchFinished(elements);
				}				
			});		
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(chkObjectCode))
		{
			opObjectCode.setEnabled(chkObjectCode.isSelected());
			txtObjectCode.setEnabled(chkObjectCode.isSelected());
		}
		else if(e.getSource().equals(chkElementCode))
		{
			opElementCode.setEnabled(chkElementCode.isSelected());
			txtElementCode.setEnabled(chkElementCode.isSelected());
		}
		else if(e.getSource().equals(chkSampleCode))
		{
			opSampleCode.setEnabled(chkSampleCode.isSelected());
			txtSampleCode.setEnabled(chkSampleCode.isSelected());
		}
	}	


}
