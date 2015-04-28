package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis2.OpenGLTestCapabilities;
import org.tellervo.desktop.gis2.TellervoSampleLayer;
import org.tellervo.desktop.gis2.WWJPanel;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.menus.FullEditorActions;
import org.tellervo.desktop.gui.menus.FullEditorMenuBar;
import org.tellervo.desktop.gui.widgets.TitlelessButton;
import org.tellervo.desktop.gui.widgets.TitlelessToggleButton;
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

public class FullEditor extends AbstractEditor {

	private static FullEditor instance = null;
	private static final long serialVersionUID = 1L;
	private JPanel metadataHolder;
	private JPanel componentHolder;
	private JPanel dependentHolder;
	private JPanel mapHolder;
	public WWJPanel wwMapPanel;
	private TridasMetadataPanel metaView;
	
	
	/**
	 * DO NOT INSTANTIATE
	 */
	protected FullEditor()
	{
		super();
		instance = this;
		this.setVisible(true);
		initActions();
		initFullEditor();
		//initActions();
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
		
	public void switchToMapTab()
	{
		try{
			this.tabbedPane.setSelectedIndex(4);
		} catch (Exception e)
		{
			
		}
	}
	
	/**
	 * Get the map panel
	 * 
	 * @return
	 */
	public WWJPanel getMapPanel()
	{
		return wwMapPanel;
	}
	
	/**
	 * Add the specified sample to the workspace and select it
	 * 
	 * @param s
	 */
	@Override
	public void addSample(Sample s)
	{
		try{
			for(Sample sample : samplesModel.getSamples())
			{
				
				if(s.getIdentifier().isSetValue() && s.getIdentifier().getValue()!="newSeries" 
						&& s.getIdentifier().equals(sample.getIdentifier()))
				{
					log.debug("Sample already in workspace.  Not adding again, just selecting it");
					log.debug("Sample identifier: "+s.getIdentifier().getValue());
					this.getLstSamples().setSelectedValue(sample, true);
					return;
				}
			}
		} catch (Exception e)
		{
			
		}
		
		samplesModel.addElement(s);		
		this.getLstSamples().setSelectedValue(s, true);
		itemSelected();
	}
	
	/**
	 * Do initialization of features not handled by AbstractEditor 
	 */
	public void initFullEditor()
	{
		this.btnAdd.setVisible(false);
		
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
		
		if(OpenGLTestCapabilities.isOpenGLCapable()) tabbedPane.addTab("Map", Builder.getIcon("maptab.png", 16), mapHolder, null);
		
			
		itemSelected();
		initPopupMenu();
		initMapPanel();
	
		
		
		this.getLstSamples().addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent evt) {
				if(evt.getClickCount()>1)
				{
					// Zoom map
					if(OpenGLTestCapabilities.isOpenGLCapable()) wwMapPanel.zoomToSample(getSample());
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
	}
	
	public void reinitMapPanel()
	{
		initMapPanel();
		
		TellervoSampleLayer layer = (TellervoSampleLayer) wwMapPanel.getWorkspaceSeriesLayer();
		
		for(Sample s: samplesModel.getSamples())
		{
			layer.addMarker(s);
		}
		
		
	}
	
	private void initMapPanel()
	{
		if(!OpenGLTestCapabilities.isOpenGLCapable()) return;

		
		wwMapPanel = new WWJPanel();
		mapHolder.removeAll();
		mapHolder.add(wwMapPanel, BorderLayout.CENTER);
		
		this.getSamplesModel().addListDataListener(new ListDataListener(){

			@Override
			public void contentsChanged(ListDataEvent arg0) {
				
				wwMapPanel.removeAnnotations();
				
				TellervoSampleLayer layer = (TellervoSampleLayer) wwMapPanel.getWorkspaceSeriesLayer();
				
				for(Sample s: samplesModel.getSamples())
				{
					layer.addMarker(s);
				}
				
				layer.removeAbsentMarkers(samplesModel.getSamples());
				
			}

			@Override
			public void intervalAdded(ListDataEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void intervalRemoved(ListDataEvent evt) {
				TellervoSampleLayer layer = (TellervoSampleLayer) wwMapPanel.getWorkspaceSeriesLayer();

				
				
			}
			
		});		
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
					this.gInfo = dataView.getGraphPanel().getGraphSettings();
					this.plotAgent = dataView.getGraphPanel().getPlotAgent();
				}
				
				
				dataView = new SeriesDataMatrix(sample, this);
				dataView.setPlotAgent(plotAgent);
				
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
					componentHolder.setVisible(sample.getSampleType().isDerived());
					componentHolder.removeAll();
					if(sample.getSampleType().isDerived()){
						componentsPanel = new ComponentViewerOld(sample);
						this.componentHolder.add(componentsPanel, BorderLayout.CENTER);
						this.tabbedPane.setEnabledAt(2, true);
					}
					else
					{
						this.tabbedPane.setEnabledAt(2, false);
					}
					
					
					// Dependents tab
					dependentHolder.removeAll();
					DependentsViewer dependentsPanel = new DependentsViewer(sample);
					dependentHolder.add(dependentsPanel, BorderLayout.CENTER);

					// Highlight map pin
					if(OpenGLTestCapabilities.isOpenGLCapable()) wwMapPanel.highlightMarkerForSample(getSample());
					
					this.revalidate();
				} catch (Exception e)
				{
					
				}
				
			}
			else 
			{
				// Sample is null so clean tabs
				
				dataPanel.removeAll();
				metadataHolder.removeAll();
				componentHolder.removeAll();
				dependentHolder.removeAll();
				dataPanel.repaint();
				metadataHolder.repaint();
				componentHolder.repaint();
				dependentHolder.repaint();

			}
			
			setTitle();
			this.repaint();
	
	}
	
