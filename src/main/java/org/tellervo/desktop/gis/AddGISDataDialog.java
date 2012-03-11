package org.tellervo.desktop.gis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.widgets.TellervoCodePanel;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwind.util.WWUtil;

public class AddGISDataDialog extends JDialog implements ActionListener, TridasSelectListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(AddGISDataDialog.class);
	private GISFrame parent;
	private GISPanel wwMapPanel;
	private JComboBox cboAddType;
	private JButton btnGo;
	private JPanel pickerPanelHolder;
	private TridasEntityPickerPanel pickerPanel;
	
	public enum AddLayerType{
		ALL_OBJECTS("All objects in database"),
		CORINA_ENTITY("One specific entity from database"),
		ELEMENTS_FOR_OBJECT("All elements of an object"),
		ITRDB_SITES("All ITRDB sites"),
		SHAPEFILE("ESRI Shapefile"),
		KML("Google Earth KML/KMZ file");
		
		private String name;
		
		private AddLayerType(String n)
		{
			name = n; 
		}
		public String getName()
		{
			return name;
		}
		
		public String toString()
		{
			return name;
			
		}
	};
	
	
	
	/**
	 * Create the panel.
	 * 
	 * @param worldWindowGLCanvas 
	 */
	public AddGISDataDialog(GISFrame parent) {
		setTitle("Add data layer");		
		setIconImage(Builder.getApplicationIcon());
		//setModal(true);
		
		
		this.wwMapPanel = parent.wwMapPanel;
		this.parent = parent;
		getContentPane().setLayout(new MigLayout("", "[::128px][45.00px][grow]", "[][][grow][]"));
		
		JLabel lblIcon = new JLabel("");
		lblIcon.setIcon(Builder.getIcon("layers.png", 128));		
		getContentPane().add(lblIcon, "cell 0 0 1 3,alignx center, aligny top");
		
		JLabel lblAddLayer = new JLabel("Add:");
		getContentPane().add(lblAddLayer, "cell 1 0,alignx trailing");
		
		cboAddType = new JComboBox();
		
		cboAddType.setModel(new DefaultComboBoxModel(AddLayerType.values()));
		getContentPane().add(cboAddType, "cell 2 0,growx");		
		cboAddType.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				setGUIForAddType();
				
			}
			
		});
				
		pickerPanelHolder = new JPanel();
		pickerPanelHolder.setBackground(Color.red);
		pickerPanelHolder.setLayout(new BorderLayout());
		
		getContentPane().add(pickerPanelHolder, "cell 2 1,growx");
		this.setPickerPanelVisible(false);
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, "cell 0 3 3 1,alignx right,growy");
		
		btnGo = new JButton("Add");
		btnGo.setActionCommand("add");
		btnGo.addActionListener(this);
		buttonPanel.add(btnGo);
		
		JButton btnCancel = new JButton("Close");
		btnCancel.setActionCommand("cancel");
		btnCancel.addActionListener(this);
		buttonPanel.add(btnCancel);
		
		pack();
		this.setLocationRelativeTo(parent);
	}

	
	private void setPickerPanelVisible(Boolean b)
	{
		
		if(b)
		{
			pickerPanelHolder.removeAll();
			pickerPanelHolder.add(pickerPanel, BorderLayout.CENTER);
		}
		
		pickerPanelHolder.setVisible(b);
	}
	
	private void setGUIForAddType()
	{
		AddLayerType selectedType = (AddLayerType) cboAddType.getSelectedItem();
		
		// Alter 'go' button text
		if(selectedType.equals(AddLayerType.SHAPEFILE) ||
		   selectedType.equals(AddLayerType.KML)
			)
		{
			btnGo.setText("Browse");
		}
		else
		{
			btnGo.setText("Add");
		}
		
		// Show or hide picker panel accordingly
		if(selectedType.equals(AddLayerType.CORINA_ENTITY))
		{
			pickerPanel = new TridasEntityPickerPanel(this, TridasObject.class, EntitiesAccepted.ALL);
			((TridasEntityPickerPanel) pickerPanel).setMinimalGui(true);
			((TridasEntityPickerPanel) pickerPanel).addTridasSelectListener(this);
			
			setPickerPanelVisible(true);
		}
		else if (selectedType.equals(AddLayerType.ELEMENTS_FOR_OBJECT))
		{
			pickerPanel = new TridasEntityPickerPanel(this, TridasObject.class, EntitiesAccepted.SPECIFIED_ENTITY_ONLY);
			((TridasEntityPickerPanel) pickerPanel).setMinimalGui(true);
			((TridasEntityPickerPanel) pickerPanel).addTridasSelectListener(this);
			setPickerPanelVisible(true);
		}
		else
		{
			setPickerPanelVisible(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("add"))
		{
			if(cboAddType.getSelectedItem().equals(AddLayerType.ITRDB_SITES))
			{
				addITRDBSites();
				dispose();
			}
			else if(cboAddType.getSelectedItem().equals(AddLayerType.SHAPEFILE))
			{
	            Boolean success = addShapefile();
	            if(success) dispose();
			}
			else if (cboAddType.getSelectedItem().equals(AddLayerType.ALL_OBJECTS))
			{
				addAllObjects();
				dispose();
			}
			else if ((cboAddType.getSelectedItem().equals(AddLayerType.CORINA_ENTITY)) ||
					(cboAddType.getSelectedItem().equals(AddLayerType.ELEMENTS_FOR_OBJECT))
					)
			{
				((TridasEntityPickerPanel) pickerPanel).forceFireEvent();
				
			}
			else if (cboAddType.getSelectedItem().equals(AddLayerType.KML))
			{
				 Boolean success = addKMLFile();
				 if(success) dispose();
			}
		}
		else if (event.getActionCommand().equals("cancel"))
		{
			dispose();
		}
	
	}
	
	private void addITRDBSites()
	{
		try{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			wwMapPanel.addLayer(ITRDBMarkerLayerBuilder.createITRDBLayer());
			parent.layerPanel.update(parent.wwMapPanel.getWwd());
			
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private void addAllObjects()
	{
		try{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			wwMapPanel.addLayer(TridasMarkerLayerBuilder.getMarkerLayerForAllSites());
			parent.layerPanel.update(parent.wwMapPanel.getWwd());
			
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private Boolean addKMLFile()
	{
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Google Earth (*.kml; *.kmz)", "kml", "kmz"));

        int status = fileChooser.showOpenDialog(wwMapPanel);
        if (status == JFileChooser.APPROVE_OPTION)
        {
        	wwMapPanel.getWwd().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            new KMLWorkerThread(fileChooser.getSelectedFile(), parent).start();
            return true;
        }
        else
        {
        	return false;
        }

	}
	
	
    private Boolean addShapefile()
    {

		JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("ESRI Shapefile", "shp"));
        
        int retVal = fc.showOpenDialog(wwMapPanel);
        if (retVal != JFileChooser.APPROVE_OPTION)
            return false;

        Thread t = new ShapefileWorkerThread(fc.getSelectedFile(), parent);
        t.start();
        wwMapPanel.getWwd().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        return true;
    }
    
	

	@Override
	public void entitySelected(TridasSelectEvent event) {
		
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ArrayList<ITridas> entitylist = event.getEntityList();

			// Nothing found so return 
			if(entitylist==null) return;
			
			if(entitylist.size()==0)
			{
				Alert.error("No matches", "No records were found for the specified code");
				return;
			}
			
			// Build a marker layer
			TridasMarkerLayerBuilder mlb = new TridasMarkerLayerBuilder();
			
			// Extract object from event
			TridasObject obj = null;
			if(entitylist.get(0) instanceof TridasObject)
			{
				obj = (TridasObject) entitylist.get(0);
			}
			else
			{
				return;
			}
			
			if(cboAddType.getSelectedItem().equals(AddLayerType.CORINA_ENTITY))
			{
				if(TridasUtils.getElementList(obj).size()>0)
				{
					TridasElement el = TridasUtils.getElementList(obj).get(0);
					mlb.addMarkerForTridasElement(el);
					mlb.setName(obj.getTitle()+"-"+el.getTitle());
				}
				else
				{
					mlb.addMarkerForTridasObject((TridasObject)entitylist.get(0));
					mlb.setName(obj.getTitle());
				}
			}
			else if (cboAddType.getSelectedItem().equals(AddLayerType.ELEMENTS_FOR_OBJECT))
			{
				SearchParameters search = null;
				
				search = new SearchParameters(SearchReturnObject.ELEMENT);
				search.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, obj.getIdentifier().getValue());
				
				// Do the search 
				EntitySearchResource<TridasElement> searchResource = new EntitySearchResource<TridasElement>(search, TridasElement.class);
				
				
				searchResource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.STANDARD);
				TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(parent, searchResource);
				searchResource.query();
				dlg.setVisible(true);
				
				
				if(!dlg.isSuccessful()) 
				{
					// Search failed
					new Bug(dlg.getFailException());
					return;
				} 
				else 
				{
					// Search successful
					List<TridasElement> foundEntities = (List<TridasElement>) searchResource.getAssociatedResult();
					
					for(TridasElement el : foundEntities)
					{
						mlb.addMarkerForTridasElement(el);
					}
					mlb.setName("Elements for "+obj.getTitle());
					
				}

			}
										
			if(!mlb.containsMarkers())
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				Alert.error("No records found", "No location records were found for the specified code");
				return;
			}
			else
			{				
				wwMapPanel.addLayer(mlb.getMarkerLayer());
				parent.layerPanel.update(parent.wwMapPanel.getWwd());
			}
			
			
		} catch (Exception e) {
			log.error("Error loading points from database");
			e.printStackTrace();
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		
	}

    /** A <code>Thread</code> that loads a KML file and displays it in an <code>AppFrame</code>. */
    public static class KMLWorkerThread extends Thread
    {
        /** Indicates the source of the KML file loaded by this thread. Initialized during construction. */
        protected Object kmlSource;
        /** Indicates the <code>AppFrame</code> the KML file content is displayed in. Initialized during construction. */
        protected GISFrame gisFrame;

        /**
         * Creates a new worker thread from a specified <code>kmlSource</code> and <code>appFrame</code>.
         *
         * @param kmlSource the source of the KML file to load. May be a {@link File}, a {@link URL}, or an {@link
         *                  java.io.InputStream}, or a {@link String} identifying a file path or URL.
         * @param gisFrame  the <code>AppFrame</code> in which to display the KML source.
         */
        public KMLWorkerThread(Object kmlSource, GISFrame gisFrame)
        {
            this.kmlSource = kmlSource;
            this.gisFrame = gisFrame;
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
                        gisFrame.addKMLLayer(finalKMLRoot);
                        gisFrame.layerPanel.update(gisFrame.wwMapPanel.getWwd());
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
            	gisFrame.wwMapPanel.getWwd().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
    }
	
	
    public static class ShapefileWorkerThread extends Thread
    {
        protected Object source;
        protected GISFrame frame;

        public ShapefileWorkerThread(Object source, GISFrame frame)
        {
            this.source = source;
            this.frame = frame;

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
                }

                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        for (Layer layer : layers)
                        {
                            //insertBeforePlacenames(appFrame.getWwd(), layer);
                            frame.wwMapPanel.addLayer(layer);
                        }

                        frame.layerPanel.update(frame.wwMapPanel.getWwd());
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
                        frame.wwMapPanel.getWwd().setCursor(Cursor.getDefaultCursor());
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
