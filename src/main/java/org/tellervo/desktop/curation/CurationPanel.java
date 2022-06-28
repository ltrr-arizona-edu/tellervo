package org.tellervo.desktop.curation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.labels.ui.SampleLabelPrintingUI;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSICurationEvent;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import antlr.collections.List;

public class CurationPanel extends JPanel {
	private final static Logger log = LoggerFactory.getLogger(CurationPanel.class);

	private static final long serialVersionUID = 1L;
	private JTable tblCurationHistory;
	private JTextField txtBox;
	private JTextField txtStorageLocation;
	private CurationTableModel curationTableModel;
	
	private TridasSample sample;
	private WSIBox box;
	private JScrollPane scrollPane;
	private JTextField txtCurationStatus;
	private JPanel historyPanel;
	private JButton btnAssignToBox;
	private JButton btnUpdate;
	private Boolean wasChanged = false;
	
	
	/**
	 * Create the panel.
	 */
	public CurationPanel(TridasSample sample) {
		
		setupGUI();
		setSample(sample);


		
	}
	
	/**
	 * Set GUI components to reflect current sample and box info
	 */
	private void populate() {

		if(sample!=null)
		{
			if(TridasUtils.getGenericFieldByName(sample, "tellervo.curationStatus")!=null)
			{
				txtCurationStatus.setText(TridasUtils.getGenericFieldByName(sample, "tellervo.curationStatus").getValue());
			}		
		}

		if(box==null)
		{
			txtBox.setText("");
			txtStorageLocation.setText("");
		}
		else
		{		
			txtBox.setText(box.getTitle());
			txtStorageLocation.setText(box.getCurationLocation());		
		}
	}

	public String getCurrentCurationStatus()
	{
		return txtCurationStatus.getText();
	}
	
	public void setSample(TridasSample sample)
	{
		SearchParameters param2 = new SearchParameters(SearchReturnObject.SAMPLE);
		param2.addSearchConstraint(SearchParameterName.SAMPLEDBID, SearchOperator.EQUALS, sample.getIdentifier().getValue());		
		
		EntitySearchResource<TridasObject> resource2 = new EntitySearchResource<TridasObject>(param2, TridasObject.class);
		resource2.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);

		TellervoResourceAccessDialog dialog2 = new TellervoResourceAccessDialog(resource2);
		resource2.query();	
		dialog2.setVisible(true);
		
		if(!dialog2.isSuccessful()) 
		{ 
			log.error("Error getting sample information");
			return;
		}
		
		
		java.util.List<TridasSample> sampList = SampleLabelPrintingUI.getSamplesList(resource2.getAssociatedResult(), null, null);
		
		this.sample = sampList.get(0);
		
    	TridasGenericField f = GenericFieldUtils.findField(this.sample, "tellervo.internal.labcodeText");
    	
