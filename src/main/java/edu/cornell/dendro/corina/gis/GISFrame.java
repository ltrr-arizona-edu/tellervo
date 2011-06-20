package edu.cornell.dendro.corina.gis;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.cornell.dendro.corina.gui.menus.EditMenu;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.ui.Builder;
import gov.nasa.worldwind.layers.MarkerLayer;

public class GISFrame extends JFrame {

	private static final long serialVersionUID = -451333846688316647L;
	protected GISPanel wwMapPanel;
	
	/**
	 * Simple constructor with all sites shown on map
	 */
	public GISFrame()
	{
		setupGui(TridasMarkerLayerBuilder.getMarkerLayerForAllSites());
		setupMenus();
	}
	
	/**
	 * Simple constructor which shows given layer on map
	 * 
	 * @param layer
	 */
	public GISFrame(MarkerLayer layer)
	{
		setupGui(layer);
		setupMenus();
	}
	
	private void setupGui(MarkerLayer layer)
	{
		wwMapPanel = new GISPanel(new Dimension(300,300),true, layer);
		
		add(wwMapPanel, BorderLayout.CENTER);
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
	        	
				this.add(gazPanel,   //use default yahoo service
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

		menuBar.add(new EditMenu(this));
		
		menuBar.add(new GISViewMenu(wwMapPanel.getWwd(), wwMapPanel.getVisibleLayers()));
	
		if (Platform.isMac())
			menuBar.add(new WindowMenu(this));
		menuBar.add(new HelpMenu());
        
		
		
        this.setJMenuBar(menuBar);

	}
	

}
