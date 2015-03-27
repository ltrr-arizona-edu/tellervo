package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.Earth.BMNGWMSLayer;
import gov.nasa.worldwind.layers.Earth.CountryBoundariesLayer;
import gov.nasa.worldwind.layers.Earth.MGRSGraticuleLayer;
import gov.nasa.worldwind.layers.Earth.NASAWFSPlaceNameLayer;
import gov.nasa.worldwind.layers.Earth.UTMGraticuleLayer;
import gov.nasa.worldwindx.examples.LayerPanel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class TellervoLayerPanel extends LayerPanel {

	private static final long serialVersionUID = 1L;

	public TellervoLayerPanel(WorldWindow wwd) {
		super(wwd);
	}

	@Override
    protected void fill(WorldWindow wwd)
    {
		JLabel heading = new JLabel("Background layers");
		heading.setFont(new Font("Dialog", Font.BOLD, 13));
		this.layersPanel.add(heading);

        for (JCheckBox cbx : getBackgroundLayers(wwd))
        {
        	this.layersPanel.add(cbx);
        }
        
		heading = new JLabel("Data layers");
		heading.setFont(new Font("Dialog", Font.BOLD, 13));
		this.layersPanel.add(heading);        
		for (JCheckBox cbx : getDataLayers(wwd))
        {
        	this.layersPanel.add(cbx);
        }
        
    }
	
	private boolean isHiddenLayer(Layer layer)
	{
		if(layer instanceof StarsLayer || 
    			layer instanceof ScalebarLayer ||
    			layer instanceof SkyColorLayer ||
    			layer instanceof SkyGradientLayer ||
    			layer instanceof BMNGWMSLayer ||
    			layer instanceof CompassLayer ||
    			layer instanceof UTMGraticuleLayer ||
    			layer instanceof MGRSGraticuleLayer ||
    			layer instanceof NASAWFSPlaceNameLayer ||
    			layer instanceof CountryBoundariesLayer || 
    			layer.getName().equals("Popup information") || 
    			layer instanceof ViewControlsLayer ||
    			layer instanceof WorldMapLayer)
    	{
    		return true;
    	}
		
		return false;
	}
	
	
	private ArrayList<JCheckBox> getBackgroundLayers(WorldWindow wwd)
	{
		ArrayList<JCheckBox> items = new ArrayList<JCheckBox>();
		
        for (Layer layer : wwd.getModel().getLayers())
        {
        	if(isHiddenLayer(layer)) continue;
        	
        	if(layer instanceof TellervoDataLayer) continue;
        	
            LayerAction action = new LayerAction(layer, wwd, layer.isEnabled());
            JCheckBox jcb = new JCheckBox(action);
            jcb.setSelected(action.selected);
            items.add(jcb);

            if (defaultFont == null)
            {
                this.defaultFont = jcb.getFont();
            }
        }

		return items;				
	}
	
	
	private ArrayList<JCheckBox> getDataLayers(WorldWindow wwd)
	{
		ArrayList<JCheckBox> items = new ArrayList<JCheckBox>();
		
        for (Layer layer : wwd.getModel().getLayers())
        {
        	if(isHiddenLayer(layer)) continue;
        	
        	if(layer instanceof TellervoDataLayer)
        	{
	        	
	            LayerAction action = new LayerAction(layer, wwd, layer.isEnabled());
	            JCheckBox jcb = new JCheckBox(action);
	            jcb.setSelected(action.selected);
	            items.add(jcb);
	
	            if (defaultFont == null)
	            {
	                this.defaultFont = jcb.getFont();
	            }
        	}
        }

		return items;				
	}
	
	
    protected static class LayerAction extends AbstractAction
    {

		private static final long serialVersionUID = 1L;
		protected WorldWindow wwd;
        protected Layer layer;
        protected boolean selected;

        public LayerAction(Layer layer, WorldWindow wwd, boolean selected)
        {
            super(layer.getName());
            this.wwd = wwd;
            this.layer = layer;
            this.selected = selected;
            this.layer.setEnabled(this.selected);
        }

        public void actionPerformed(ActionEvent actionEvent)
        {
            // Simply enable or disable the layer based on its toggle button.
            if (((JCheckBox) actionEvent.getSource()).isSelected())
                this.layer.setEnabled(true);
            else
                this.layer.setEnabled(false);

            wwd.redraw();
        }
    }

}
