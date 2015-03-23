package org.tellervo.desktop.editor;

import gov.nasa.worldwind.layers.MarkerLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.GISPanel;
import org.tellervo.desktop.gis.TridasMarkerLayerBuilder;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.menus.FullEditorActions;
import org.tellervo.desktop.gui.menus.FullEditorMenuBar;
import org.tellervo.desktop.gui.widgets.TitlelessButton;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.prefs.PrefsEvent;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.ui.ComponentViewerOld;
import org.tellervo.desktop.tridasv2.ui.DependentsViewer;
import org.tellervo.desktop.tridasv2.ui.TridasMetadataPanel;
import org.tellervo.desktop.tridasv2.ui.TridasMetadataPanel.EditType;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;

public class FullEditor extends AbstractEditor {

	private static FullEditor instance = null;
	private static final long serialVersionUID = 1L;
	private JPanel metadataHolder;
	private JPanel componentHolder;
	private JPanel dependentHolder;
	private JPanel mapHolder;
	private GISPanel wwMapPanel;
	private TridasMetadataPanel metaView;
	
	
	/**
	 * DO NOT INSTANTIATE
	 */
	protected FullEditor()
	{
		super();
		this.setVisible(true);
		initFullEditor();
	}
	
	/**
	 * Get the FullEditor singleton. If it doesn't already exist, create it.
	 *  
	 * @return
	 */
	public synchronized static FullEditor getInstance()
	{
		if(instance==null)
		{
			instance = new FullEditor();
		}
		return instance;
	
	}
		
	/**
	 * Get the map panel
	 * 
	 * @return
	 */
	public GISPanel getGISPanel()
	{
		return wwMapPanel;
	}
	
	/**
	 * Do initialization of features not handled by AbstractEditor 
	 */
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
		initPopupMenu();

		
	}
	
	/**
	 * Initialise the actions for this FullEditor
	 */
	protected void initActions()
	{
		actions = new FullEditorActions(this);
	}
	
	/**
	 * Initialise the menu for this FullEditor
	 */
	protected void initMenu() {
		menuBar = new FullEditorMenuBar((FullEditorActions) actions, this);
		contentPane.add(menuBar, "cell 0 0,growx,aligny top");

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
			Alert.error(this, I18n.getText("error.ioerror"), I18n.getText("error.savingError") +": \n" + ioe.getMessage());
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
		return null;
	}

	@Override
	public String getDocumentTitle() {
		return null;
	}

	@Override
	public void prefChanged(PrefsEvent e) {
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
				
				// Data tab
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
					
					// Metadata tab
					metadataHolder.removeAll();
					metaView = new TridasMetadataPanel(sample);
					metadataHolder.add(metaView, BorderLayout.CENTER);
					
					
					// Components tab
					ComponentViewerOld componentsPanel;				
					componentHolder.removeAll();
					if(sample.getSampleType().isDerived()){
						componentsPanel = new ComponentViewerOld(sample);
						this.componentHolder.add(componentsPanel, BorderLayout.CENTER);
					}
					
					// Dependents tab
					dependentHolder.removeAll();
					DependentsViewer dependentsPanel = new DependentsViewer(sample);
					dependentHolder.add(dependentsPanel, BorderLayout.CENTER);
					
					// Map tab
					mapHolder.removeAll();
					createMapPanel();
					mapHolder.add(wwMapPanel, BorderLayout.CENTER);
					
					this.revalidate();
				} catch (Exception e)
				{
					
				}
				
				setTitle();
				this.pack();
				this.repaint();
			}
	
	}
	
	protected void initToolbar() {

		JToolBar toolBar = new JToolBar();

		// File Buttons
		AbstractButton fileOpen = new TitlelessButton(actions.fileOpenAction);
		toolBar.add(fileOpen);

		AbstractButton save = new TitlelessButton(actions.fileSaveAction);
		toolBar.add(save);

		AbstractButton fileexport = new TitlelessButton(actions.fileExportDataAction);
		toolBar.add(fileexport);

		// Edit Buttons
		AbstractButton measure = new TitlelessButton(actions.editMeasureAction);
		toolBar.add(measure);

		// Initialize data grid button
		AbstractButton initGrid = new TitlelessButton(actions.editInitGridAction);
		toolBar.add(initGrid);

		// Remarks Button
		AbstractButton toggleRemarks = new TitlelessButton(actions.remarkAction);
		toolBar.add(toggleRemarks);

		// Admin Buttons
		toolBar.addSeparator();
		AbstractButton launchMetadb = new TitlelessButton(actions.adminMetaDBAction);
		toolBar.add(launchMetadb);

		// s Buttons
		toolBar.addSeparator();
		AbstractButton truncate = new TitlelessButton(actions.toolsTruncateAction);
		toolBar.add(truncate);

		// Graph Buttons
		toolBar.addSeparator();
		AbstractButton graph = new TitlelessButton(actions.graphAllSeriesAction);
		toolBar.add(graph);

		contentPane.add(toolBar, "cell 0 1,growx,aligny top");

	}
	
	/**
	 * Close Tellervo but confirm save of series if necessary first.
	 * 
	 */
	public void cleanupAndDispose()
	{
		int modifiedCount = 0;
		for(Sample s : getSamples())
		{
			if(s.isModified()) modifiedCount++;
		}
		
		if(modifiedCount>0)
		{
			String question = "";
			if(modifiedCount==1)
			{
				if(getSamples().size()>1) {
					question = "One of the series has been modified.  Would you like to save it before closing?";
				} else 
				{
					question = "The series has been modified.  Would you like to save it before closing?";
				}
			}
			else if(modifiedCount>1)
			{
				question = "There are "+modifiedCount+" series that have unsaved changes.  Would you like to save them before closing?";
			}
				
			
			// Confirm save
			Object[] options = {"Save",
                    "Discard",
                    "Cancel"};
				int n = JOptionPane.showOptionDialog(this,
				    question,
				    "Save document?",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[2]);
				
			if(n==JOptionPane.YES_OPTION)
			{
				save();
			}
			else if (n==JOptionPane.NO_OPTION)
			{
				
			}
			else if (n==JOptionPane.CANCEL_OPTION)
			{
				return;
			}
		}
		
		System.exit(0);
	}
	
	/**
	 * Create the GISPanel tab panel
	 * 
	 * @return
	 */
	private GISPanel createMapPanel()
	{
		
		TridasElement elem = this.getSample().getMeta(Metadata.ELEMENT, TridasElement.class);
		
		
		// Create layer of all sites
		TridasMarkerLayerBuilder builder = new TridasMarkerLayerBuilder();
		MarkerLayer allSites = TridasMarkerLayerBuilder.getMarkerLayerForAllSites();
		allSites.setEnabled(false);
		
		try{
			wwMapPanel = new GISPanel(new Dimension(300,400),true, allSites);
			
		} catch (Exception e)
		{
			Alert.error(this, "Error", "There was an error initialising the map, most " +
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

	/**
	 * Set the metadata page to show
	 * 
	 * @param type
	 */
	public void showPage(EditType type)
	{
		if(metaView!=null) metaView.showPage(type);
	}


	@Override
	public void windowActivated(WindowEvent e) {		
	}

	@Override
	public void windowClosed(WindowEvent e) {		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		cleanupAndDispose();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {	
	}

	@Override
	public void windowDeiconified(WindowEvent e) {	
	}

	@Override
	public void windowIconified(WindowEvent e) {		
	}

	@Override
	public void windowOpened(WindowEvent e) {	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
