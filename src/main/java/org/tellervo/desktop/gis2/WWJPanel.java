package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.Earth.BMNGWMSLayer;
import gov.nasa.worldwind.layers.Earth.CountryBoundariesLayer;
import gov.nasa.worldwind.layers.Earth.LandsatI3WMSLayer;
import gov.nasa.worldwind.layers.Earth.MGRSGraticuleLayer;
import gov.nasa.worldwind.layers.Earth.MSVirtualEarthLayer;
import gov.nasa.worldwind.layers.Earth.NASAWFSPlaceNameLayer;
import gov.nasa.worldwind.layers.Earth.OSMMapnikLayer;
import gov.nasa.worldwind.layers.Earth.USGSTopoHighRes;
import gov.nasa.worldwind.layers.Earth.USGSTopoLowRes;
import gov.nasa.worldwind.layers.Earth.USGSTopoMedRes;
import gov.nasa.worldwind.layers.Earth.USGSUrbanAreaOrtho;
import gov.nasa.worldwind.layers.Earth.UTMGraticuleLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.util.layertree.KMLLayerTreeNode;
import gov.nasa.worldwind.util.layertree.KMLNetworkLinkTreeNode;
import gov.nasa.worldwindx.examples.LayerPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.ITRDBMarker;
import org.tellervo.desktop.gis.TridasAnnotation;
import org.tellervo.desktop.gis.TridasAnnotationController;
import org.tellervo.desktop.gis.TridasMarker;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tridas.interfaces.ITridas;

public class WWJPanel extends JPanel  implements SelectListener{

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(WWJPanel.class);

	private JPanel mainPanel;
	
	private ViewControlsLayer viewControlsLayer;
	
	protected WorldWindow wwd;
	public TellervoLayerPanel layerPanel;
	protected StatusBar statusBar;
	protected LayerList layersList;
	private RenderableLayer annotationLayer;

	public WWJPanel() {
		super(new BorderLayout());
		init();

	}

	public LayerList getLayersList()
	{
		return layersList;
	}
	
