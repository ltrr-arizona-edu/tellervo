package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.kml.KMLViewer.AppFrame;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gis2.WWJPanel;
import org.tellervo.desktop.ui.Builder;

public class MapKMLLayerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	protected WWJPanel wwjPanel;
	
	public MapKMLLayerAction(FullEditor editor) {
        super("KML Layer", Builder.getIcon("map.png", 22));
		putValue(SHORT_DESCRIPTION, "KML Layer");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
	
		
		
		final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("KML/KMZ File", "kml", "kmz"));
		
		try
        {
            
            
			int status = fileChooser.showOpenDialog(wwjPanel);
            if (status == JFileChooser.APPROVE_OPTION)
            {
                for (File file : fileChooser.getSelectedFiles())
                {
                    new WorkerThread(file, wwjPanel).start();
                }
            }
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }
		
	}

 class WorkerThread extends Thread
{
    /** Indicates the source of the KML file loaded by this thread. Initialized during construction. */
    protected Object kmlSource;
	private WWJPanel wwjPanel;
    /** Indicates the <code>AppFrame</code> the KML file content is displayed in. Initialized during construction. */
    

    /**
     * Creates a new worker thread from a specified <code>kmlSource</code> and <code>appFrame</code>.
     *
     * @param kmlSource the source of the KML file to load. May be a {@link File}, a {@link URL}, or an {@link
     *                  java.io.InputStream}, or a {@link String} identifying a file path or URL.
     * @param wwjPanel  the <code>AppFrame</code> in which to display the KML source.
     */
    public WorkerThread(Object kmlSource, WWJPanel wwjPanel)
    {
        this.kmlSource = kmlSource;
        this.wwjPanel = wwjPanel;
    }

    /**
     * Loads this worker thread's KML source into a new <code>{@link gov.nasa.worldwind.ogc.kml.KMLRoot}</code>,
     * then adds the new <code>KMLRoot</code> to this worker thread's <code>AppFrame</code>. The
     * <code>KMLRoot</code>'s <code>AVKey.DISPLAY_NAME</code> field contains a display name created from either the
     * KML source or the KML root feature name.
     * <p/>
     * If loading the KML source fails, this prints the exception and its stack trace to the standard error stream,
     * but otherwise does nothing.
     */
    public void run()
    {
        try
        {
            KMLRoot kmlRoot = this.parse();

            // Set the document's display name
            kmlRoot.setField(AVKey.DISPLAY_NAME, formName(this.kmlSource, kmlRoot));

            // Schedule a task on the EDT to add the parsed document to a layer
            final KMLRoot finalKMLRoot = kmlRoot;
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    wwjPanel.addKMLLayer(finalKMLRoot);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Parse the KML document.
     *
     * @return The parsed document.
     *
     * @throws IOException        if the document cannot be read.
     * @throws XMLStreamException if document cannot be parsed.
     */
    protected KMLRoot parse() throws IOException, XMLStreamException
    {
        // KMLRoot.createAndParse will attempt to parse the document using a namespace aware parser, but if that
        // fails due to a parsing error it will try again using a namespace unaware parser. Note that this second
        // step may require the document to be read from the network again if the kmlSource is a stream.
        return KMLRoot.createAndParse(this.kmlSource);
    }
    
    protected static String formName(Object kmlSource, KMLRoot kmlRoot)
    {
        KMLAbstractFeature rootFeature = kmlRoot.getFeature();

        if (rootFeature != null && !WWUtil.isEmpty(rootFeature.getName()))
            return rootFeature.getName();

        if (kmlSource instanceof File)
            return ((File) kmlSource).getName();

        if (kmlSource instanceof URL)
            return ((URL) kmlSource).getPath();

        if (kmlSource instanceof String && WWIO.makeURL((String) kmlSource) != null)
            return WWIO.makeURL((String) kmlSource).getPath();

        return "KML Layer";
    }
}



