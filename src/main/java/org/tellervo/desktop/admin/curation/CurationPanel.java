package org.tellervo.desktop.admin.curation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSICuration;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasSample;

public class CurationPanel extends JPanel {
	private final static Logger log = LoggerFactory.getLogger(CurationPanel.class);

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField txtBox;
	private JTextField txtStorageLocation;
	private CurationTableModel curationTableModel;
	
	private TridasSample sample;
	private WSIBox box;
	private JScrollPane scrollPane;
	private JTextField txtCurationStatus;
	
	
	
	/**
	 * Create the panel.
	 */
	public CurationPanel(TridasSample sample) {
		
		setupGUI();
		setSample(sample);


		
	}
	
	private void populate() {

		txtBox.setText(box.getTitle());
		txtStorageLocation.setText(box.getCurationLocation());		
		if(TridasUtils.getGenericFieldByName(sample, "curationStatus")!=null)
		{
			txtCurationStatus.setText(TridasUtils.getGenericFieldByName(sample, "curationStatus").getValue());
		}
	}

	public void setSample(TridasSample sample)
	{
		this.sample = sample;
		
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
			Alert.error("Unexpected", "Expecting one box result from database.  Received "+objList.size());
			return;
		}
		
		
		SearchParameters param2 = new SearchParameters(SearchReturnObject.CURATION);
    	param2.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, sample.getIdentifier().getValue());
		EntitySearchResource<WSICuration> resource2 = new EntitySearchResource<WSICuration>(param2, WSICuration.class);
		TellervoResourceAccessDialog dialog2 = new TellervoResourceAccessDialog(resource2);
		resource2.query();	
		dialog2.setVisible(true);
		
		if(!dialog2.isSuccessful()) 
		{ 
			log.error("Error getting loans");
			return;
		}
		
		java.util.List<WSICuration> curationHistory = resource2.getAssociatedResult();
		
		for(WSICuration curation : curationHistory)
		{
			curationTableModel.addCurationEvent(curation);
		}
		
		table.repaint();
		
		populate();
		
		
	}
	
	
	
	
	private void setupGUI()
	{
		setLayout(new MigLayout("", "[54.00,grow,right][grow]", "[][74.00,grow]"));
		
		JPanel boxPanel = new JPanel();
		boxPanel.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191)), "Box details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(boxPanel, "cell 0 0 2 1,grow");
		boxPanel.setLayout(new MigLayout("", "[135px:135px,right][grow,fill]", "[][]"));
		
		JLabel lblBox = new JLabel("Name");
		boxPanel.add(lblBox, "cell 0 0");
		
		txtBox = new JTextField();
		txtBox.setEditable(false);
		boxPanel.add(txtBox, "cell 1 0");
		txtBox.setColumns(10);
		
		JLabel lblStorageLocation = new JLabel("Storage location:");
		boxPanel.add(lblStorageLocation, "cell 0 1");
		
		txtStorageLocation = new JTextField();
		txtStorageLocation.setEditable(false);
		boxPanel.add(txtStorageLocation, "cell 1 1");
		txtStorageLocation.setColumns(10);
		
		JPanel historyPanel = new JPanel();
		historyPanel.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191)), "Curation details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(historyPanel, "cell 0 1 2 1,grow");
		historyPanel.setLayout(new MigLayout("", "[135px:135px,right][grow,fill]", "[][100px:150px,grow]"));
		
		JLabel lblCurationStatus = new JLabel("Current status:");
		historyPanel.add(lblCurationStatus, "cell 0 0,alignx trailing");
		
		txtCurationStatus = new JTextField();
		txtCurationStatus.setEditable(false);
		historyPanel.add(txtCurationStatus, "cell 1 0,growx");
		txtCurationStatus.setColumns(10);
		
		JLabel lblCurationHistory = new JLabel("History:");
		historyPanel.add(lblCurationHistory, "cell 0 1");
		
		scrollPane = new JScrollPane();
		historyPanel.add(scrollPane, "cell 1 1");
		
		table = new JTable();
		curationTableModel = new CurationTableModel();
		
		 
		
		scrollPane.setViewportView(table);
		table.setModel(curationTableModel);
	}
	

}
