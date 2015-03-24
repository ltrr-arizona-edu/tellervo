package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.Earth.BMNGWMSLayer;
import gov.nasa.worldwind.layers.Earth.CountryBoundariesLayer;
import gov.nasa.worldwind.layers.Earth.MGRSGraticuleLayer;
import gov.nasa.worldwind.layers.Earth.NASAWFSPlaceNameLayer;
import gov.nasa.worldwind.layers.Earth.UTMGraticuleLayer;
import gov.nasa.worldwindx.examples.LayerPanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

public class TellervoLayerPanel extends LayerPanel {

	private static final long serialVersionUID = 1L;

	public TellervoLayerPanel(WorldWindow wwd) {
		super(wwd);
	}

	@Override
    protected void fill(WorldWindow wwd)
    {
        // Fill the layers panel with the titles of all layers in the world window's current model.
        for (Layer layer : wwd.getModel().getLayers())
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
        			layer instanceof CountryBoundariesLayer)
        	{
        		// Do not include these in layer list as they are basic controls
        		continue;
        	}
        	
        	
            LayerAction action = new LayerAction(layer, wwd, layer.isEnabled());
            JCheckBox jcb = new JCheckBox(action);
            jcb.setSelected(action.selected);
            this.layersPanel.add(jcb);

            if (defaultFont == null)
            {
                this.defaultFont = jcb.getFont();
            }
        }
    }
	
    protected static class LayerAction extends AbstractAction
    {
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
