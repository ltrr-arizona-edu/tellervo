package edu.cornell.dendro.corina.prefs.panels;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gis.GrfxWarning;
import edu.cornell.dendro.corina.gis.WMSTableModel;
import edu.cornell.dendro.corina.schema.WSIWmsServer;
import edu.cornell.dendro.corina.ui.I18n;

@SuppressWarnings("serial")
public class MappingPrefsPanel extends AbstractPreferencesPanel {
	private GrfxWarning panelWarning;
	private JPanel panelWMS;
	private JButton btnRemove;
	private JButton btnAdd;
	private WMSTableModel wmsModel = new WMSTableModel();
	private JScrollPane scrollPane;
	private JTable tblWMS;
	private JLabel lblMappingRequiresWeb;
	
	/**
	 * Create the panel.
	 */
	public MappingPrefsPanel() {
		
		super(I18n.getText("preferences.mapping"), 
				"map.png", 
				"Manage the Web Mapping Services available within Corina");
		
		setLayout(new MigLayout("", "[10px,grow]", "[grow,fill][100px,grow][grow]"));
		
		
		panelWarning = new GrfxWarning();
		add(panelWarning, "cell 0 0,grow");
		
		panelWMS = new JPanel();
		panelWMS.setBorder(new TitledBorder(null, "Web Map Services (WMS)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelWMS, "cell 0 1,growx,aligny top");
		panelWMS.setLayout(new MigLayout("", "[181.00px,grow,fill][176.00,grow][fill]", "[][][87px][][]"));
		
		lblMappingRequiresWeb = new JLabel("Mapping requires a web service connection");
		panelWMS.add(lblMappingRequiresWeb, "cell 0 1 3 1");
		
		btnRemove = new JButton("Remove");
		panelWMS.add(btnRemove, "cell 1 2,alignx right");
		
		btnAdd = new JButton("Add");
		panelWMS.add(btnAdd, "cell 2 2,alignx right,aligny center");
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelWMS.add(scrollPane, "cell 0 3 3 1,grow");
		
		tblWMS = new JTable();
		tblWMS.setShowHorizontalLines(false);
		scrollPane.setViewportView(tblWMS);

		// Enable/Disable mapping
		setMappingEnabled(!App.prefs.getBooleanPref("opengl.failed", false));
		populateWMSTable();

	}

	@SuppressWarnings("unchecked")
	private void populateWMSTable() {
		ArrayList<WSIWmsServer> serverDetails = Dictionary
				.getMutableDictionary("wmsServerDictionary");
		wmsModel = new WMSTableModel();
		
		if (serverDetails == null || serverDetails.size() == 0) {
			setMappingEnabled(false);	
			return;
		}
		else
		{
			setMappingEnabled(true);			
		}
		
		for(WSIWmsServer server : serverDetails)
		{
			wmsModel.addServer(server);
		}
		
		tblWMS.setModel(wmsModel);

	}

	
	public void setMappingEnabled(Boolean b) {

		panelWMS.setVisible(b);
		panelWMS.setEnabled(false);
		panelWarning.setVisible(!b);
		
		if (!b) {
			panelWarning.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					if (evt.getActionCommand().equals("fail")) {
						setMappingEnabled(false);
					} else if (evt.getActionCommand().equals("pass")) {
						setMappingEnabled(true);
					}
				}
			});
		}

	}
	
}
