package edu.cornell.dendro.corina.gis;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.CorinaCodePanel;
import edu.cornell.dendro.corina.gui.TridasSelectEvent;
import edu.cornell.dendro.corina.gui.TridasSelectListener;
import edu.cornell.dendro.corina.gui.TridasSelectEvent.TridasSelectType;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.WWIO;

public class AddGISDataDialog extends JDialog implements ActionListener, TridasSelectListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(AddGISDataDialog.class);
	private GISFrame parent;
	private GISPanel wwMapPanel;
	private JComboBox cboAddType;
	private JButton btnAdd;
	private CorinaCodePanel codePanel;
	private JLabel lblLabcode;
	
	public enum AddLayerType{
		ALL_OBJECTS("All Corina objects"),
		CORINA_ENTITY("Corina entity from database"),
		ELEMENTS_FOR_OBJECT("Elements from an object"),
		ITRDB_SITES("All ITRDB sites"),
		SHAPEFILE("ESRI Shapefile");
		
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
		setModal(true);
		
		
		this.wwMapPanel = parent.wwMapPanel;
		this.parent = parent;
		getContentPane().setLayout(new MigLayout("", "[84.00][65.00px][grow]", "[][][grow][]"));
		
		JLabel lblIcon = new JLabel("");
		lblIcon.setIcon(Builder.getIcon("layers.png", 64));		
		getContentPane().add(lblIcon, "cell 0 0 1 3,alignx center");
		
		JLabel lblAddLayer = new JLabel("Add:");
		getContentPane().add(lblAddLayer, "cell 1 0,alignx trailing");
		
		cboAddType = new JComboBox();
		
		cboAddType.setModel(new DefaultComboBoxModel(AddLayerType.values()));
		getContentPane().add(cboAddType, "cell 2 0,growx");
		
		cboAddType.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGUIForAddType();
				
			}
			
		});
		
		codePanel = new CorinaCodePanel(this);
		codePanel.addTridasSelectListener(this);
		
		lblLabcode = new JLabel("Labcode:");
		getContentPane().add(lblLabcode, "cell 1 1,alignx right");
		getContentPane().add(codePanel, "cell 2 1,growx");
		this.setCodePanelVisible(false);
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, "cell 0 3 3 1,alignx right,growy");
		
		btnAdd = new JButton("Add");
		btnAdd.setActionCommand("add");
		btnAdd.addActionListener(this);
		buttonPanel.add(btnAdd);
		
		JButton btnCancel = new JButton("Close");
		btnCancel.setActionCommand("cancel");
		btnCancel.addActionListener(this);
		buttonPanel.add(btnCancel);
		
		pack();
		this.setLocationRelativeTo(parent);
	}

	
	private void setCodePanelVisible(Boolean b)
	{
		lblLabcode.setVisible(b);
		codePanel.setVisible(b);
	}
	
	private void setGUIForAddType()
	{
		AddLayerType selectedType = (AddLayerType) cboAddType.getSelectedItem();
		if(selectedType.equals(AddLayerType.SHAPEFILE))
		{
			btnAdd.setText("Browse");
		}
		else
		{
			btnAdd.setText("Add");
		}
		
		if(selectedType.equals(AddLayerType.CORINA_ENTITY) ||
		   selectedType.equals(AddLayerType.ELEMENTS_FOR_OBJECT))
		{
			setCodePanelVisible(true);
		}
		else
		{
			setCodePanelVisible(false);
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
				codePanel.processLabCode();
				
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
				
				
				searchResource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.STANDARD);
				CorinaResourceAccessDialog dlg = new CorinaResourceAccessDialog(parent, searchResource);
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

    
}
