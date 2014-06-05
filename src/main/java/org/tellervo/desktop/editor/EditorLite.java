package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.graph.GraphToolbar.TitlelessButton;
import org.tellervo.desktop.gui.menus.EditorEditMenu;
import org.tellervo.desktop.gui.menus.EditorLiteFileMenu;
import org.tellervo.desktop.gui.menus.EditorLiteGraphMenu;
import org.tellervo.desktop.gui.menus.EditorLiteToolsMenu;
import org.tellervo.desktop.gui.menus.HelpMenu;
import org.tellervo.desktop.gui.menus.WindowMenu;
import org.tellervo.desktop.gui.menus.actions.FileOpenAction;
import org.tellervo.desktop.gui.menus.actions.GraphSeriesAction;
import org.tellervo.desktop.gui.menus.actions.InitDataGridAction;
import org.tellervo.desktop.gui.menus.actions.MeasureToggleAction;
import org.tellervo.desktop.gui.menus.actions.PrintAction;
import org.tellervo.desktop.gui.menus.actions.RemarkToggleAction;
import org.tellervo.desktop.gui.menus.actions.SaveAction;
import org.tellervo.desktop.gui.menus.actions.TruncateAction;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.IncompleteTridasDataException;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.util.ITRDBTaxonConverter;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasInterpretation;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasTridas;

public class EditorLite extends Editor {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(EditorLite.class);

	public EditorLite(Sample sample) {
		super(sample);

		this.metaView.setVisible(false);
		this.editorViewMenu.setVisible(false);
		tabbedPanel.setSelectedIndex(0);
	}
	protected void initDependentsPanel() {
		
	}
	
	@Override
	protected void initMetaView() {
		metaView = new BasicMetadataPanel();
		((BasicMetadataPanel) metaView).populateFromSample(sample);
	}

	@Override
	public void setDefaultFocus()
	{
		this.tabbedPanel.setSelectedIndex(0);

	}

	private void saveToDisk(String format, File file)
	{
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


		try{

			writer = TridasIO.getFileWriter(format);

			NumericalNamingConvention nc = new NumericalNamingConvention(filename);
			nc.setAddSequenceNumbersForUniqueness(false);
			writer.setNamingConvention(nc);

			TridasTridas container = (TridasTridas) getContainerForFile().clone();

			writer.load(container);
			// Actually save file(s) to disk
			writer.saveAllToDisk(path);


		}
		catch (IncompleteTridasDataException e){
			e.printStackTrace();
		} 
		catch (ConversionWarningException e) { } 
		catch (Exception ex){
			Alert.error(this, "Invalid format", "Invalid format : "+format);	
			return;
		}

		sample.clearModified();
		sample.setMeta(Metadata.FILENAME, file.getAbsolutePath());
		sample.setMeta(Metadata.LEGACY_FORMAT, format);
		sample.setMeta(Metadata.LEGACY_SAVED_BY_TELLERVO, true);
		sample.fireSampleMetadataChanged();

		App.prefs.setPref(PrefKey.EXPORT_FORMAT, format);
		App.prefs.setPref(PrefKey.FOLDER_LAST_SAVE, path);

	}
	
	private TridasTridas getContainerForFile()
	{

		TridasMeasurementSeries series = (TridasMeasurementSeries) getSample().getSeries();

		series.setTitle(((BasicMetadataPanel)metaView).txtTitle.getText());
		series.setDendrochronologist(((BasicMetadataPanel)metaView).txtAuthor.getText());
		
		if(!series.isSetInterpretation() || !series.getInterpretation().isSetFirstYear())
		{
			TridasInterpretation interp = new TridasInterpretation();
			
			interp.setFirstYear(getSample().getStart().tridasYearValue());
			series.setInterpretation(interp);
		}
		
		
		TridasGenericField gf = new TridasGenericField();
		gf.setName("keycode");
		gf.setType("xs:string");
		gf.setValue(((BasicMetadataPanel)metaView).txtKeycode.getText());
		series.getGenericFields().add(gf);

		TridasRadius radius = new TridasRadius();

		TridasSample sample = new TridasSample();

		TridasElement element = new TridasElement();
		element.setTaxon(ITRDBTaxonConverter.getControlledVocFromCode(((BasicMetadataPanel)metaView).txtSpecies.getText()));

		TridasObject object = new TridasObject();
		

		TridasProject proj = new TridasProject();

		radius.getMeasurementSeries().add(series);
		sample.getRadiuses().add(radius);
		element.getSamples().add(sample);
		object.getElements().add(element);
		proj.getObjects().add(object);


		// Extract the TridasProject
		TridasTridas container = new TridasTridas();
		container.getProjects().add(proj);

		return container;
	}

	
	public void saveAs()
	{
		

		
		// custom jfilechooser
		File file = null;
		String format = null;
		JFileChooser fc = new JFileChooser();

		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);

		// Loop through formats and create filters for each
		fc.setAcceptAllFileFilterUsed(false);
		ArrayList<DendroFileFilter> filters = TridasIO.getFileWritingFilterArray();
		Collections.sort(filters);
		for(DendroFileFilter filter : filters)
		{
			// No point trying to save to TRiDaS as there is all the metadata missing
			if(filter.getFormatName().startsWith("TRiDaS")) continue;
			// No point trying to save unstacked formats as there is only ever one series
			if(filter.getFormatName().endsWith("(unstacked)")) continue;


			fc.addChoosableFileFilter(filter);
			if(App.prefs.getPref(PrefKey.EXPORT_FORMAT, null)!=null)
			{
				if(App.prefs.getPref(PrefKey.EXPORT_FORMAT, null).equals(filter.getFormatName()))
				{
					fc.setFileFilter(filter);
				}
			}
		}


