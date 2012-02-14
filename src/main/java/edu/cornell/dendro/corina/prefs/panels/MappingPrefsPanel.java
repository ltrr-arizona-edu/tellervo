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
package edu.cornell.dendro.corina.prefs.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gis.GrfxWarning;
import edu.cornell.dendro.corina.gis.WMSTableModel;
import edu.cornell.dendro.corina.prefs.AddWMSServerDialog;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;
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
	@SuppressWarnings("unused")
	private ArrayList<WSIWmsServer> serverDetails = new ArrayList<WSIWmsServer>();
	
	/**
	 * Create the panel.
	 */
	public 
	MappingPrefsPanel(final JDialog parent) {
				
		super(I18n.getText("preferences.mapping"), 
				"map.png", 
				"Manage the Web Mapping Services available within Corina",
				parent);
		
		
		serverDetails = Dictionary.getMutableDictionary("wmsServerDictionary");
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		panelWMS = new JPanel();
		panelWMS.setBorder(new TitledBorder(null, "Web Map Services (WMS)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelWMS);
		panelWMS.setLayout(new MigLayout("", "[181.00px,grow,fill][325.00,grow][176.00,right][fill]", "[25.00,top][82.00,grow,fill]"));
		
		lblMappingRequiresWeb = new JLabel("Mapping requires a web service connection");
		lblMappingRequiresWeb.setForeground(Color.RED);
		panelWMS.add(lblMappingRequiresWeb, "cell 0 0 2 1");
		
		btnRemove = new JButton("Remove");
		
		btnRemove.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				WSIWmsServer server = wmsModel.getServerFromRow(tblWMS.getSelectedRow());
				
				if(server==null) return;
				
				App.prefs.removeWSIWmsServerFromArray(PrefKey.WMS_PERSONAL_SERVERS, server);
				populateWMSTable();
				
				
			}
			
		});
		
		panelWMS.add(btnRemove, "cell 2 0,alignx right");
		
		btnAdd = new JButton("Add");
		panelWMS.add(btnAdd, "cell 3 0,alignx right,aligny top");
		btnAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				WSIWmsServer server = AddWMSServerDialog.showAddWMSServerDialog(parent);
				
				if(server!=null)
				{
					App.prefs.addWSIWmsServerToArrayPref(PrefKey.WMS_PERSONAL_SERVERS, server);
					populateWMSTable();
				}
				
				
			}
			
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelWMS.add(scrollPane, "cell 0 1 4 1,grow");
		
		tblWMS = new JTable();
		tblWMS.setShowHorizontalLines(false);
		scrollPane.setViewportView(tblWMS);
		
		
		panelWarning = new GrfxWarning();
		add(panelWarning);

		// Enable/Disable mapping
		setMappingEnabled(!App.prefs.getBooleanPref(PrefKey.OPENGL_FAILED, false));
		populateWMSTable();
		
		tblWMS.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
					btnRemove.setEnabled(wmsModel.isRowEditable(tblWMS.getSelectedRow()));
			}
			
		});

	}

	private void populateWMSTable() {
		ArrayList<WSIWmsServer> personalServers = App.prefs.getWSIWmsServerArrayPref(PrefKey.WMS_PERSONAL_SERVERS);
		wmsModel = new WMSTableModel();
		wmsModel.setSystemServers();
		wmsModel.setPersonalServers(personalServers);
				
		tblWMS.setModel(wmsModel);

	}

	
	public void setMappingEnabled(Boolean b) {

		panelWMS.setVisible(b);
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
		
		if(panelWMS.isVisible())
		{
			populateWMSTable();
			// Show message and buttons depending on WS status
			Boolean wsDisabled = App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false);
			lblMappingRequiresWeb.setVisible(wsDisabled);
			btnAdd.setEnabled(!wsDisabled);
			btnRemove.setEnabled(!wsDisabled);
			tblWMS.setEnabled(!wsDisabled);
			
		}
		
		

	}

	@Override
	public void refresh() {
		setMappingEnabled(true);		
	}
	
}
