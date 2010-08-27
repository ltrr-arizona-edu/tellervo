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
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.WWIcon;
import gov.nasa.worldwind.util.StatusBar;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Map panel containing a WorldWind Java canvas
 * 
 * @author peterbrewer
 *
 */
public class MapPanel extends JPanel implements SelectListener{

	private static final long serialVersionUID = 6769486491009238118L;
		protected WorldWindowGLCanvas wwd;
        protected StatusBar statusBar;
        
        public MapPanel(Dimension canvasSize, boolean includeStatusBar, MarkerLayer ly)
        {
            super(new BorderLayout());
        	setupGui(canvasSize, includeStatusBar);
        	addLayer(ly);
        	this.getWwd().addSelectListener(this);
        	

        }

        public void addLayer(MarkerLayer layer)
        {
        	ApplicationTemplate.insertBeforeCompass(this.getWwd(), layer);
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

		@Override
		public void selected(SelectEvent e) {
           
			if (e == null)
                return;

            PickedObject topPickedObject = e.getTopPickedObject();
			

            if (e.getEventAction() == SelectEvent.LEFT_DOUBLE_CLICK)
            {
                if (topPickedObject != null && topPickedObject.getObject() instanceof WWIcon)
                {

                }
                else
                {

                }
            }
		}
    
	
}
