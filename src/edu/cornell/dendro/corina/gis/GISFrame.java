package edu.cornell.dendro.corina.gis;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.cornell.dendro.corina.gui.menus.AdminMenu;
import edu.cornell.dendro.corina.gui.menus.EditMenu;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.ui.Builder;

public class GISFrame extends JFrame {

	private static final long serialVersionUID = -451333846688316647L;
	protected GISPanel wwMapPanel;
	
	public GISFrame()
	{
		setupGui();
		setupMenus();
	}
	
	
	private void setupGui()
	{
		wwMapPanel = new GISPanel(new Dimension(300,300),true, 
				TridasMarkerLayerBuilder.getMarkerLayerForAllSites());
		
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
        	CorinaGazetteerPanel gazPanel = new CorinaGazetteerPanel(wwMapPanel.getWwd(), null);
        	
			this.add(gazPanel,   //use default yahoo service
			        BorderLayout.SOUTH);
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
        
		menuBar.add(new GISFileMenu(this, wwMapPanel));

		menuBar.add(new EditMenu(this));
		menuBar.add(new AdminMenu(this));
		
		if (Platform.isMac())
			menuBar.add(new WindowMenu(this));
		menuBar.add(new HelpMenu());
        
		
        this.setJMenuBar(menuBar);

	}
}
