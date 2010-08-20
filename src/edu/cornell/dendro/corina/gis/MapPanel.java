package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.examples.ApplicationTemplate;
import gov.nasa.worldwind.examples.ClickAndGoSelectListener;
import gov.nasa.worldwind.examples.ApplicationTemplate.AppFrame;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.formats.gpx.GpxReader;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.StatusBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class MapPanel extends JPanel {

	private static final long serialVersionUID = 6769486491009238118L;
		protected WorldWindowGLCanvas wwd;
        protected StatusBar statusBar;
        private Boolean failedRequirements = false;
        private MarkerLayer layer = new MarkerLayer();

        public MapPanel(Dimension canvasSize, boolean includeStatusBar, Position pos)
        {
        	super(new BorderLayout());
        	ArrayList<Position> positions = new ArrayList<Position>();
        	positions.add(pos);
        	setupGui(canvasSize, includeStatusBar, positions);
        }
        
        public MapPanel(Dimension canvasSize, boolean includeStatusBar, ArrayList<Position> positions)
        {
            super(new BorderLayout());
        	setupGui(canvasSize, includeStatusBar, positions);

        }

        private void setupGui(Dimension canvasSize, boolean includeStatusBar, ArrayList<Position> positions)
        {
            this.wwd = this.createWorldWindow();
            this.wwd.setPreferredSize(canvasSize);
            
            // Create the default model as described in the current worldwind properties.
            Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
            this.wwd.setModel(m);

            
            
            
            initMarkerLayer(positions);
            ApplicationTemplate.insertBeforeCompass(this.getWwd(), layer);
            
            // Setup a select listener for the worldmap click-and-go feature
            this.wwd.addSelectListener(new ClickAndGoSelectListener(this.getWwd(), WorldMapLayer.class));

            this.wwd.addRenderingExceptionListener(new RenderingExceptionListener()
            {
                public void exceptionThrown(Throwable t)
                {
                    if (t instanceof WWAbsentRequirementException)
                    {
                        String message = "Uhoh... Computer does not meet minimum graphics requirements.\n";
                        message += "Please install up-to-date graphics driver and try again.\n";
                        message += "Reason: " + t.getMessage() + "\n";
                        message += "This program will end when you press OK.";

                        JOptionPane.showMessageDialog(null, message, "Unable to Start Program",
                            JOptionPane.ERROR_MESSAGE);
                        failedRequirements = true;
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
        }
        
        private void initMarkerLayer(ArrayList<Position> positions)
        {
        	
        	
            BasicMarkerAttributes attrs =
                new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CYLINDER, 0.6d);

            ArrayList<Marker> markers = new ArrayList<Marker>();
            
            for(Position pos: positions)
            {
            	markers.add(new BasicMarker(pos, attrs));
            }

            layer = new MarkerLayer(markers);
            layer.setOverrideMarkerElevation(true);
            layer.setElevation(0);
            layer.setEnablePickSizeReturn(true);        
        
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
    
	
}