		// Pick the last used directory by default
		try{
			File lastDirectory = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_SAVE, null));
			if(lastDirectory != null){
				fc.setCurrentDirectory(lastDirectory);
			}
		} catch (Exception e)
		{
		}

		int retValue = fc.showSaveDialog(this);
		TricycleModelLocator.getInstance().setLastDirectory(fc.getCurrentDirectory());
		if (retValue == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			String formatDesc = fc.getFileFilter().getDescription();
			try{
				format = formatDesc.substring(0, formatDesc.indexOf("(")).trim();
			} catch (Exception e){}			
		}
		if (file == null) {
			return;
		}
		
		log.debug("Does file "+file.getAbsolutePath()+" exist? "+file.exists());
		
		// If overwriting file warn that metadata may be lost if the fil
		if(!file.exists() || 
				(sample.getMeta(Metadata.LEGACY_SAVED_BY_TELLERVO)!=null 
				&& !sample.getMeta(Metadata.LEGACY_SAVED_BY_TELLERVO).equals(true)))
		{
		}
		else
		{
			Object[] options = {"Yes",
                    "No",
                    "Cancel"};
			int n = JOptionPane.showOptionDialog(this,
			    "The file '"+file.getName()+"' already exists.\n\n" +
			    "Are you sure you want to overwrite this file?",
			    "Overwrite File?",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[2]);
			
			if(n != JOptionPane.YES_OPTION)
			{
				return;
			}
		}
		
		saveToDisk(format, file);

	}

	public void save()
	{
		if(!((BasicMetadataPanel)metaView).isMetadataSet())
		{
			Object[] options = {"Yes",
                    "No",
                    "Cancel"};
			int n = JOptionPane.showOptionDialog(this,
			    "You have not set any metadata for this file.  Depending on the output\n"+
				"format you choose, the keycode field can be especially important.\n\n"+
			    "Would you like to continue saving without metadata?",
			    "No metadata",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[2]);
			
			if(n != JOptionPane.YES_OPTION)
			{
				// Set focus to metadata tab
				this.tabbedPanel.setSelectedIndex(1);
				return;
			}

		}
		
		
		// Save without prompt if possible
		if(sample.getMeta(Metadata.FILENAME)!=null
				&& sample.getMeta(Metadata.LEGACY_FORMAT)!=null  )
		{
			if(sample.getMeta(Metadata.LEGACY_SAVED_BY_TELLERVO)!=null && 
					sample.getMeta(Metadata.LEGACY_SAVED_BY_TELLERVO).equals(true))
			{
				saveToDisk(sample.getMeta(Metadata.LEGACY_FORMAT).toString(), new File((String) sample.getMeta(Metadata.FILENAME)));
				return;
			}
			else
			{
				Object[] options = {"Yes",
	                    "No",
	                    "Cancel"};
				int n = JOptionPane.showOptionDialog(this,
				    "Tellervo has not previously saved to this file. In offline mode Tellervo\n" +
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
					saveToDisk(sample.getMeta(Metadata.LEGACY_FORMAT).toString(), new File(sample.getMeta(Metadata.FILENAME).toString()));
				}
				return;
			}


			
		}
		
		// No known filename so we must check
		saveAs();

	}

	@Override
	protected void initToolbar()
	{
		toolbar=  new JToolBar();

		// File Buttons
		Action fileOpenAction = new FileOpenAction(this);
		AbstractButton fileOpen = new TitlelessButton(fileOpenAction);
		toolbar.add(fileOpen);

		Action saveAction = new SaveAction(this);
		AbstractButton save = new TitlelessButton(saveAction);
		toolbar.add(save);

		// Edit Buttons
		Action measureAction = new MeasureToggleAction(this);
		AbstractButton measure = new TitlelessButton(measureAction);
		toolbar.add(measure);

		// Initialize data grid button
		Action initGridAction = new InitDataGridAction(this, dataView);
		AbstractButton initGrid = new TitlelessButton(initGridAction);
		toolbar.add(initGrid);

		// Remarks Button
		Action remarkAction = new RemarkToggleAction(this);
		AbstractButton toggleRemarks = new TitlelessButton(remarkAction);
		toolbar.add(toggleRemarks);

		// Tools Buttons
		//toolbar.addSeparator();
		//Action truncateAction = new TruncateAction(null, sample, this, null);
		//AbstractButton truncate = new TitlelessButton(truncateAction);
		//toolbar.add(truncate);

		// Graph Buttons
		toolbar.addSeparator();
		Action graphSeriesAction = new GraphSeriesAction(sample);
		AbstractButton graph = new TitlelessButton(graphSeriesAction);
		toolbar.add(graph);

		getContentPane().add(toolbar, BorderLayout.NORTH);
	}


	@Override
	protected void setupMenuBar()
	{
		// menubar
		// This must happen *after* initRolodex(), as dataview and elempanel come from it.
		JMenuBar menubar = new JMenuBar();

		menubar.add(new EditorLiteFileMenu(this, sample));
		editorEditMenu = new EditorEditMenu(sample, dataView, this);
		menubar.add(editorEditMenu);
		menubar.add(this.editorViewMenu);
		menubar.add(new EditorLiteToolsMenu(this, sample));
		menubar.add(new EditorLiteGraphMenu(this, sample));
		if (Platform.isMac())
			menubar.add(new WindowMenu(this));
		menubar.add(new HelpMenu());
		setJMenuBar(menubar);
	}
}
