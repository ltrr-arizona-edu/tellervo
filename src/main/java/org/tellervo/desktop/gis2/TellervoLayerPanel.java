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
import gov.nasa.worldwind.render.Renderable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.menus.AbstractEditorActions;
import org.tellervo.desktop.gui.menus.FullEditorActions;




import net.miginfocom.swing.MigLayout;

public class TellervoLayerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(TellervoLayerPanel.class);
	protected FullEditorActions actions;
	protected FullEditor editor;

	    protected JPanel layersPanel;
	    protected JPanel westPanel;
	    protected JScrollPane scrollPane;
	    protected Font defaultFont;
	    private JPanel panel;
	    private JLabel lblMapLayers;


	    
	    /**
	     * Create a panel with the default size.
	     *
	     * @param wwd WorldWindow to supply the layer list.
	     * @wbp.parser.constructor
	     */
	    public TellervoLayerPanel(WorldWindow wwd)
	    {
	        // Make a panel at a default size.
	        super(new BorderLayout());
	        actions = FullEditor.getInstance().getAction();
	        this.makePanel(wwd, new Dimension(200, 400));


	        
	    }

	    /**
	     * Create a panel with a size.
	     *
	     * @param wwd  WorldWindow to supply the layer list.
	     * @param size Size of the panel.
	     */
	    public TellervoLayerPanel(WorldWindow wwd, Dimension size)
	    {
	        // Make a panel at a specified size.
	        super(new BorderLayout());
	        actions = FullEditor.getInstance().getAction();
	        this.makePanel(wwd, size);

	    }

	    protected void makePanel(WorldWindow wwd, Dimension size)
	    {
	        // Make and fill the panel holding the layer titles.
	        this.layersPanel = new JPanel(new GridLayout(0, 1, 0, 4));
	        layersPanel.setBackground(Color.WHITE);
	        
	        

	        this.layersPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        this.fill(wwd);

	        // Must put the layer grid in a container to prevent scroll panel from stretching their vertical spacing.
	        JPanel dummyPanel = new JPanel(new BorderLayout());
	        dummyPanel.setBackground(Color.WHITE);
	        dummyPanel.add(this.layersPanel, BorderLayout.NORTH);

	        // Put the name panel in a scroll bar.
	        this.scrollPane = new JScrollPane(dummyPanel);
	        scrollPane.getViewport().setOpaque(true);
	        scrollPane.getViewport().setBackground(Color.WHITE);
	        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	        if (size != null)
	            this.scrollPane.setPreferredSize(size);

	        // Add the scroll bar and name panel to a titled panel that will resize with the main window.
	        westPanel = new JPanel();
	        westPanel.setToolTipText("Layers to Show");
	        westPanel.setLayout(new BorderLayout(0, 0));
	        westPanel.add(scrollPane);
	        this.add(westPanel, BorderLayout.CENTER);
	        
	        panel = new JPanel();
	        westPanel.add(panel, BorderLayout.NORTH);
	        panel.setLayout(new MigLayout("", "[78px]", "[15px]"));
	        
	        lblMapLayers = new JLabel("Map layers:");
	        panel.add(lblMapLayers, "cell 0 0,alignx left,aligny top");
	    }

	    protected void fill(WorldWindow wwd)
	    {
	        JLabel heading = new JLabel("Background layers");
			heading.setFont(new Font("Dialog", Font.BOLD, 13));
			this.layersPanel.add(heading);


	        for (JCheckBox cbx : getBackgroundLayers(wwd))
	        {
	        	this.layersPanel.add(cbx);
	        	initPopupMenu(wwd, cbx);
	        }
	        
			heading = new JLabel("Data layers");
			heading.setFont(new Font("Dialog", Font.BOLD, 13));
			this.layersPanel.add(heading);        
			for (JCheckBox cbx : getDataLayers(wwd))
	        {
	        	this.layersPanel.add(cbx);
	        	initPopupMenu(wwd, cbx);
	        	
	        }	
	     }
	    
	    
	    protected void initPopupMenu(WorldWindow wwd, JCheckBox cbx){
	    	
			log.debug("Init popup menu");
			Layer layer;
		
			
			final JPopupMenu popupMenu = new JPopupMenu();
			
			JMenu addLayers = new JMenu(actions.mapAddLayersAction);
						
			JMenuItem shapeFileLayer = new JMenuItem(actions.mapShapefileLayerAction);
			addLayers.add(shapeFileLayer);
			
			JMenuItem KMLLayer = new JMenuItem(actions.mapKMLLayerAction);
			addLayers.add(KMLLayer);
			
			JMenuItem WMSLayer = new JMenuItem(actions.mapWMSLayerAction);
			addLayers.add(WMSLayer);
			
			JMenuItem GISLayer = new JMenuItem(actions.mapGISImageAction);
			addLayers.add(GISLayer);
			
			popupMenu.add(addLayers);
			
				
			cbx.addMouseListener(new MouseAdapter() {
			    public void mousePressed(MouseEvent e) {
			        showPopup(e);
			    }

			    public void mouseReleased(MouseEvent e) {
			        showPopup(e);
			    }

			    private void showPopup(MouseEvent e) {
			       
					if (e.isPopupTrigger()) {
			            popupMenu.show(e.getComponent(),
			                       e.getX(), e.getY());
			        }
			    }
			});
	
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
        	
        	if(layer instanceof TellervoDataLayer || layer instanceof Renderable) continue;
        	
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
        	
        	if(layer instanceof TellervoDataLayer || layer instanceof Renderable)
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

	   	


}
