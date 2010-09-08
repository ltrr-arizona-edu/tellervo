package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    
    HashMap<String, String> serverDetails = new HashMap<String, String>();
    
    
    public WMSManager(WorldWindowGLCanvas wwd)
    {
    	this.wwd = wwd;
    	setupGUI();
    }
    
    public void setupGUI()
    {
    	
    	serverDetails.put("NASA", "http://neowms.sci.gsfc.nasa.gov/wms/wms");
    	
    	
    	
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

                String server = JOptionPane.showInputDialog("Enter wms server URL");
                if (server == null || server.length() < 1)
                {
                    tabbedPane.setSelectedIndex(previousTabIndex);
                    return;
                }

                // Respond by adding a new WMSLayerPanel to the tabbed pane.
                if (addTab(tabbedPane.getTabCount(), server.trim(), "New tab") != null)
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        });

        // Create a tab for each server and add it to the tabbed panel.
        Set set = serverDetails.entrySet();
        Iterator it = set.iterator();
        int i = 0;
        while(it.hasNext())
        {
        	Map.Entry me = (Map.Entry)it.next(); 
        	i++;
        	this.addTab(i, (String)me.getKey(), (String)me.getValue());
        	
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
    }

    private WMSLayersPanel addTab(int position, String serverName, String serverURI)
    {
        // Add a server to the tabbed dialog.
        try
        {
            WMSLayersPanel layersPanel = new WMSLayersPanel(wwd, serverURI, serverName, wmsPanelSize);
            this.tabbedPane.add(layersPanel, BorderLayout.CENTER);
            String title = layersPanel.getServerDisplayString();
            this.tabbedPane.setTitleAt(position, title != null && title.length() > 0 ? title : serverName);

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
