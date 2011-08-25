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
import gov.nasa.worldwind.layers.MarkerLayer;

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
	

}
