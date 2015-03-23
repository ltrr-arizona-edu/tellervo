package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.FilenameUtils;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.menus.LiteEditorActions;
import org.tellervo.desktop.gui.menus.LiteEditorMenuBar;
import org.tellervo.desktop.io.AbstractDendroReaderFileFilter;
import org.tellervo.desktop.io.DendroReaderFileFilter;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.PrefsEvent;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.util.UnitUtils;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasInterpretation;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasTridas;
import org.tridas.schema.TridasValues;

public class LiteEditor extends AbstractEditor implements SaveableDocument{

	private static final long serialVersionUID = 1L;
	
	private JPanel metadataHolder;
	private boolean savedByTellervo = false;
	private BasicMetadataPanel metadata;
	
	private static ArrayList<LiteEditor> windows = new ArrayList<LiteEditor>();
	
	/**
	 * Do not instantiate.  Use getNewInstance() instead
	 */
	protected LiteEditor()
	{
		super();
		this.setVisible(true);
		initLiteEditor();
	}
	
	/**
	 * Close all LiteEditor windows open 
	 */
	public static void closeAllEditors()
	{
		ArrayList<LiteEditor> modifiedEditors = new ArrayList<LiteEditor>();
		
		for(LiteEditor editor : windows)
		{
			if(!editor.isSaved()) modifiedEditors.add(editor);
		}
		
		if(modifiedEditors.size()==0)
		{
			for(LiteEditor editor : windows)
			{
				editor.cleanupAndDispose();
			}
		}
		else
		{
			// Confirm save
			Object[] options = {"Save",
                    "Discard",
                    "Cancel"};
				int n = JOptionPane.showOptionDialog(windows.get(0),
				    "Would you like to save changes to all modified documents before closing?",
				    "Save document?",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[2]);
				
			if(n==JOptionPane.YES_OPTION)
			{
				for(LiteEditor ed : modifiedEditors)
				{
					ed.save();
					ed.cleanupAndDispose();
				}
			}
			else if (n==JOptionPane.NO_OPTION)
			{
				for(LiteEditor ed : modifiedEditors)
				{
					ed.cleanupAndDispose(true);
				}
			}
			else if (n==JOptionPane.CANCEL_OPTION)
			{
				return;
			}
			
			
		}
	}
	
	/**
	 * Get a new instance of a LiteEditor.  If there is just one editor open and it is clean with no series, then this will be reused
	 * 
	 * @return
	 */
	public synchronized static LiteEditor getNewInstance()
	{		
		if(windows.size()==1)
		{
			if(windows.get(0).getSamples().size()==0)
			{
				return windows.get(0);
			}
		}
		
		LiteEditor editor = new LiteEditor();
		windows.add(editor);
		
		return editor;
	}
	
	/**
	 * Load a legacy dendro data file
	 * 
	 * @param parent
	 * @param file
	 * @param fileType
	 * @throws Exception
	 */
	public void loadFile(Window parent, File file, String fileType) throws Exception
	{
		loadFile(parent, file, new DendroFileFilter(null, fileType));
							
	}
	
	/**
	 * Load a legacy dendro data file
	 * 
	 * @param parent
	 * @param file
	 * @param filetypefilter
	 * @throws Exception
	 */
	public void loadFile(Window parent, File file, DendroFileFilter filetypefilter) throws Exception
	{
		this.parent = parent;
		this.file = file;
		
				
		for (String readername : TridasIO.getSupportedReadingFormats())
		{
			AbstractDendroReaderFileFilter filter = new DendroReaderFileFilter(readername);
			String format = filetypefilter.getFormatName();
			log.debug("Checking to see if "+format+" matches "+ filter.getDescription());
			
			if(filter.getDescription().equals(format))
			{
				this.fileType = filter.getDescription();
				parseFile();
				initLiteEditor();
				return;
			}
		}
		
		throw new Exception("Unsupported dendro data file type.  Do not know "+filetypefilter.toString());
		
	}
	
	/**
	 * Does this editor have any samples in it?
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return this.getSamples().size()==0;
	}
	
	/**
	 * Initialise the Lite editor window
	 * 
	 */
	public void initLiteEditor()
	{
		metadataHolder = new JPanel();
		metadataHolder.setLayout(new BorderLayout());
		tabbedPane.addTab("Metadata", Builder.getIcon("database.png", 16), metadataHolder, null);
		metadata = new BasicMetadataPanel();
		this.metadataHolder.add(metadata, BorderLayout.CENTER);
		
		itemSelected();
		this.setVisible(true);
			
	}
	
	
	@Override
	public boolean isSaved() {
		
		for(Sample s: getSamples())
		{
			if(s.isModified()) return false;
		}
		
		return true;
	}