	protected void initToolbar() {

		
		JToolBar toolBar = new JToolBar();
		
		// File Buttons
		AbstractButton fileNew = new TitlelessButton(actions.fileNewAction);
		toolBar.add(fileNew);
		
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

		// Tool Buttons
		toolBar.addSeparator();
		AbstractButton truncate = new TitlelessButton(actions.toolsTruncateAction);
		toolBar.add(truncate);
		
		AbstractButton index = new TitlelessButton(((FullEditorActions)actions).toolsIndexAction);
		toolBar.add(index);
		
		AbstractButton sum = new TitlelessButton(((FullEditorActions)actions).toolsSumAction);
		toolBar.add(sum);
		
		

		// Graph Buttons
		toolBar.addSeparator();
		AbstractButton graph = new TitlelessButton(actions.graphAllSeriesAction);
		toolBar.add(graph);

		// Map Buttons
		
		toolBar.addSeparator();
		
		AbstractButton spatialSearch = new TitlelessButton(((FullEditorActions)actions).mapSpatialSearchAction);
		toolBar.add(spatialSearch);
		
		AbstractButton compass = new TitlelessToggleButton(((FullEditorActions)actions).mapCompassToggleAction);
		toolBar.add(compass);
		
		AbstractButton overviewMap = new TitlelessToggleButton(((FullEditorActions)actions).mapWorldMapLayerToggleAction);
		toolBar.add(overviewMap);
		
		AbstractButton controlLayer = new TitlelessToggleButton(((FullEditorActions)actions).mapControlLayerToggleAction);
		toolBar.add(controlLayer);
		
		AbstractButton scaleBarLayer = new TitlelessToggleButton(((FullEditorActions)actions).mapScaleBarLayerToggleAction);
		toolBar.add(scaleBarLayer);
		
		AbstractButton UTMGraticule = new TitlelessToggleButton(((FullEditorActions)actions).mapUTMGraticuleLayerToggleAction);
		toolBar.add(UTMGraticule);
		
		AbstractButton MGRSGraticule = new TitlelessToggleButton(((FullEditorActions)actions).mapMGRSGraticuleLayerToggleAction);
		toolBar.add(MGRSGraticule);
		
		AbstractButton placeNames = new TitlelessToggleButton(((FullEditorActions)actions).mapNASAWFSPlaceNameLayerToggleAction);
		toolBar.add(placeNames);
		
		AbstractButton politicalBoundaries = new TitlelessToggleButton(((FullEditorActions)actions).mapCountryBoundariesLayerToggleAction);
		toolBar.add(politicalBoundaries);
		
		toolBar.addSeparator();
		
		AbstractButton stereo = new TitlelessButton(((FullEditorActions)actions).mapStereoModeAction);
		toolBar.add(stereo);
		
		AbstractButton saveImage = new TitlelessButton(((FullEditorActions)actions).mapSaveCurrentMapAsImagesAction);
		toolBar.add(saveImage);
		
		AbstractButton shapefile = new TitlelessButton(((FullEditorActions)actions).mapShapefileLayerAction);
		toolBar.add(shapefile);
		
		AbstractButton KMLLayer = new TitlelessButton(((FullEditorActions)actions).mapKMLLayerAction);
		toolBar.add(KMLLayer);
		
		AbstractButton GISImage = new TitlelessButton(((FullEditorActions)actions).mapGISImageAction);
		toolBar.add(GISImage);
		
		AbstractButton WMSLayer = new TitlelessButton(((FullEditorActions)actions).mapWMSLayerAction);
		toolBar.add(WMSLayer);
		
		AbstractButton DBLayer = new TitlelessButton(((FullEditorActions)actions).mapDatabaseLayerAction);
		toolBar.add(DBLayer);	
		
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

	@Override
	protected void initPopupMenu()
	{
		log.debug("Init popup menu");
		
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem delete = new JMenuItem(actions.removeSeriesAction);
		popupMenu.add(delete);
		
		JMenuItem zoomMap = new JMenuItem(((FullEditorActions)actions).mapZoomAction);
		popupMenu.add(zoomMap);
		
		
		this.getLstSamples().addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		        showPopup(e);
		    }

		    public void mouseReleased(MouseEvent e) {
		        showPopup(e);
		    }

		    private void showPopup(MouseEvent e) {
		       
				if (e.isPopupTrigger()) {
					if(getLstSamples().getSelectedIndex()!=-1)
					{
						popupMenu.show(e.getComponent(),
								e.getX(), e.getY());
					}
		        }
		    }
		});
			
	}

	/**
	 * Sample list has changed
	 */
	@Override
	public void contentsChanged(ListDataEvent e) {

		
	}

	/**
	 * Sample list has had item added to it
	 */
	@Override
	public void intervalAdded(ListDataEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sample list has had item removed from it
	 */
	@Override
	public void intervalRemoved(ListDataEvent e) {
		
		
	}
	
	public FullEditorActions getAction(){
		
		return (FullEditorActions) actions;
	}
	

	
}
