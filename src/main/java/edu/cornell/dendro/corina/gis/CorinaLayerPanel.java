package edu.cornell.dendro.corina.gis;

import javax.swing.JPanel;

/*
Copyright (C) 2001, 2011 United States Government as represented by
the Administrator of the National Aeronautics and Space Administration.
All Rights Reserved.
*/


import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panel to display a list of layers. A layer can be turned on or off by clicking a check box next to the layer name.
 *
 * @version $Id: LayerPanel.java 1 2011-07-16 23:22:47Z dcollins $
 *
 * @see LayerTreeUsage
 * @see OnScreenLayerManager
 */
public class CorinaLayerPanel extends JPanel
{
    protected JPanel layersPanel;
    protected JPanel westPanel;
    protected JScrollPane scrollPane;
    protected Font defaultFont;
    private JPanel secondPanel;
    private JScrollPane scrollPane_1;
    private JPanel panel_1;
    private JPanel controlsPanel;
    private JPanel thirdPanel;
    private JScrollPane scrollPane_2;
    private JPanel panel_2;
    private JPanel backgroundPanel;
    private JSplitPane splitPane;
    private JSplitPane splitPane_1;
    private JButton btnAddData;

    /**
     * Create a panel with the default size.
     *
     * @param wwd WorldWindow to supply the layer list.
     * @wbp.parser.constructor
     */
    public CorinaLayerPanel(WorldWindow wwd)
    {
        this.makePanel(wwd, new Dimension(200, 400));
    }

    /**
     * Create a panel with a size.
     *
     * @param wwd  WorldWindow to supply the layer list.
     * @param size Size of the panel.
     */
    public CorinaLayerPanel(WorldWindow wwd, Dimension size)
    {
        // Make a panel at a specified size.
        super(new BorderLayout());
        this.makePanel(wwd, size);
    }

    protected void makePanel(WorldWindow wwd, Dimension size)
    {
        if (size != null)
            this.scrollPane.setPreferredSize(size);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.33);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        add(splitPane);
        // Make and fill the panel holding the layer titles.
        this.layersPanel = new JPanel(new GridLayout(0, 1, 0, 4));        
        this.layersPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        
                // Must put the layer grid in a container to prevent scroll panel from stretching their vertical spacing.
                JPanel dummyPanel = new JPanel(new BorderLayout());
                dummyPanel.add(this.layersPanel, BorderLayout.NORTH);
                
                        // Put the name panel in a scroll bar.
                        this.scrollPane = new JScrollPane(dummyPanel);
                        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add the scroll bar and name panel to a titled panel that will resize with the main window.
        westPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        splitPane.setLeftComponent(westPanel);
        westPanel.setBorder(
            new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Layers")));
        westPanel.setToolTipText("Layers to Show");
        westPanel.add(scrollPane);
        
        splitPane_1 = new JSplitPane();
        splitPane_1.setResizeWeight(0.5);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setRightComponent(splitPane_1);
        
        secondPanel = new JPanel();
        splitPane_1.setRightComponent(secondPanel);
        secondPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Controls and decorations")));
        secondPanel.setLayout(new GridLayout(0, 1, 0, 0));
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        secondPanel.add(scrollPane_1);
        
        
        panel_1 = new JPanel();
        scrollPane_1.setViewportView(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        controlsPanel = new JPanel();
        controlsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel_1.add(controlsPanel, BorderLayout.NORTH);
        controlsPanel.setLayout(new GridLayout(0, 1, 0, 4));
        
        thirdPanel = new JPanel();
        splitPane_1.setLeftComponent(thirdPanel);
        thirdPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Background layers")));
        thirdPanel.setLayout(new GridLayout(0, 1, 0, 0));
        
        scrollPane_2 = new JScrollPane();
        scrollPane_2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        thirdPanel.add(scrollPane_2);
        
        panel_2 = new JPanel();
        scrollPane_2.setViewportView(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));
        
        backgroundPanel = new JPanel();
        backgroundPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel_2.add(backgroundPanel, BorderLayout.NORTH);
        backgroundPanel.setLayout(new GridLayout(0, 1, 0, 4));
      	        
        this.fill(wwd);
    }

    protected void fill(WorldWindow wwd)
    {
        // Fill the layers panel with the titles of all layers in the world window's current model.
        for (Layer layer : wwd.getModel().getLayers())
        {        	
            LayerAction action = new LayerAction(layer, wwd, layer.isEnabled());
            JCheckBox jcb = new JCheckBox(action);
            jcb.setSelected(action.selected);
            
            
        	if(layer instanceof CompassLayer ||
        	   layer instanceof WorldMapLayer || 
        	   layer instanceof ScalebarLayer ||
        	   layer instanceof ViewControlsLayer ||
        	   layer.getName().equals("Stars") ||
        	   layer.getName().equals("Atmosphere")) 
        	{
        		this.controlsPanel.add(jcb);
        	}
        	else if (layer instanceof MarkerLayer)
        	{
        		this.layersPanel.add(jcb);
        	}
        	else
        	{
        		this.backgroundPanel.add(jcb);
        	}

            if (defaultFont == null)
            {
                this.defaultFont = jcb.getFont();
            }
        }
    }

    /**
     * Update the panel to match the layer list active in a WorldWindow.
     *
     * @param wwd WorldWindow that will supply the new layer list.
     */
    public void update(WorldWindow wwd)
    {
        // Replace all the layer names in the layers panel with the names of the current layers.
        this.layersPanel.removeAll();
        this.fill(wwd);
        this.westPanel.revalidate();
        this.westPanel.repaint();
    }

    @Override
    public void setToolTipText(String string)
    {
        this.scrollPane.setToolTipText(string);
    }

    protected static class LayerAction extends AbstractAction
    {
        WorldWindow wwd;
        private Layer layer;
        private boolean selected;

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