    	String title = (f != null) ? f.getValue() : this.sample.getTitle();
		
		
		historyPanel.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191)), "Curation details for "+title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		
		SearchParameters param = new SearchParameters(SearchReturnObject.BOX);
    	param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, sample.getIdentifier().getValue());
		EntitySearchResource<WSIBox> resource = new EntitySearchResource<WSIBox>(param, WSIBox.class);
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting loans");
			return;
		}
		
		java.util.List<WSIBox> objList = resource.getAssociatedResult();
		
		if(objList.size()==1)
		{
			box = objList.get(0);
		}
		else
		{
			box = null;
		}
		
		
		SearchParameters param3 = new SearchParameters(SearchReturnObject.CURATION_EVENT);
    	param3.addSearchConstraint(SearchParameterName.SAMPLEDBID, SearchOperator.EQUALS, sample.getIdentifier().getValue());
		EntitySearchResource<WSICurationEvent> resource3 = new EntitySearchResource<WSICurationEvent>(param3, WSICurationEvent.class);
		TellervoResourceAccessDialog dialog3 = new TellervoResourceAccessDialog(resource3);
		resource3.query();	
		dialog3.setVisible(true);
		
		if(!dialog3.isSuccessful()) 
		{ 
			log.error("Error getting loans");
			return;
		}
		
		java.util.List<WSICurationEvent> curationHistory = resource3.getAssociatedResult();
		curationTableModel = new CurationTableModel();
		tblCurationHistory.setModel(curationTableModel);
		for(WSICurationEvent curation : curationHistory)
		{
			curationTableModel.addCurationEvent(curation);
		}
		
		RowSorter sorter = tblCurationHistory.getRowSorter();
		ArrayList sortKeys = new ArrayList();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);		
		
		tblCurationHistory.repaint();
		
		populate();
		
		
	}
	
	
	
	
	private void setupGUI()
	{
		setLayout(new MigLayout("", "[54.00,grow,right][grow]", "[grow][74.00]"));
		JPanel boxPanel = new JPanel();
		boxPanel.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191)), "Sample stored in box", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(boxPanel, "cell 0 1 2 1,grow");
		boxPanel.setLayout(new MigLayout("", "[135px:135px,right][grow,fill]", "[][][]"));
		
		JLabel lblBox = new JLabel("Name");
		boxPanel.add(lblBox, "cell 0 0");
		
		txtBox = new JTextField();
		txtBox.setFocusable(false);
		txtBox.setEditable(false);
		
		boxPanel.add(txtBox, "cell 1 0");
		txtBox.setColumns(10);
		
		JLabel lblStorageLocation = new JLabel("Storage location:");
		boxPanel.add(lblStorageLocation, "cell 0 1");
		
		txtStorageLocation = new JTextField();
		txtStorageLocation.setFocusable(false);
		txtStorageLocation.setEditable(false);
		boxPanel.add(txtStorageLocation, "flowy,cell 1 1");
		txtStorageLocation.setColumns(10);	
		
		btnAssignToBox = new JButton("Assign sample to a box");
		btnAssignToBox.setEnabled(false);
		boxPanel.add(btnAssignToBox, "cell 1 2");
		historyPanel = new JPanel();
		historyPanel.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191)), "Curation details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(historyPanel, "cell 0 0 2 1,grow");
		historyPanel.setLayout(new MigLayout("", "[135px:135px,right][grow,fill]", "[][100px:150px,grow]"));
		
		JLabel lblCurationStatus = new JLabel("Current status:");
		historyPanel.add(lblCurationStatus, "cell 0 0,alignx trailing");
		
		txtCurationStatus = new JTextField();
		txtCurationStatus.setEditable(false);
		txtCurationStatus.setFocusable(false);
		historyPanel.add(txtCurationStatus, "flowx,cell 1 0,growx");
		
		JLabel lblCurationHistory = new JLabel("History:");
		historyPanel.add(lblCurationHistory, "cell 0 1");
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		historyPanel.add(scrollPane, "cell 1 1");
		
		tblCurationHistory = new JTable();
		tblCurationHistory.setAutoCreateRowSorter(true);
		curationTableModel = new CurationTableModel();			
		
		tblCurationHistory.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent evt) {
				if(evt.getClickCount()>1)
				{
					WSICurationEvent curationEvent = ((CurationTableModel)tblCurationHistory.getModel()).getRowAsWSICurationEvent(tblCurationHistory.getSelectedRow());
					log.debug("Curation event loan"+curationEvent.getLoan());
					if(curationEvent!=null && curationEvent.isSetLoan())
					{
						LoanDialog dialog = new LoanDialog(null);
						dialog.setLoan(curationEvent.getLoan());
						dialog.setVisible(true);
						dialog.expandDetailsPanel(0.0d);
						
					}
				}
				else
				{
					WSICurationEvent curationEvent = ((CurationTableModel)tblCurationHistory.getModel()).getRowAsWSICurationEvent(tblCurationHistory.getSelectedRow());
					log.debug("Curation event loan: "+curationEvent.getLoan());

				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {	}

			@Override
			public void mouseExited(MouseEvent arg0) { }

			@Override
			public void mousePressed(MouseEvent arg0) {	}

			@Override
			public void mouseReleased(MouseEvent arg0) { }
			
		});

		scrollPane.setViewportView(tblCurationHistory);
		tblCurationHistory.setModel(curationTableModel);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				CurationEventDialog dialog = new CurationEventDialog(historyPanel, sample);				
				dialog.setVisible(true);
				
				if(dialog.wasChanged())
				{
					wasChanged=true;
					setSample(sample);
				}
				
			}
		});
		historyPanel.add(btnUpdate, "cell 1 0");
		
	}
	
	public boolean wasChanged()
	{
		return wasChanged;
	}
	
}