	private void init()
	{
		// Create map and holder
		this.wwd = new WorldWindowGLCanvas();
		((Component) this.wwd).setPreferredSize(new Dimension(300,300));
		JPanel mapHolder = new JPanel();
		mapHolder.setLayout(new BorderLayout());
		mapHolder.add((Component) wwd, BorderLayout.CENTER);
		
		// Create the default model as described in the current worldwind properties.
		Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		this.wwd.setModel(m);

		
        // Create the Model, starting with the Globe.
        Globe earth = new Earth();
        
        
        this.annotationLayer = new RenderableLayer();
        annotationLayer.setName("Popup information");
        
        viewControlsLayer = new ViewControlsLayer();
        
        // Create layers that both World Windows can share.
        Layer[] layers = new Layer[]
            {
        		annotationLayer,
                new StarsLayer(),
                new ScalebarLayer(),
                new SkyColorLayer(),
                new SkyGradientLayer(),
                new BMNGWMSLayer(),
                                
                new CompassLayer(),
                viewControlsLayer,
                new WorldMapLayer(),
                new UTMGraticuleLayer(),
                new MGRSGraticuleLayer(),
                new NASAWFSPlaceNameLayer(),
                new CountryBoundariesLayer(),
                
                new LandsatI3WMSLayer(),
                new MSVirtualEarthLayer(),
                new OSMMapnikLayer(),
                new USGSTopoHighRes(),
                new USGSTopoMedRes(),
                new USGSTopoLowRes(),
                new USGSUrbanAreaOrtho(),
                new AllSitesLayer(),
                new TridasEntityLayer("Workspace series"),
            };
        
        // Turn off some layers by default 
        for(Layer layer : layers)
        {
        	if(layer instanceof UTMGraticuleLayer || 
        			layer instanceof MGRSGraticuleLayer ||
        			layer instanceof LandsatI3WMSLayer ||
        			layer instanceof CountryBoundariesLayer ||
        			layer instanceof MSVirtualEarthLayer ||
        			layer instanceof OSMMapnikLayer ||
        			layer instanceof USGSTopoHighRes ||
        			layer instanceof USGSTopoMedRes ||
        			layer instanceof USGSTopoLowRes ||
        			layer instanceof USGSUrbanAreaOrtho ||
        			layer instanceof AllSitesLayer)
        	{
        		layer.setEnabled(false);
        	}
        	
        }
        
        layersList = new LayerList(layers);

        WorldMapLayer layer = new WorldMapLayer();
        //layer.get
        
        // Create two models and pass them the shared layers.
        Model model = new BasicModel();
        model.setGlobe(earth);
        model.setLayers(layersList);
        wwd.setModel(model);
        
        // Set control layer enabled/disabled based on preferences
        model.getLayers().getLayerByName(Logging.getMessage("layers.ViewControlsLayer.Name")).setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_CONTROLS_ENABLED, true));
        
        // Set compass layer enabled/disabled based on preferences
        model.getLayers().getLayerByName(Logging.getMessage("layers.CompassLayer.Name")).setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_COMPASS_ENABLED, true));
        
        // Set MGRS graticule layer enabled/disabled based on preferences
        model.getLayers().getLayerByName(Logging.getMessage("layers.Earth.MGRSGraticule.Name")).setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_MGRSGRATICULE_ENABLED, true));
        
        // Set NASAWFS place name layer enabled/disabled based on preferences
        model.getLayers().getLayerByName(Logging.getMessage("layers.Earth.PlaceName.Name")).setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_NASAWFSPLACENAME_ENABLED, true));
        
        // Set Scale bar layer enabled/disabled based on preferences
        model.getLayers().getLayerByName(Logging.getMessage("layers.Earth.ScalebarLayer.Name")).setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_SCALEBAR_ENABLED, true));
     
        // Set world map layer enabled/disabled based on preferences
        model.getLayers().getLayerByName(Logging.getMessage("layers.Earth.WorldMapLayer.Name")).setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_WORLDMAP_ENABLED, true));
        
        // Set UTM graticule layer enabled/disabled based on preferences
        model.getLayers().getLayerByName(Logging.getMessage("layers.Earth.UTMGraticule.Name")).setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_UTMGRATICULE_ENABLED, true));
        
        // Set country boundaries enabled/disabled based on preferences
        model.getLayers().getLayerByName("Political Boundaries").setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_COUNTRYBOUNDARY_ENABLED, true));
        
        //Set stereo mode on based on preferences
		if(System.getProperty("gov.nasa.worldwind.stereo.mode")!=null && System.getProperty("gov.nasa.worldwind.stereo.mode").equals("redblue"))
		{
			System.setProperty("gov.nasa.worldwind.stereo.mode", "");
			setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_STEREOMODE_ENABLED, true));
	        
		}
		else
		{
			System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");
			setEnabled(App.prefs.getBooleanPref(PrefKey.MAP_STEREOMODE_ENABLED, true));


		}
        
        
		// Create status bar
		this.statusBar = new StatusBar();
		this.add(statusBar, BorderLayout.PAGE_END);
		this.statusBar.setEventSource(wwd);
		
	
		// Create layers panel
		this.layerPanel = new TellervoLayerPanel(wwd);
			
		
		// Create split pane
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		splitPane.setLeftComponent(mapHolder);
		splitPane.setRightComponent(layerPanel);
		splitPane.setDividerLocation(0.85);
		splitPane.setResizeWeight(1.0);
		splitPane.setOneTouchExpandable(true);
		
		wwd.addSelectListener(this);
		
		
        // Create and install the view controls layer and register a controller for it with the World Window.
        this.getWwd().addSelectListener(new ViewControlsSelectListener(this.getWwd(), viewControlsLayer));
	}
	
	public WorldWindow getWwd() {
		return wwd;
	}

	
	public void zoomToSample(Sample s)
	{
		TridasEntityLayer layer = getWorkspaceSeriesLayer();
		
		Marker marker = layer.getMarkerForSample(s);

		if(marker==null) 
		{
			Alert.message(App.mainWindow, "No location", "This series does not have a location associated with it.");
			
			return;
		}
		
		if(wwd.getView()==null) return;
		if(wwd.getView().getGlobe()==null) return;
		
		
		try{
        wwd.getView().stopAnimations();
        wwd.getView().goTo(marker.getPosition(), 500000d);
		} catch (Exception e)
		{
			e.printStackTrace();
			log.debug("Exception caught while zooming to sample");
		}
        	
		
	}
	
	/**
	 * Highlight a pin for the specified sample
	 * 
	 * @param s
	 */
	public void highlightMarkerForSample(Sample s)
	{
		this.getWorkspaceSeriesLayer().highlightMarkerForSample(s);
		removeAnnotations();
		openResource(getWorkspaceSeriesLayer().getMarkerForSample(s));
		this.repaint();
	}
	
	/**
	 * Close all open annotations
	 */
	public void removeAnnotations()
	{
		this.annotationLayer.removeAllRenderables();
	}
	
	/**
	 * Get the layer containing pins for the items open in the workspace
	 * 
	 * @return
	 */
	public TridasEntityLayer getWorkspaceSeriesLayer()
	{
		return (TridasEntityLayer) layersList.getLayerByName("Workspace series");
	}

	/**
	 * Animate zoom to the extent of all pins on the map 
	 */
	/*public void zoomToObjectsExtent()
	{
		log.debug("Zooming to object...");
		
		if(wwd.getView().getGlobe() == null) return;
		

        View view = wwd.getView();		
		
		 Vec4[] lookAtPoints = this.computeViewLookAtForScene(wwd.getView());
         if (lookAtPoints == null)
         {
             return;

         }
        
         else if (lookAtPoints.length != 3)
         {
         	 
        	 return;
         }

         Position centerPos = wwd.getModel().getGlobe().computePositionFromPoint(lookAtPoints[1]);
         double zoom = lookAtPoints[0].distanceTo3(lookAtPoints[1]);

         wwd.getView().stopAnimations();
         wwd.getView().goTo(centerPos, zoom);
         
	}
	
    public Vec4[] computeViewLookAtForScene(View view)
    {
    	
        Globe globe = wwd.getModel().getGlobe();
        
        if(globe==null) return null;
        
        double ve = wwd.getSceneController().getVerticalExaggeration();

        ExtentVisibilitySupport vs = new ExtentVisibilitySupport();
        this.addExtents(vs);

        return vs.computeViewLookAtContainingExtents(globe, ve, view);
    }
	
    protected void addExtents(ExtentVisibilitySupport vs)
    {
    

    	
        // Compute screen extents for WWIcons which have feedback information from their IconRenderer.
        Iterable<?> iterable = getMarkerLayer().getMarkers();
        if (iterable == null)
            return;

        ArrayList<ExtentHolder> extentHolders = new ArrayList<ExtentHolder>();
        ArrayList<ExtentVisibilitySupport.ScreenExtent> screenExtents =
            new ArrayList<ExtentVisibilitySupport.ScreenExtent>();

        for (Object o : iterable)
        {
            if (o == null)
                continue;

            if (o instanceof ExtentHolder)
            {
                extentHolders.add((ExtentHolder) o);
            }
            else if (o instanceof AVList)
            {
                AVList avl = (AVList) o;

                Object b = avl.getValue(AVKey.FEEDBACK_ENABLED);
                if (b == null || !Boolean.TRUE.equals(b))
                    continue;

                if (avl.getValue(AVKey.FEEDBACK_REFERENCE_POINT) != null)
                {
                    screenExtents.add(new ExtentVisibilitySupport.ScreenExtent(
                        (Vec4) avl.getValue(AVKey.FEEDBACK_REFERENCE_POINT),
                        (Rectangle) avl.getValue(AVKey.FEEDBACK_SCREEN_BOUNDS)));
                }
            }
        }

        if (!extentHolders.isEmpty())
        {
            Globe globe = wwd.getModel().getGlobe();
            double ve = wwd.getSceneController().getVerticalExaggeration();
            vs.setExtents(ExtentVisibilitySupport.extentsFromExtentHolders(extentHolders, globe, ve));
        }

        if (!screenExtents.isEmpty())
        {
            vs.setScreenExtents(screenExtents);
        }
    }*/
	
    protected void closeResource(ContentAnnotation content)
    {
        if (content == null)
            return;

        content.detach();
    }
    
    protected void openResource(Marker marker)
    {
        if (marker == null)
            return;

        ContentAnnotation content = this.createContent(marker);

        if (content != null)
        {
            content.attach();
        }
    }
    
    protected ContentAnnotation createContent(Marker marker)
    {
    	Position position = marker.getPosition();
    	
    	if(marker instanceof TridasMarker)
    	{
    		return createTridasAnnotation(this, position, "Title", ((TridasMarker) marker).getEntity());
    	}
    	else if (marker instanceof ITRDBMarker)
    	{
    		return createITRDBAnnotation(this, position, "Title", ((ITRDBMarker) marker));
    	}
    	
    	return null;
        
    }

    public static ContentAnnotation createITRDBAnnotation(WWJPanel mapPanel, Position position, String title, ITRDBMarker marker)
    {
        if (mapPanel == null)
        {
            String message = "AppFrameIsNull";
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (position == null)
        {
            String message = Logging.getMessage("nullValue.PositionIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (title == null)
        {
            String message = Logging.getMessage("nullValue.StringIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

    	
		return null;
    	
    }
    
    public static ContentAnnotation createTridasAnnotation(WWJPanel mapPanel, Position position, String title, ITridas entity)
    {
        if (mapPanel == null)
        {
            String message = "AppFrameIsNull";
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (position == null)
        {
            String message = Logging.getMessage("nullValue.PositionIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (title == null)
        {
            String message = Logging.getMessage("nullValue.StringIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (entity == null)
        {
            String message = Logging.getMessage("nullValue.SourceIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        TridasAnnotation annotation = new TridasAnnotation(position, entity);
        annotation.setAlwaysOnTop(true);

        TridasAnnotationController controller = new TridasAnnotationController(mapPanel.getWwd(), annotation, entity);

        
        return new TridasContentAnnotation(mapPanel, annotation, controller, entity);
    }
    

    protected RenderableLayer getAnnotationLayer()
    {
    	return this.annotationLayer;
    }
	

    public static class ContentAnnotation implements ActionListener
    {
        protected WWJPanel mapPanel;
        protected TridasAnnotation annotation;
        protected TridasAnnotationController controller;

        public ContentAnnotation(WWJPanel mapPanel, TridasAnnotation annotation, TridasAnnotationController controller)
        {
            this.mapPanel = mapPanel;
            this.annotation = annotation;
            this.annotation.addActionListener(this);
            this.controller = controller;
        }

        public WWJPanel getMapPanel()
        {
            return this.mapPanel;
        }

        public TridasAnnotation getAnnotation()
        {
            return this.annotation;
        }

        public TridasAnnotationController getController()
        {
            return this.controller;
        }

        public void actionPerformed(ActionEvent e)
        {
            if (e == null)
                return;

            if (e.getActionCommand() == AVKey.CLOSE)
            {
                this.getMapPanel().closeResource(this);
            }
        }

        public void detach()
        {
            this.getController().setEnabled(false);

            RenderableLayer layer = this.getMapPanel().getAnnotationLayer();
            layer.removeRenderable(this.getAnnotation());
        }

        public void attach()
        {
            this.getController().setEnabled(true);

            RenderableLayer layer = this.mapPanel.getAnnotationLayer();
            layer.removeRenderable(this.getAnnotation());
            layer.addRenderable(this.getAnnotation());
        }
    }
	
    public static class TridasContentAnnotation extends ContentAnnotation
    {

    	ITridas entity;
    	
		public TridasContentAnnotation(WWJPanel mapPanel,
				TridasAnnotation annotation,
				TridasAnnotationController controller,
				ITridas entity) {
			super(mapPanel, annotation, controller);
			this.entity=entity;
		}
    	
    }

	@Override
	public void selected(SelectEvent e) {
        
		
		log.debug("Map item selected");
		
		if (e == null)
            return;

        PickedObject topPickedObject = e.getTopPickedObject();

        if (e.getEventAction() == SelectEvent.LEFT_PRESS)
        {
            if (topPickedObject != null && topPickedObject.getObject() instanceof Marker)
            {
            	Marker selected = (Marker) topPickedObject.getObject();
                this.openResource(selected);
            }
            else if (topPickedObject != null && topPickedObject.getObject() instanceof ITRDBMarker)
            {
            	
            }
        }
		
	}
	
    public static void insertBeforePlacenames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }
    
    public static void insertBeforeCompass(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the compass.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof CompassLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
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
    public void addKMLLayer(KMLRoot kmlRoot)
    {
        // Create a KMLController to adapt the KMLRoot to the World Wind renderable interface.
        KMLController kmlController = new KMLController(kmlRoot);

        // Adds a new layer containing the KMLRoot to the end of the WorldWindow's layer list. This
        // retrieves the layer name from the KMLRoot's DISPLAY_NAME field.
        RenderableLayer layer = new RenderableLayer();
        layer.setName((String) kmlRoot.getField(AVKey.DISPLAY_NAME));
        layer.addRenderable(kmlController);
        this.getWwd().getModel().getLayers().add(layer);

        // Adds a new layer tree node for the KMLRoot to the on-screen layer tree, and makes the new node visible
        // in the tree. This also expands any tree paths that represent open KML containers or open KML network
        // links.
       // KMLLayerTreeNode layerNode = new KMLLayerTreeNode(layer, kmlRoot);
        //wwd.getModel().getLayers().addLayer(layerNode);
        //this.layerTree.makeVisible(layerNode.getPath());
        //layerNode.expandOpenContainers(this.layerTree);

        // Listens to refresh property change events from KML network link nodes. Upon receiving such an event this
        // expands any tree paths that represent open KML containers. When a KML network link refreshes, its tree
        // node replaces its children with new nodes created from the refreshed content, then sends a refresh
        // property change event through the layer tree. By expanding open containers after a network link refresh,
        // we ensure that the network link tree view appearance is consistent with the KML specification.
       /* layerNode.addPropertyChangeListener(AVKey.RETRIEVAL_STATE_SUCCESSFUL, new PropertyChangeListener()
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
                         //   ((KMLNetworkLinkTreeNode) event.getSource()).expandOpenContainers(layerTree);
                            getWwd().redraw();
                        }
                    });
                }
            }
        });*/
    }
}
    
	

