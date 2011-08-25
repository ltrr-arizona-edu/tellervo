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
package edu.cornell.dendro.corina.gis;

import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.ui.Builder;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class GISFrame extends JFrame {

	private static final long serialVersionUID = -451333846688316647L;
	protected GISPanel wwMapPanel;
	protected JSplitPane splitPane;
	protected CorinaLayerPanel layerPanel;
	private final Boolean isMiniMap;
	
	/**
	 * Simple constructor with all sites shown on map
	 * @wbp.parser.constructor
	 */
	public GISFrame(Boolean isMiniMap)
	{
		this.isMiniMap = isMiniMap;
		
		setupGui(TridasMarkerLayerBuilder.getMarkerLayerForAllSites());
		
		//setupGui(KMLMarkerLayerBuilder.createLayerFromKMZ("/tmp/tree-ring-records.kmz"));
		//setupGui(ITRDBMarkerLayerBuilder.createITRDBLayer());
		setupMenus();
	}
	
	/**
	 * Simple constructor which shows given layer on map
	 * 
	 * @param layer
	 */
	public GISFrame(MarkerLayer layer, Boolean isMiniMap)
	{
		this.isMiniMap = isMiniMap;
	
		setupGui(layer);
		setupMenus();
	}
	
	
	private void setupGui(MarkerLayer layer)
	{
		wwMapPanel = new GISPanel(new Dimension(300,300),true, layer);
		//managerPanel = new GISManagerPanel(wwMapPanel);
		
		wwMapPanel.setIsMiniMap(isMiniMap);
		
		if(!wwMapPanel.isMiniMap())
		{
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            layerPanel = new CorinaLayerPanel(wwMapPanel.getWwd());
            
            JButton addLayer = new JButton("Add layer");
            final GISFrame glue = this;
            
            addLayer.addActionListener(new ActionListener(){

            
				@Override
				public void actionPerformed(ActionEvent arg0) {
					AddGISDataDialog dialog = new AddGISDataDialog(glue);
					dialog.setVisible(true);
					
				}
            	
            });
            container.add(layerPanel);
            container.add(addLayer);
            
            
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					container, wwMapPanel);
			getContentPane().add(splitPane, BorderLayout.CENTER);
		}
		else
		{
			getContentPane().add(wwMapPanel, BorderLayout.CENTER);
		}

		pack();

		int state = getExtendedState(); 
		// Set the maximized bits 
		state |= JFrame.MAXIMIZED_BOTH; 
		
		// Maximize the frame 
		setExtendedState(state); 
		setVisible(true);
		setTitle("Site map");
		setIconImage(Builder.getApplicationIcon());
 
        // Add the Gazetteer
        try {
        	if(wwMapPanel.getWwd()!=null)
        	{      	
	        	CorinaGazetteerPanel gazPanel = new CorinaGazetteerPanel(wwMapPanel.getWwd(), null);
	        	
				getContentPane().add(gazPanel,   //use default yahoo service
				        BorderLayout.SOUTH);
        	}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		

		
		
		
	}
	
	private void setupMenus()
	{

        JMenuBar menuBar = new JMenuBar();
        
		menuBar.add(new GISFileMenu(this));

		menuBar.add(new GISEditMenu(this));
		
		menuBar.add(new GISViewMenu(wwMapPanel.getWwd()));
	
		if (Platform.isMac())
			menuBar.add(new WindowMenu(this));
		menuBar.add(new HelpMenu());
        
		
		
        this.setJMenuBar(menuBar);

	}
	
    /**
     * Adds the specified <code>kmlRoot</code> to this app frame's <code>WorldWindow</code> as a new
     * <code>Layer</code>, and adds a new <code>KMLLayerTreeNode</code> for the <code>kmlRoot</code> to this app
     * frame's on-screen layer tree.
     * <p/>
     * This expects the <code>kmlRoot</code>'s <code>AVKey.DISPLAY_NAME</code> field to contain a display name
     * suitable for use as a layer name.
     *
     * @param kmlRoot the KMLRoot to add a new layer for.
     */
    protected void addKMLLayer(KMLRoot kmlRoot)
    {
        // Create a KMLController to adapt the KMLRoot to the World Wind renderable interface.
        KMLController kmlController = new KMLController(kmlRoot);

        // Adds a new layer containing the KMLRoot to the end of the WorldWindow's layer list. This
        // retrieves the layer name from the KMLRoot's DISPLAY_NAME field.
        RenderableLayer layer = new RenderableLayer();
        layer.setName((String) kmlRoot.getField(AVKey.DISPLAY_NAME));
        layer.addRenderable(kmlController);
        wwMapPanel.getWwd().getModel().getLayers().add(layer);

        // Adds a new layer tree node for the KMLRoot to the on-screen layer tree, and makes the new node visible
        // in the tree. This also expands any tree paths that represent open KML containers or open KML network
        // links.
        /*KMLLayerTreeNode layerNode = new KMLLayerTreeNode(layer, kmlRoot);
        this.layerTree.getModel().addLayer(layerNode);
        this.layerTree.makeVisible(layerNode.getPath());
        layerNode.expandOpenContainers(this.layerTree);*/

        // Listens to refresh property change events from KML network link nodes. Upon receiving such an event this
        // expands any tree paths that represent open KML containers. When a KML network link refreshes, its tree
        // node replaces its children with new nodes created form the refreshed content, then sends a refresh
        // property change event through the layer tree. By expanding open containers after a network link refresh,
        // we ensure that the network link tree view appearance is consistent with the KML specification.
        /*layerNode.addPropertyChangeListener(AVKey.RETRIEVAL_STATE_SUCCESSFUL, new PropertyChangeListener()
        {
            public void propertyChange(final PropertyChangeEvent event)
            {
                if (event.getSource() instanceof KMLNetworkLinkTreeNode)
                {
                    // Manipulate the tree on the EDT.
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            ((KMLNetworkLinkTreeNode) event.getSource()).expandOpenContainers(layerTree);
                            getWwd().redraw();
                        }
                    });
                }
            }
        });*/
    }
}
