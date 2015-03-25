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
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.LayerPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gis.ITRDBMarker;
import org.tellervo.desktop.gis.TridasAnnotation;
import org.tellervo.desktop.gis.TridasAnnotationController;
import org.tellervo.desktop.gis.TridasMarker;
import org.tellervo.desktop.sample.Sample;
import org.tridas.interfaces.ITridas;

public class WWJPanel extends JPanel  implements SelectListener{

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(WWJPanel.class);

	private JPanel mainPanel;
	
	
	protected WorldWindow wwd;
	protected LayerPanel layerPanel;
	protected StatusBar statusBar;
	protected LayerList layersList;
	private RenderableLayer annotationLayer;

	public WWJPanel() {
		super(new BorderLayout());
		init();

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

        // Create two models and pass them the shared layers.
        Model model = new BasicModel();
        model.setGlobe(earth);
        model.setLayers(layersList);
        wwd.setModel(model);

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
	}
	
	public WorldWindow getWwd() {
		return wwd;
	}

	
	public void zoomToSample(Sample s)
	{
		TridasEntityLayer layer = getWorkspaceSeriesLayer();
		
		Marker marker = layer.getMarkerForSample(s);

		if(marker==null) return;
		
        wwd.getView().stopAnimations();
        wwd.getView().goTo(marker.getPosition(), 500000d);
        return;
		
		
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
    
	
}