	@Override
	public void save() {
	
		// make sure we're not measuring
		this.stopMeasuring();
		
		// make sure user isn't editing
		dataView.stopEditing(false);
		
		// Save without prompt if possible
		if(file!=null)
		{
			// Existing file
			
			if(savedByTellervo)
			{
				// Previously saved by Tellervo
				try {
					saveToDisk();
				} catch (Exception e) {
					Alert.error(this, "Error Saving", "Error saving to disk.  "+e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
			else
			{
				// Not previously saved by Tellervo
				
				Object[] options = {"Yes",
	                    "No",
	                    "Cancel"};
				int n = JOptionPane.showOptionDialog(this,
				    "Tellervo has not previously saved to this file. Tellervo-lite\n" +
				    "doesn't handle structured metadata. If this file already contains\n" +
				    "metadata then you may loose some information.\n\n" +
			        "Are you sure you want to overwrite this file?",
				    "Overwrite File?",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[2]);
				
				if(n == JOptionPane.YES_OPTION)
				{
					try {
						saveToDisk();
					} catch (Exception e) {
						Alert.error(this, "Error Saving", "Error saving to disk.  "+e.getLocalizedMessage());
						e.printStackTrace();
					}
				}
				return;
			}
		}
		else
		{
			// New file
			
			if(savedByTellervo)
			{
				log.error("New file but has previously been saved?!? This should be impossible!");
			}
			else
			{
				try {
					saveAs();
				} catch (Exception e) {
					Alert.error(this, "Error Saving", "Error saving to disk.  "+e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}	
		
	}

	@Override
	public boolean isNameChangeable() {
		return true;
	}

	@Override
	public void setFilename(String fn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFilename() {
		return file.getName();
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
	public void setTitle()
	{
		if(this.file!=null)
		{
			if(this.isSaved())
			{
				this.setTitle(file.getName()+" - Tellervo");
			}
			else
			{
				this.setTitle("* "+file.getName()+" - Tellervo");

			}
		}
		else
		{
			if(this.isSaved())
			{
				this.setTitle("Tellervo");
			}
			else
			{
				this.setTitle("* Tellervo");
			}
		}
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
					//dataView.saveRemarksDividerLocation();	
					//dataView.saveGraphDividerLocation();
				}
				
				dataView = new SeriesDataMatrix(sample, this);
				dataPanel.removeAll();
				dataPanel.add(dataView, BorderLayout.CENTER);

				//dataView.restoreRemarksDividerLocation();
				//dataView.restoreGraphDividerLocation();

				metadata.setSample(getSample());
							
			}
			
			setTitle();
			//this.pack();
			this.repaint();
	}
	
	/**
	 * Save the current file to disk with a new name
	 * 
	 * @throws Exception
	 */
	public void saveAs() throws Exception
	{	
		// custom jfilechooser
		File thisFile = null;
		String format = null;
		JFileChooser fc = new JFileChooser();
	
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
								
		// Loop through formats and create filters for each
		fc.setAcceptAllFileFilterUsed(false);
		ArrayList<DendroFileFilter> filters = TridasIO.getFileReadingFilterArray();
		Collections.sort(filters);
		for(DendroFileFilter filter : filters)
		{			
			fc.addChoosableFileFilter(filter);
			if(fileType!=null)
			{
				if(fileType.equals(filter.getFormatName()))
				{
					fc.setFileFilter(filter);
				}
			}
		}
		

		// Pick the last used directory by default
		try{
			File lastDirectory = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
			if(lastDirectory != null){
				fc.setCurrentDirectory(lastDirectory);
			}
		} catch (Exception e)
		{
		}
		
		int retValue = fc.showSaveDialog(parent);
		if (retValue == JFileChooser.APPROVE_OPTION) {
			thisFile = fc.getSelectedFile();
			// Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, thisFile.getPath());
			format = ((DendroFileFilter)fc.getFileFilter()).getFormatName();
			App.prefs.setPref(PrefKey.IMPORT_FORMAT, format);
		}
		
		if (thisFile == null) {
			return;
		}
				
		
		file = thisFile;
		fileType = format;
		saveToDisk();
	}
	
	/**
	 * Save the current file to disk 
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean saveToDisk() throws Exception
	{
		if(file==null )
		{
			throw new Exception("No filename provided for saving");
		}
		
		if(fileType==null)
		{
			throw new Exception("No file format provided for saving");
		}
		
		AbstractDendroCollectionWriter writer = null;

		String filename = file.getAbsolutePath();
		String ext = FilenameUtils.getExtension(filename);
		String path = FilenameUtils.getFullPath(filename);


		if(ext!=null)
		{
			filename = FilenameUtils.getBaseName(filename);
		}

		log.debug("Saving legacy file to disk");
		log.debug("Path: "+path);
		log.debug("Filename: "+filename);
		log.debug("Extension: "+ext);


	
		
		writer = TridasIO.getFileWriter(fileType);
		
		

		NumericalNamingConvention nc = new NumericalNamingConvention(filename);
		nc.setAddSequenceNumbersForUniqueness(false);
		writer.setNamingConvention(nc);

		TridasTridas container = (TridasTridas) getContainerForFile().clone();

		writer.load(container);
		// Actually save file(s) to disk
		writer.saveAllToDisk(path);


		
		for(Sample sample : getSamples())
		{	
			sample.clearModified();
			sample.setMeta(Metadata.FILENAME, file.getAbsolutePath());
			sample.setMeta(Metadata.LEGACY_FORMAT, fileType);
			sample.setMeta(Metadata.LEGACY_SAVED_BY_TELLERVO, true);
			sample.fireSampleMetadataChanged();
			
			savedByTellervo = true;
			
		}

		App.prefs.setPref(PrefKey.EXPORT_FORMAT, fileType);
		App.prefs.setPref(PrefKey.FOLDER_LAST_SAVE, path);

		setTitle();
		repaint();
		
		return true;
	}
	
	/**
	 * Got the TRiDaS container for this file
	 * 
	 * @return
	 */
	private TridasTridas getContainerForFile()
	{

		TridasRadius radius = new TridasRadius();

		TridasSample sample = new TridasSample();

		TridasElement element = new TridasElement();
		
		 
		//element.setTaxon(ITRDBTaxonConverter.getControlledVocFromCode(((BasicMetadataPanel)metaView).txtSpecies.getText()));

		TridasObject object = new TridasObject();
		

		TridasProject proj = new TridasProject();
		
		
		
		
		for(int i=0; i<this.getSamples().size(); i++)
		{
			Sample s = this.getSamples().get(i);
			TridasMeasurementSeries series = (TridasMeasurementSeries) ((TridasMeasurementSeries) s.getSeries()).clone();
			
	
			String pref = App.prefs.getPref(PrefKey.DISPLAY_UNITS, NormalTridasUnit.MICROMETRES.toString());

			log.debug("Display pref: "+pref);
			NormalTridasUnit selectedUnits = NormalTridasUnit.MICROMETRES;
			for(NormalTridasUnit unit : NormalTridasUnit.values())
			{
				
				if(unit.toString().equals(pref))
				{
					selectedUnits = unit;
					break;
				}
			}
						
			for(TridasValues tv : series.getValues())
			{
				log.debug("Original units are: "+tv.getUnit().toString());
				
				
				try {
					tv = UnitUtils.convertTridasValues(selectedUnits, tv, false);
					
					log.debug("New units are: "+tv.getUnit().toString());
				} catch (NumberFormatException | ConversionWarningException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			series.setTitle(s.getMetaString(Metadata.TITLE));
			series.setDendrochronologist(s.getMetaString(Metadata.AUTHOR));
						
			
			TridasInterpretation interp = new TridasInterpretation();
			interp.setFirstYear(s.getStart().tridasYearValue());
			series.setInterpretation(interp);
			
			TridasGenericField gf = new TridasGenericField();
			gf.setName("keycode");
			gf.setType("xs:string");
			gf.setValue(s.getMetaString(Metadata.KEYCODE));
			series.getGenericFields().add(gf);
			
			radius.getMeasurementSeries().add(series);
		}


		sample.getRadiuses().add(radius);
		element.getSamples().add(sample);
		object.getElements().add(element);
		proj.getObjects().add(object);


		// Extract the TridasProject
		TridasTridas container = new TridasTridas();
		container.getProjects().add(proj);

		return container;
	}

	/**
	 * Initialise the actions for this editor
	 * 
	 */
	protected void initActions()
	{
		actions = new LiteEditorActions(this);
	}
	
	/**
	 * Initialise the menu for this editor 
	 */
	protected void initMenu() {
		
		menuBar = new LiteEditorMenuBar((LiteEditorActions) actions, this);
		contentPane.add(menuBar, "cell 0 0,growx,aligny top");

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent evt) {
		cleanupAndDispose();	
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Dispose of this editor but confirm saving first in necessary and not in silent mode
	 * 
	 * @param silentmode
	 */
	public void cleanupAndDispose(boolean silentmode)
	{
		if(!isSaved() && !silentmode)
		{
			// Confirm save
			Object[] options = {"Save",
                    "Discard",
                    "Cancel"};
				int n = JOptionPane.showOptionDialog(this,
				    "Would you like to save this document before closing?",
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
		
		// Keep track of how many windows there are.  If this is the last, then close Tellervo.
		LiteEditor.windows.remove(this);
		if(LiteEditor.windows.size()==0)
		{
			System.exit(0);
		}
	}
	
	@Override
	public void cleanupAndDispose() {
		
		cleanupAndDispose(false);
	}
	

	
}
