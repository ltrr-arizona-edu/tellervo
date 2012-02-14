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
package org.tellervo.desktop.gis;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.prefs.AddWMSServerDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.schema.WSIWmsServer;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;

import com.dmurph.mvc.model.MVCArrayList;

@SuppressWarnings("serial")
public class WMSManager extends JFrame {
    private final Dimension wmsPanelSize = new Dimension(400, 600);
    protected WorldWindowGLCanvas wwd;
    private JTabbedPane tabbedPane;
    private int previousTabIndex;
    
    /*private static final String[] servers = new String[]
	{
    	   // "http://hypercube.telascience.org/cgi-bin/mapserv?map=/geo/haiti/mapfiles/4326.map&",
	       "http://neowms.sci.gsfc.nasa.gov/wms/wms",
	       // "http://mapserver.flightgear.org/cgi-bin/landcover",
	       // "http://wms.jpl.nasa.gov/wms.cgi",
	       // "http://nasa.network.com/wms"
	};*/
    
    ArrayList<WSIWmsServer> serverDetails = new ArrayList<WSIWmsServer>();
    
    
    public WMSManager(WorldWindowGLCanvas wwd)
    {
    	this.wwd = wwd;
    	setupGUI();
    }
    
    @SuppressWarnings("unchecked")
	public void setupGUI()
    {
    	ArrayList<WSIWmsServer> systemServers = Dictionary.getMutableDictionary("wmsServerDictionary");
    	ArrayList<WSIWmsServer> personalServers = App.prefs.getWSIWmsServerArrayPref(PrefKey.WMS_PERSONAL_SERVERS);
    	
    	serverDetails.addAll(systemServers);
    	serverDetails.addAll(personalServers);
    	

    	if(serverDetails==null || serverDetails.size()==0)
    	{
    		Alert.error("Error", "No WMS servers configured");
    		this.dispose();
    	}
    	
        this.tabbedPane = new JTabbedPane();

        this.tabbedPane.add(new JPanel());
        this.tabbedPane.setTitleAt(0, "+");
        this.tabbedPane.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent changeEvent)
            {
                if (tabbedPane.getSelectedIndex() != 0)
                {
                    previousTabIndex = tabbedPane.getSelectedIndex();
                    return;
                }

				WSIWmsServer server = AddWMSServerDialog.showAddWMSServerDialog(null);
				
				if(server!=null)
				{
					App.prefs.addWSIWmsServerToArrayPref(PrefKey.WMS_PERSONAL_SERVERS, server);
					App.refreshPreferencesDialog();
				}
				else
                {
                    tabbedPane.setSelectedIndex(previousTabIndex);
                    return;
                }

                if (addTab(tabbedPane.getTabCount(), server) != null)
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        });

        // Create a tab for each server and add it to the tabbed panel.
        int i = 1;
    	for(WSIWmsServer server : serverDetails)    	
    	{
    		this.addTab(i, server);
    		i++;
    	}


        // Display the first server pane by default.
        this.tabbedPane.setSelectedIndex(this.tabbedPane.getTabCount() > 0 ? 1 : 0);
        this.previousTabIndex = this.tabbedPane.getSelectedIndex();

        // Add the tabbed pane to a frame separate from the world window.
        
        this.getContentPane().add(tabbedPane);
        this.pack();
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setTitle("WMS Layer Manager");
        this.setIconImage(Builder.getApplicationIcon());
    }

    private WMSLayersPanel addTab(int position, WSIWmsServer server)
    {
        // Add a server to the tabbed dialog.
        try
        {
            WMSLayersPanel layersPanel = new WMSLayersPanel(wwd, server.getUrl(), server.getName(), wmsPanelSize);
            this.tabbedPane.add(layersPanel, BorderLayout.CENTER);
            String title = layersPanel.getServerDisplayString();
            this.tabbedPane.setTitleAt(position, title != null && title.length() > 0 ? title : server.getName());

            // Add a listener to notice wms layer selections and tell the layer panel to reflect the new state.
            layersPanel.addPropertyChangeListener("LayersPanelUpdated", new PropertyChangeListener()
            {
                public void propertyChange(PropertyChangeEvent propertyChangeEvent)
                {
                    //WMSLayersPanel.this.getLayerPanel().update(AppFrame.this.getWwd());
                }
            });

            return layersPanel;
        }
        catch (URISyntaxException e)
        {
            JOptionPane.showMessageDialog(null, "Server URL is invalid", "Invalid Server URL",
                JOptionPane.ERROR_MESSAGE);
            tabbedPane.setSelectedIndex(previousTabIndex);
            return null;
        }
    }
}
