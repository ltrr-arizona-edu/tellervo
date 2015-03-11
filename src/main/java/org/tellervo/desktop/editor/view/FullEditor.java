package org.tellervo.desktop.editor.view;

import gov.nasa.worldwind.layers.MarkerLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.gis.GISPanel;
import org.tellervo.desktop.gis.GISViewMenu;
import org.tellervo.desktop.gis.TridasMarkerLayerBuilder;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.prefs.PrefsEvent;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.tridasv2.ui.ComponentViewerOld;
import org.tellervo.desktop.tridasv2.ui.DependentsViewer;
import org.tellervo.desktop.tridasv2.ui.TridasMetadataPanel;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;

public class FullEditor extends AbstractEditor {

	private static final long serialVersionUID = 1L;
	JPanel metadataHolder;
	JPanel componentHolder;
	JPanel dependentHolder;
	JPanel mapHolder;
	
	
	public FullEditor()
	{
		super();
		this.setVisible(true);
		initFullEditor();
	}
	
	public FullEditor(Sample sample)
	{
		super(sample);
		
		this.setVisible(true);
		initFullEditor();
	}
	
	public FullEditor(ArrayList<Sample> samples)
	{
		super(samples);
		this.setVisible(true);
		initFullEditor();

	}
	
	public GISPanel getGISPanel()
	{
		//TODO
		return null;
	}
	
	public void initFullEditor()
	{
		metadataHolder = new JPanel();
		metadataHolder.setLayout(new BorderLayout());
		
		componentHolder = new JPanel();
		componentHolder.setLayout(new BorderLayout());
		
		dependentHolder = new JPanel();
		dependentHolder.setLayout(new BorderLayout());
		
		mapHolder = new JPanel();
		mapHolder.setLayout(new BorderLayout());
		
		tabbedPane.addTab("Metadata", Builder.getIcon("database.png", 16), metadataHolder, null);
		tabbedPane.addTab("ComponentsViewer", Builder.getIcon("history.png", 16), componentHolder, null);
		tabbedPane.addTab("DependentsViewer", Builder.getIcon("dependent.png", 16), dependentHolder, null);
		tabbedPane.addTab("Map", Builder.getIcon("maptab.png", 16), mapHolder, null);
		
		itemSelected();
		
	}
	

	
	@Override
	public boolean isSaved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNameChangeable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFilename(String fn) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSavedDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prefChanged(PrefsEvent e) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void itemSelected() {
	
			log.debug("Item selected");
			Sample sample = getSample();
			if (sample != null) {
				
				if(dataView!=null) 
				{
					dataView.saveRemarksDividerLocation();			
				}
				
				dataView = new SeriesDataMatrix(sample, this);
				dataPanel.removeAll();
				dataPanel.add(dataView, BorderLayout.CENTER);
				dataPanel.repaint();
				this.repaint();
				dataView.restoreRemarksDividerLocation();
				
				try{
					this.metadataHolder.removeAll();
					TridasMetadataPanel metadata = new TridasMetadataPanel(sample);
					this.metadataHolder.add(metadata, BorderLayout.CENTER);
					ComponentViewerOld componentsPanel;				
					if(sample.getSampleType().isDerived()){
						componentsPanel = new ComponentViewerOld(sample);
						this.componentHolder.add(componentsPanel, BorderLayout.CENTER);
					}
					DependentsViewer dependentsPanel = new DependentsViewer(sample);
					this.dependentHolder.add(dependentsPanel, BorderLayout.CENTER);
					
					this.mapHolder.add(createMapPanel(), BorderLayout.CENTER);
					
					
				} catch (Exception e)
				{
					
				}
				
				
				
				setTitle();
				
			}
	
	}
	
	private GISPanel createMapPanel()
	{
		GISPanel wwMapPanel;
		TridasElement elem = this.getSample().getMeta(Metadata.ELEMENT, TridasElement.class);
		
		
		// Create layer of all sites
		TridasMarkerLayerBuilder builder = new TridasMarkerLayerBuilder();
		MarkerLayer allSites = TridasMarkerLayerBuilder.getMarkerLayerForAllSites();
		allSites.setEnabled(false);
		
		try{
			wwMapPanel = new GISPanel(new Dimension(300,400),true, allSites);
			
		} catch (Exception e)
		{
			Alert.error("Error", "There was an error initialising the map, most " +
					"probably to do with 3D graphics drivers");
			return null;
		}
		
		try{
			// First try to add a pin for the TridasElement itself
			//editorViewMenu = new GISViewMenu(wwMapPanel.getWwd());	
			builder.addMarkerForTridasElement(elem);	
			
			// If no pin added for TridasElement, try TridasObject instead
			if(!builder.containsMarkers())
			{
				builder.addMarkerForTridasObject(getSample().getMeta(Metadata.OBJECT, TridasObject.class));
			}
				
			// If still no joy, disable the map
			if(!builder.containsMarkers())
			{
				return null;
			}
				
			//wwMapPanel.addFocusListener(this);
			
			// Create layer of current element
			builder.setName("Elements of this series");
			wwMapPanel.addLayer(builder.getMarkerLayer());

		} catch (Exception e)
		{
			
		}
		
		return wwMapPanel;
	}


}
