package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwindx.examples.Shapefiles.AppFrame;
import gov.nasa.worldwindx.examples.Shapefiles.WorkerThread;
import gov.nasa.worldwindx.examples.util.OpenStreetMapShapefileLoader;
import gov.nasa.worldwindx.examples.util.ShapefileLoader;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gis2.WWJPanel;
import org.tellervo.desktop.ui.Builder;

public class MapShapefileLayerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	
	public MapShapefileLayerAction(FullEditor editor) {
        super("Shapefile Layer", Builder.getIcon("map.png", 22));
		putValue(SHORT_DESCRIPTION, "Shapefile Layer");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileNameExtensionFilter("ESRI Shapefile", "shp"));
		fc.setAcceptAllFileFilterUsed(false);
			
		int retVal = fc.showOpenDialog(editor);
        if (retVal != JFileChooser.APPROVE_OPTION)
            return;

        Thread t = new WorkerThread(fc.getSelectedFile(), editor.getMapPanel());
        t.start();
        ((Component) editor.getMapPanel().getWwd()).setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        
	}
	
	
	class WorkerThread extends Thread
    {
        protected Object source;
        protected WWJPanel wwjPanel;

        public WorkerThread(Object source, WWJPanel wwjpanel)
        {
            this.source = source;
            this.wwjPanel = wwjpanel;
        }

        public void run()
        {
            try
            {
                final List<Layer> layers = this.makeShapefileLayers();
                for (int i = 0; i < layers.size(); i++)
                {
                    String name = this.makeDisplayName(this.source);
                    layers.get(i).setName(i == 0 ? name : name + "-" + Integer.toString(i));
                    //layers.get(i).setPickEnabled(this.appFrame.pickCheck.isSelected());
                }

                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        for (Layer layer : layers)
                        {
                            WWJPanel.insertBeforePlacenames(wwjPanel.getWwd(), layer);
                            wwjPanel.getLayersList().add(layer);
                        }

                        wwjPanel.layerPanel.update(wwjPanel.getWwd());
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        ((Component) wwjPanel.getWwd()).setCursor(Cursor.getDefaultCursor());
                    }
                });
            }
        }

        protected List<Layer> makeShapefileLayers()
        {
            if (OpenStreetMapShapefileLoader.isOSMPlacesSource(this.source))
            {
                Layer layer = OpenStreetMapShapefileLoader.makeLayerFromOSMPlacesSource(source);
                List<Layer> layers = new ArrayList<Layer>();
                layers.add(layer);
                return layers;
            }
            else
            {
                ShapefileLoader loader = new ShapefileLoader();
                return loader.createLayersFromSource(this.source);
            }
        }

        protected String makeDisplayName(Object source)
        {
            String name = WWIO.getSourcePath(source);
            if (name != null)
                name = WWIO.getFilename(name);
            if (name == null)
                name = "Shapefile";

            return name;
        }
    }


}
