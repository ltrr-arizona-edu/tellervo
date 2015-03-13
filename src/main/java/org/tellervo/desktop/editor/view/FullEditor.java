package org.tellervo.desktop.editor.view;

import gov.nasa.worldwind.layers.MarkerLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.gis.GISPanel;
import org.tellervo.desktop.gis.TridasMarkerLayerBuilder;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.FileDialog;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.prefs.PrefsEvent;
import org.tellervo.desktop.sample.FileElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.ui.ComponentViewerOld;
import org.tellervo.desktop.tridasv2.ui.DependentsViewer;
import org.tellervo.desktop.tridasv2.ui.TridasMetadataPanel;
import org.tellervo.desktop.tridasv2.ui.TridasMetadataPanel.EditType;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Overwrite;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;

public class FullEditor extends AbstractEditor {

	private static final long serialVersionUID = 1L;
	private JPanel metadataHolder;
	private JPanel componentHolder;
	private JPanel dependentHolder;
	private JPanel mapHolder;
	private TridasMetadataPanel metaView;
	
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		Sample sample = getSample();
		
		if(sample.isModified()) return false;
		
		return true;
	}

	
	
	/**
	 * Save the current sample
	 */
	@Override
	public void save() {
	
		Sample sample = getSample();
		
		saveSample(sample);
	}

	/**
	 * Save the specified sample to the database 
	 * 
	 * @param s
	 */
	public void saveSample(Sample s)
	{
		if(s==null) return;
		
		// make sure we're not measuring
		this.stopMeasuring();
		
		// make sure user isn't editing
		dataView.stopEditing(false);
		
		Sample sample = getSample();
		
		// get filename from sample; fall back to user's choice
		if (sample.getLoader() == null) {

			// make sure metadata was entered
			if (!sample.wasMetadataChanged()) {
				// what i'd prefer:
				// Alert.ask("You didn't set the metadata!", { "Save Anyway", "Cancel" })
				// or even: Alert.ask("You didn't set the metadata! [Save Anyway] [Cancel]"); (!)
				/*
				 can i put something like this directly in a resource?
				 You didn't set the metadata! [Save Anyway] [Cancel]
				 that's crazy talk!
				 */
				int x = JOptionPane.showOptionDialog(this,
						I18n.getText("error.noMetadataSet"), I18n.getText("error.metadataUntouched"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, // no icon
						new String[] { I18n.getText("question.saveAnyway"), I18n.getText("general.cancel") }, null); // default
				if (x == 1) {
					// show metadata tab, and abort.
					tabbedPane.setSelectedIndex(1);
					return; // user cancelled!
				}
			}

			String filename = (String) sample.getMeta("filename"); // BUG: why not containsKey()?
			// get target filename
			try {
				filename = FileDialog.showSingle("Save");

				// check for already-exists
				Overwrite.overwrite(filename);
			} catch (UserCancelledException uce) {
				return;
			}

			sample.setMeta(Metadata.FILENAME, filename);
			
			// attach a FileElement to it
			sample.setLoader(new FileElement(filename));
		}

		// complain if it's not complete yet 
		// but only if it's not derived!
		if(!sample.getSampleType().isDerived() && !sample.hasMeta(Metadata.RADIUS)) {
			JOptionPane.showMessageDialog(this,
					I18n.getText("error.metadataIncompleteRadiusRequired"),
					I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
			tabbedPane.setSelectedIndex(1);
			return;
		}
		
		// now, actually try and save the sample
		try {
			sample.getLoader().save(sample);
		} catch (IOException ioe) {
			Alert.error(I18n.getText("error.ioerror"), I18n.getText("error.savingError") +": \n" + ioe.getMessage());
			return;
		} catch (Exception e) {
			new Bug(e);
		}

		// set the necessary bits...
		sample.clearModified();
		sample.fireSampleMetadataChanged(); // things may have changed...
		App.platform.setModified(this, false);
		setTitle();
	}
	
	/**
	 * Save all samples in the workspace
	 */
	public void saveAll()
	{
		for(int i=0; i<this.getSamplesModel().getSize(); i++)
		{
			Sample s = this.getSamplesModel().getElementAt(i);
			
			saveSample(s);
		}
	}
	
	@Override
	public boolean isNameChangeable() {
		return false;
	}

	@Override
	public void setFilename(String fn) {
		// Irrelevant
	}

	@Override
	public String getFilename() {
		// Irrelevant
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
		// make sure we're not measuring
		this.stopMeasuring();
		
		// make sure user isn't editing
		if(dataView!=null) dataView.stopEditing(false);
		
		
			log.debug("Item selected");
			Sample sample = getSample();
			if (sample != null) {
				
				if(dataView!=null) 
				{
					dataView.saveRemarksDividerLocation();
					dataView.saveGraphDividerLocation();
				}
				
				dataView = new SeriesDataMatrix(sample, this);
				dataPanel.removeAll();
				dataPanel.add(dataView, BorderLayout.CENTER);
				dataPanel.repaint();
				this.repaint();
				dataView.restoreRemarksDividerLocation();
				dataView.restoreGraphDividerLocation();
				
				try{
					this.metadataHolder.removeAll();
					metaView = new TridasMetadataPanel(sample);
					this.metadataHolder.add(metaView, BorderLayout.CENTER);
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

	public void showPage(EditType type)
	{

		if(metaView!=null) metaView.showPage(type);
		
	}


}
