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

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.examples.ApplicationTemplate;
import gov.nasa.worldwind.examples.ClickAndGoSelectListener;
import gov.nasa.worldwind.examples.util.LayerManagerLayer;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.StatusBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
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
        protected Marker selectedMarker;
        protected RenderableLayer annotationLayer;
        protected GlobeAnnotation annotation;
        protected Boolean isGrfxRetest= false;
        protected Boolean failedRetest;
        protected ViewControlsLayer viewControlsLayer;
        protected Boolean isMiniMap = false;
        
        
        public Boolean isMiniMap()
        {
        	return isMiniMap;
        }
        
        public void setIsMiniMap(Boolean b)
        {
        	isMiniMap = b;
        }
        
        public GISPanel(Dimension canvasSize, boolean includeStatusBar, MarkerLayer ly)
        {
        	super(new BorderLayout());
        
        	
        	try{
	        	if(App.prefs.getBooleanPref(PrefKey.OPENGL_FAILED, false))
	        	{
	        		wwd=null;
	        		failedReq();
	        		return;
	        	}
	        	            
	        	setupGui(canvasSize, includeStatusBar);
	        	addLayer(ly);
	        	
	            this.annotationLayer = new RenderableLayer();
	            annotationLayer.setName("Popup information");
	            ApplicationTemplate.insertBeforePlacenames(this.getWwd(), this.annotationLayer);
	
	        	this.getWwd().addSelectListener(this);
	        	
	        	            
	            // Create and install the view controls layer and register a controller for it with the World Window.
	            ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
	            viewControlsLayer.setPosition(AVKey.NORTHEAST);
	            ApplicationTemplate.insertBeforeCompass(getWwd(), viewControlsLayer);
	            viewControlsLayer.setLayout(AVKey.VERTICAL);
	            this.getWwd().addSelectListener(new ViewControlsSelectListener(this.getWwd(), viewControlsLayer));
	            
	            CompassLayer compass = (CompassLayer) this.getWwd().getModel().getLayers().getLayerByName("Compass");
	            compass.setPosition(AVKey.SOUTHEAST);
	            compass.setLocationOffset(new Vec4(0, 20));
	            
	            WorldMapLayer overview = (WorldMapLayer) this.getWwd().getModel().getLayers().getLayerByName("World Map");
	            overview.setPosition(AVKey.SOUTHWEST);
	            overview.setResizeBehavior(AVKey.RESIZE_STRETCH);
	            
	            ScalebarLayer scale = (ScalebarLayer) this.getWwd().getModel().getLayers().getLayerByName("Scale bar");
	            
	
	        	overview.setEnabled(isMiniMap);
	        	compass.setEnabled(isMiniMap);
	        	scale.setEnabled(isMiniMap);
	        	
	        	this.getWwd().addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent evt) {
						System.out.println(evt.getButton());
						if(evt.getButton() == MouseEvent.BUTTON3)
						{
		        	         // Get clicked position on the globe.
		        	         Position clickedPosition = getWwd().getCurrentPosition();
		        	         double latitude = clickedPosition.getLatitude().getDegrees();
		           	         double longitude = clickedPosition.getLongitude().getDegrees();
							
							 GISPointDisplay dialog = new GISPointDisplay(latitude,longitude);
							 dialog.setLocationRelativeTo(null);
							 dialog.setVisible(true);
						}
						
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
	        		
	        	});
	        	
	        	
        	
			} catch (UnsatisfiedLinkError e)
			{
				Alert.error("Critical Error", 
						"There has been a fatal error accessing native 3D graphics libraries\n" +
						"on your computer.\n\n" +
						"The most common cause is because you are running a 32 bit version\n" +
						"of Tellervo on a 64 bit version of Java (or vice versa). Alternatively\n" +
						"there may have been a problem with the installer. The 3D mapping\n" +
						"functionality in Tellervo will now be disabled.\n\n" +
						"Please see the manual or contact the developers for more information.");
				App.prefs.setBooleanPref(PrefKey.OPENGL_FAILED, true);
			}

        }

        public Boolean isGrfxRetest()
        {
        	return this.isGrfxRetest;
        }
        
        public Boolean getFailedRetest()
        {
        	return this.failedRetest;
        }
        
        public void setIsGrfxRetest(Boolean b)
        {
        	this.isGrfxRetest = b;
        }

 
        public void addLayer(MarkerLayer layer)
        {
        	ApplicationTemplate.insertBeforePlacenames(this.getWwd(), layer);
        }
        
        public void addLayer(Layer layer)
        {
        	ApplicationTemplate.insertBeforeCompass(this.getWwd(), layer);
        }
        
        private void failedReq()
        {
        	this.failedRetest = true;
        	App.prefs.setBooleanPref(PrefKey.OPENGL_FAILED, true);
        	GrfxWarning warn = new GrfxWarning(this);
        	//warn.btnRetry.setVisible(false);
        	this.add(warn, BorderLayout.CENTER);
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
                	GISPanel.this.remove((Component) wwd);
                	GISPanel.this.failedReq();
                    return;
                        
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
            
            if(isMiniMap)
            {
	            TellervoLayerManagerLayer layermanager = new TellervoLayerManagerLayer(getWwd(), getWwd().getModel().getLayers());
	            layermanager.setName("Show/hide layer list");
	            layermanager.setMinimized(true);
	            layermanager.setPosition(AVKey.NORTHWEST);
	            getWwd().getModel().getLayers().add(layermanager);
            }

            
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

            if (e.getEventAction() == SelectEvent.LEFT_PRESS)
            {
                if (topPickedObject != null && topPickedObject.getObject() instanceof Marker)
                {
                	Marker selected = (Marker) topPickedObject.getObject();
                    this.highlight(selected);
                    this.openResource(selected);
                }
                else if (topPickedObject != null && topPickedObject.getObject() instanceof ITRDBMarker)
                {
                	
                }
            }

        }
        
        public void highlight(Marker marker)
        {
            if (this.selectedMarker == marker)
                return;

            if (this.selectedMarker != null)
            {
                this.selectedMarker = null;
            }

            if (marker != null)
            {
                this.selectedMarker = marker;
            }

            this.getWwd().redraw();            
        }

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

        public static ContentAnnotation createITRDBAnnotation(GISPanel mapPanel, Position position, String title, ITRDBMarker marker)
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
        
        public static ContentAnnotation createTridasAnnotation(GISPanel mapPanel, Position position, String title, ITridas entity)
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
