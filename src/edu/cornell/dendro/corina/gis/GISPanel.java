package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.examples.ApplicationTemplate;
import gov.nasa.worldwind.examples.ClickAndGoSelectListener;
import gov.nasa.worldwind.examples.GazetteerPanel;
import gov.nasa.worldwind.examples.util.LayerManagerLayer;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.WWIcon;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.StatusBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.tridas.interfaces.ITridas;

/**
 * Map panel containing a WorldWind Java canvas
 * 
 * @author peterbrewer
 *
 */
public class GISPanel extends JPanel implements SelectListener{

	private static final long serialVersionUID = 6769486491009238118L;
		protected WorldWindowGLCanvas wwd;
        protected StatusBar statusBar;
        protected TridasMarker selectedMarker;
        protected RenderableLayer annotationLayer;
        protected TridasAnnotation annotation;
        private ArrayList<String> visibleLayers = new ArrayList<String>();
        public GISPanel(Dimension canvasSize, boolean includeStatusBar, MarkerLayer ly)
        {
            super(new BorderLayout());
        	setupGui(canvasSize, includeStatusBar);
        	addLayer(ly);
            this.annotationLayer = new RenderableLayer();
            annotationLayer.setName("Popup information");
            ApplicationTemplate.insertBeforePlacenames(this.getWwd(), this.annotationLayer);

        	
        	this.getWwd().addSelectListener(this);
        	
        	
        	

        }

        public void addLayer(MarkerLayer layer)
        {
        	ApplicationTemplate.insertBeforeCompass(this.getWwd(), layer);
        	visibleLayers.add(layer.getName());
        }
        
        private void failedReq()
        {
        	JLabel failed = new JLabel();
        	failed.setText("This computer does not meet minimum graphics requirements");
        	this.add(failed, BorderLayout.NORTH);
        }
        
        private void setupGui(Dimension canvasSize, boolean includeStatusBar)
        {
        	
            this.wwd = this.createWorldWindow();
            this.wwd.setPreferredSize(canvasSize);
            
            // Create the default model as described in the current worldwind properties.
            Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
            this.wwd.setModel(m);

            
            
            // Setup a select listener for the worldmap click-and-go feature
            this.wwd.addSelectListener(new ClickAndGoSelectListener(this.getWwd(), WorldMapLayer.class));

            this.wwd.addRenderingExceptionListener(new RenderingExceptionListener()
            {
                public void exceptionThrown(Throwable t)
                {
                    if (t instanceof WWAbsentRequirementException)
                    {
                        String message = "Computer does not meet minimum graphics requirements.\n";
                        message += "Please install up-to-date graphics driver and try again.\n";
                        message += "Reason: " + t.getMessage();

                        JOptionPane.showMessageDialog(null, message, "Unable to Start Program",
                            JOptionPane.ERROR_MESSAGE);
                        failedReq();
                        return;
                    }
                }
            });
            
            
            this.add(this.wwd, BorderLayout.CENTER);
           
            
            if (includeStatusBar)
            {
                this.statusBar = new StatusBar();
                this.add(statusBar, BorderLayout.PAGE_END);
                this.statusBar.setEventSource(wwd);
            }
            
            // Add the layer manager layer to the model layer list
            
            CorinaLayerManagerLayer layermanager = new CorinaLayerManagerLayer(getWwd(), visibleLayers);
            layermanager.setName("Show/hide layer list");
            layermanager.setMinimized(true);
            getWwd().getModel().getLayers().add(layermanager);
            

            
        }
        
        protected WorldWindowGLCanvas createWorldWindow()
        {
            return new WorldWindowGLCanvas();
        }

        public WorldWindowGLCanvas getWwd()
        {
            return wwd;
        }

        public StatusBar getStatusBar()
        {
            return statusBar;
        }


        public void selected(SelectEvent e)
        {
            if (e == null)
                return;

            PickedObject topPickedObject = e.getTopPickedObject();

           /* if (e.getEventAction() == SelectEvent.LEFT_PRESS)
            {
                if (topPickedObject != null && topPickedObject.getObject() instanceof TridasMarker)
                {
                	TridasMarker selected = (TridasMarker) topPickedObject.getObject();
                    this.highlight(selected);
                }
                else
                {
                    this.highlight(null);
                }
            }*/
            if (e.getEventAction() == SelectEvent.LEFT_PRESS)
            {
                if (topPickedObject != null && topPickedObject.getObject() instanceof TridasMarker)
                {
                	TridasMarker selected = (TridasMarker) topPickedObject.getObject();
                    this.highlight(selected);
                    this.openResource(selected);
                }
            }

        }
        
        public void highlight(TridasMarker marker)
        {
            if (this.selectedMarker == marker)
                return;

            if (this.selectedMarker != null)
            {
                this.selectedMarker.setHighlighted(false);
                this.selectedMarker = null;
            }

            if (marker != null)
            {
                this.selectedMarker = marker;
                this.selectedMarker.setHighlighted(true);
            }

            this.getWwd().redraw();            
        }

        protected void closeResource(ContentAnnotation content)
        {
            if (content == null)
                return;

            content.detach();
        }
        
        protected void openResource(TridasMarker marker)
        {
            if (marker == null)
                return;

            ContentAnnotation content = this.createContent(marker.getPosition(), marker.getEntity());

            if (content != null)
            {
                content.attach();
            }
        }
        
        protected ContentAnnotation createContent(Position position, ITridas entity)
        {
            return createContentAnnotation(this, position, entity);
        }
        
        public static ContentAnnotation createContentAnnotation(GISPanel mapPanel, Position position, ITridas entity)
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


            return createTridasAnnotation(mapPanel, position, "blah", entity);

           
        }
        
        public static ContentAnnotation createTridasAnnotation(GISPanel mapPanel, Position position, String title,
                ITridas entity)
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
	        protected GISPanel mapPanel;
	        protected TridasAnnotation annotation;
	        protected TridasAnnotationController controller;

	        public ContentAnnotation(GISPanel mapPanel, TridasAnnotation annotation, TridasAnnotationController controller)
	        {
	            this.mapPanel = mapPanel;
	            this.annotation = annotation;
	            this.annotation.addActionListener(this);
	            this.controller = controller;
	        }

	        public GISPanel getMapPanel()
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
	    	
			public TridasContentAnnotation(GISPanel mapPanel,
					TridasAnnotation annotation,
					TridasAnnotationController controller,
					ITridas entity) {
				super(mapPanel, annotation, controller);
				this.entity=entity;
			}
	    	
	    }
		
}
