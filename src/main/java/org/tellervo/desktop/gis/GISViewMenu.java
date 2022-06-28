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

import gov.nasa.worldwind.WorldWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class GISViewMenu extends JMenu implements ItemListener, ActionListener {

	private static final long serialVersionUID = -6739232540394701181L;
	private JMenuItem overview, compass, scalebar, layerslist, stars, atmosphere, blueMarble, blueMarbleWMS2004, landsat, 
	usda, msAerial, boundaries, placenames, wmslayermanager;

	private WorldWindow wwd;
;
	private WMSManager layermanager;
	
	public enum GISLayers{
		OVERVIEW("World Map"), 
		COMPASS("Compass"), SCALEBAR("Scale bar"), LAYERSLIST("Layer List"), STARS("Stars"), ATMOSPHERE("Atmosphere"), BLUEMARBLE("NASA Blue Marble Image"), 
		BLUEMARBLEWMS2004("Blue Marble (WMS) 2004"), LANDSAT("i-cubed Landsat"), USDA("USDA NAIP"), MSAERIAL("MS Virtual Earth Aerial"), BOUNDARIES("Political Boundaries"), 
		PLACENAMES("Place Names");
		private String name;
		
		private GISLayers(String n)
		{
			name = n; 
		}
		public String getName()
		{
			return name;
		}
	};
	
	
	public GISViewMenu(WorldWindow wwd)
	{
        super(I18n.getText("menus.view"));
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        this.wwd = wwd;
        

        
		addLayersMenu();
		addSeparator();
		addControlMenuItems();

		linkModel();
	}
	
	protected void launchWMSLayerManager()
	{
		if(layermanager==null)
		{
			layermanager = new WMSManager(wwd);
		}
		layermanager.setVisible(true);
		
	}
	
	private void addLayersMenu()
	{
		JMenu layersmenu = Builder.makeMenu("menus.view.layers", "layers.png");

        //JMenuItem wmslayermanager = Builder.makeMenuItem("menus.view.wmslayermanager", "org.tellervo.desktop.gis.GISViewMenu.launchWMSLayerManager()");
		wmslayermanager = new JMenuItem();
		wmslayermanager.setText(I18n.getText("menus.view.wmslayermanager"));
		wmslayermanager.addActionListener(this);
		

		stars = Builder.makeCheckBoxMenuItem("menus.view.layers.stars");
		atmosphere = Builder.makeCheckBoxMenuItem("menus.view.layers.atmosphere");
		blueMarble = Builder.makeCheckBoxMenuItem("menus.view.layers.blueMarble");
		blueMarbleWMS2004 = Builder.makeCheckBoxMenuItem("menus.view.layers.blueMarbleWMS2004");
		landsat = Builder.makeCheckBoxMenuItem("menus.view.layers.landsat");
		usda = Builder.makeCheckBoxMenuItem("menus.view.layers.usda");
		msAerial = Builder.makeCheckBoxMenuItem("menus.view.layers.msAerial");
		boundaries = Builder.makeCheckBoxMenuItem("menus.view.layers.boundaries");
		placenames = Builder.makeCheckBoxMenuItem("menus.view.layers.placenames");

		stars.addItemListener(this);
		atmosphere.addItemListener(this);
		blueMarble.addItemListener(this);
		blueMarbleWMS2004.addItemListener(this);
		landsat.addItemListener(this);
		usda.addItemListener(this);
		msAerial.addItemListener(this);
		boundaries.addItemListener(this);
		placenames.addItemListener(this);

		
		layersmenu.add(blueMarble);
		layersmenu.add(blueMarbleWMS2004);
		layersmenu.add(landsat);
		layersmenu.add(msAerial);
		//layersmenu.add(usda);
		layersmenu.addSeparator();
		layersmenu.add(stars);
		layersmenu.add(atmosphere);
		layersmenu.addSeparator();
		layersmenu.add(boundaries);
		layersmenu.add(placenames);
		layersmenu.addSeparator();
		layersmenu.add(wmslayermanager);
		
		this.add(layersmenu);
	}
	
	private void addControlMenuItems()
	{
		overview = Builder.makeCheckBoxMenuItem("menus.view.overview", "map.png");
		compass = Builder.makeCheckBoxMenuItem("menus.view.compass", "compass.png");
		scalebar = Builder.makeCheckBoxMenuItem("menus.view.scalebar", "scale.png");
		layerslist = Builder.makeCheckBoxMenuItem("menus.view.layerslist", "list.png");

		setupLayerMenuButtons();

		layerslist.addItemListener(this);
		overview.addItemListener(this);
		compass.addItemListener(this);
		scalebar.addItemListener(this);
		
		add(layerslist);
		add(overview);
		add(compass);
		add(scalebar);
		
	}

	private void setupLayerMenuButtons()
	{
		for (GISLayers layer : GISLayers.values())
		{
			setupMenuButton(layer);
		}
	}
	
	private void setupMenuButton(GISLayers layer)
	{
		try{
			setMenuButtonSelected(wwd.getModel().getLayers().getLayerByName(layer.getName()).isEnabled(), layer);
		} catch (Exception e)
		{
			if(getMenuItemFromLayer(layer)!=null)
			{
				getMenuItemFromLayer(layer).setSelected(false);
				getMenuItemFromLayer(layer).setEnabled(false);
			}
		}
	}
	
	private void setMenuButtonSelected(Boolean b, GISLayers layer)
	{
		try{
			wwd.getModel().getLayers().getLayerByName(layer.getName()).setEnabled(b);
		} catch (Exception e)
		{
			if(getMenuItemFromLayer(layer)!=null)
			{
				getMenuItemFromLayer(layer).setSelected(false);
				getMenuItemFromLayer(layer).setEnabled(false);
			}
		}
		
		getMenuItemFromLayer(layer).setSelected(b);
		
	}
	
	private JMenuItem getMenuItemFromLayer(GISLayers layer)
	{
		switch(layer)
		{
			case ATMOSPHERE: 		return atmosphere;
			case OVERVIEW:			return overview;
			case COMPASS:			return compass;
			case SCALEBAR:			return scalebar;
			case LAYERSLIST:		return layerslist;
			case STARS:				return stars;
			case BLUEMARBLE:		return blueMarble;
			case BLUEMARBLEWMS2004:	return blueMarbleWMS2004;
			case LANDSAT:			return landsat;
			case USDA:				return usda;
			case MSAERIAL:			return msAerial;
			case BOUNDARIES:		return boundaries;
			case PLACENAMES:		return placenames;
			default: return null;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {

        Boolean selected = event.getStateChange() == ItemEvent.SELECTED;
        
        if(event.getSource()==overview)
        {
        	setMenuButtonSelected(selected, GISLayers.OVERVIEW);
        }
        else if(event.getSource()==compass)
        {
        	setMenuButtonSelected(selected, GISLayers.COMPASS);
        }
        else if(event.getSource()==scalebar)
        {
        	setMenuButtonSelected(selected, GISLayers.SCALEBAR);
        }
        else if(event.getSource()==atmosphere)
        {
        	setMenuButtonSelected(selected, GISLayers.ATMOSPHERE);
        }
        else if(event.getSource()==stars)
        {
        	setMenuButtonSelected(selected, GISLayers.STARS);
        }
        else if(event.getSource()==blueMarble)
        {
        	setMenuButtonSelected(selected, GISLayers.BLUEMARBLE);
        }
        else if(event.getSource()==blueMarbleWMS2004)
        {
        	setMenuButtonSelected(selected, GISLayers.BLUEMARBLEWMS2004);
        }
        else if(event.getSource()==landsat)
        {
        	setMenuButtonSelected(selected, GISLayers.LANDSAT);
        }
        else if(event.getSource()==usda)
        {
        	setMenuButtonSelected(selected, GISLayers.USDA);
        }
        else if(event.getSource()==msAerial)
        {
        	setMenuButtonSelected(selected, GISLayers.MSAERIAL);
        }
        else if(event.getSource()==boundaries)
        {
        	setMenuButtonSelected(selected, GISLayers.BOUNDARIES);
        }
        else if(event.getSource()==placenames)
        {
        	setMenuButtonSelected(selected, GISLayers.PLACENAMES);
        }
        else if(event.getSource()==layerslist)
        {
        	setMenuButtonSelected(selected, GISLayers.LAYERSLIST);
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==wmslayermanager)
		{
			launchWMSLayerManager();
		}
		
	}
    
    
	protected void linkModel()
	{
		  App.appmodel.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent argEvt) {
					if(argEvt.getPropertyName().equals(AppModel.NETWORK_STATUS)){
						setEnabled(App.isLoggedIn());
					}	
				}
			});
		  
	}
	  
	
	
}
