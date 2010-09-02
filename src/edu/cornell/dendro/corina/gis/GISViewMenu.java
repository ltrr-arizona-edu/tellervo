package edu.cornell.dendro.corina.gis;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GISViewMenu extends JMenu implements ItemListener {

	private static final long serialVersionUID = -6739232540394701181L;
	private JFrame parent;
	private JMenuItem overview, compass, scalebar, layerslist, stars, atmosphere, blueMarble, blueMarbleWMS2004, landsat, 
	usda, msAerial, boundaries, placenames;
	private  ItemListener iListener;
	private WorldWindow wwd;
	private ArrayList<String> visibleLayers;
	
	
	public GISViewMenu(JFrame parent, WorldWindow wwd, ArrayList<String> visibleLayers)
	{
        super(I18n.getText("menus.view"));
        
        this.parent = parent;
        this.wwd = wwd;
        this.visibleLayers = visibleLayers;
        
		addLayersMenu();
		addControlMenuItems();

		
	}
	
	private void addLayersMenu()
	{
		JMenu layersmenu = Builder.makeMenu("menus.view.layers", "prosheet.png");
		
		layerslist = Builder.makeCheckBoxMenuItem("menus.view.layerslist");
		stars = Builder.makeCheckBoxMenuItem("menus.view.layers.stars");
		atmosphere = Builder.makeCheckBoxMenuItem("menus.view.layers.atmosphere");
		blueMarble = Builder.makeCheckBoxMenuItem("menus.view.layers.blueMarble");
		blueMarbleWMS2004 = Builder.makeCheckBoxMenuItem("menus.view.layers.blueMarbleWMS2004");
		landsat = Builder.makeCheckBoxMenuItem("menus.view.layers.landsat");
		usda = Builder.makeCheckBoxMenuItem("menus.view.layers.usda");
		msAerial = Builder.makeCheckBoxMenuItem("menus.view.layers.msAerial");
		boundaries = Builder.makeCheckBoxMenuItem("menus.view.layers.boundaries");
		placenames = Builder.makeCheckBoxMenuItem("menus.view.layers.placenames");

		
		layerslist.addItemListener(this);
		
		layersmenu.add(layerslist);
		layersmenu.addSeparator();
		
		
		this.add(layersmenu);
	}
	
	private void addControlMenuItems()
	{
		overview = Builder.makeCheckBoxMenuItem("menus.view.overview");
		compass = Builder.makeCheckBoxMenuItem("menus.view.compass");
		scalebar = Builder.makeCheckBoxMenuItem("menus.view.scalebar");
	
		LayerList layers = wwd.getModel().getLayers();
		
		setupOverview();
		setupCompass();
		setupScaleBar();
		
		overview.addItemListener(this);
		compass.addItemListener(this);
		scalebar.addItemListener(this);
		
		add(overview);
		add(compass);
		add(scalebar);
	}

	private void setupOverview()
	{
		setOverviewSelected(wwd.getModel().getLayers().getLayerByName("World Map").isEnabled());
	}
	
	private void setOverviewSelected(Boolean b)
	{
		wwd.getModel().getLayers().getLayerByName("World Map").setEnabled(b);		
		overview.setSelected(b);
	}
	
	private void setupCompass()
	{
		setCompassSelected(wwd.getModel().getLayers().getLayerByName("Compass").isEnabled());
	}
	
	private void setCompassSelected(Boolean b)
	{
		wwd.getModel().getLayers().getLayerByName("Compass").setEnabled(b);		
		compass.setSelected(b);
	}
	
	private void setupScaleBar()
	{
		setScaleBarSelected(wwd.getModel().getLayers().getLayerByName("Scale bar").isEnabled());
	}
	
	private void setScaleBarSelected(Boolean b)
	{
		wwd.getModel().getLayers().getLayerByName("Scale bar").setEnabled(b);		
		scalebar.setSelected(b);
	}
	
	@Override
	public void itemStateChanged(ItemEvent event) {

        Boolean selected = event.getStateChange() == ItemEvent.SELECTED;
        
        if(event.getSource()==overview)
        {
        	setOverviewSelected(selected);
        }
        else if(event.getSource()==compass)
        {
        	setCompassSelected(selected);
        }
        else if(event.getSource()==scalebar)
        {
        	setScaleBarSelected(selected);
        }
	
	    }
    
    
	
	
	
}
