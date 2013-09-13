package org.tellervo.desktop.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.menus.FileMenu;
import org.tellervo.desktop.gui.menus.actions.SaveAction;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.io.view.ImportDataOnly;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.IncompleteTridasDataException;
import org.tridas.io.formats.tucson.TucsonWriter;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasTridas;

public class EditorLiteFileMenu extends FileMenu {


	private static final long serialVersionUID = 1L;
	
	private Sample s;
	private JFrame parent;

	public EditorLiteFileMenu(JFrame f)
	{
		super(f);
		this.parent =f;
		this.s = null;
	}
	
	public EditorLiteFileMenu(Editor e, Sample s){
		super(e);
		this.s = s;
		this.parent = e;
		
	}
	
	@Override
	public void addNewOpenMenus() {
		
		filenew = Builder.makeMenuItem("menus.file.new", true, "filenew.png");
		filenew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				org.tellervo.desktop.editor.EditorFactory.newSeries(f);
			}
		});
		add(filenew);
		
		
		fileimportdataonly = Builder.makeMenu("menus.file.open2", "fileopen.png");
		
		for (final String s : TridasIO.getSupportedReadingFormats()) {
			
			JMenuItem importitem = new JMenuItem(s);

			importitem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Set up file chooser and filters
					AbstractDendroFileReader reader = TridasIO.getFileReader(s);
					DendroFileFilter filter = reader.getDendroFileFilter();
					File lastFolder = null;
					try{
						lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
					} catch (Exception e){}
					
					JFileChooser fc = new JFileChooser(lastFolder);
					fc.addChoosableFileFilter(filter);
					fc.setFileFilter(filter);
					
					int returnVal = fc.showOpenDialog(null);
						
					// Get details from user
				    if (returnVal == JFileChooser.APPROVE_OPTION) {
				        File file = fc.getSelectedFile();
				        ImportDataOnly importDialog = new ImportDataOnly(f, file, s);
				        importDialog.openEditors();
				        
						// Remember this folder for next time
						App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
					    
				    } else {
				    	return;
				    }

					
				}
				
			});
			
			fileimportdataonly.add(importitem);
		}
		add(fileimportdataonly);
		

	}
	

	@Override
	public void addIOMenus()
	{
		
	}

	
	@Override
	public void addPrintingMenus()
	{
		
	}
	
	@Override
	  protected void setMenusForNetworkStatus()
	  {

		  logoff.setVisible(false);  
		  logon.setVisible(false);  

	  }
	
	@Override
	public void addSaveMenu() {
		
		JMenuItem save = Builder.makeMenuItem("menus.file.save");
		
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
								
				if(s.getMeta(Metadata.FILENAME)!=null && s.getMeta(Metadata.LEGACY_FORMAT)!=null)
				{
					saveToDisk(s.getMeta(Metadata.LEGACY_FORMAT).toString(), new File(s.getMeta(Metadata.FILENAME).toString()));
				}
				else
				{
					saveAs();
				}
				
			}
			
		});
		
		add(save);
		
		 fileOfflineSaveAs = Builder.makeMenuItem("menus.file.saveas");
			
			if(f instanceof EditorLite)
			{
				fileOfflineSaveAs.setEnabled(true);
				
				fileOfflineSaveAs.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						saveAs();
						
					}


				
				});
			}
			
			add(fileOfflineSaveAs);
		
		
	}
	
	private void saveAs()
	{
		
		
		// custom jfilechooser
		File file = null;
		String format = null;
		JFileChooser fc = new JFileChooser();
	
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
								
		// Loop through formats and create filters for each
		ArrayList<DendroFileFilter> filters = TridasIO.getFileFilterArray();
		Collections.sort(filters);
		for(DendroFileFilter filter : filters)
		{
			fc.addChoosableFileFilter(filter);
		}

		// Pick the last used directory by default
		/*try{
			File lastDirectory = TricycleModelLocator.getInstance().getLastDirectory();
			if(lastDirectory != null){
				fc.setCurrentDirectory(lastDirectory);
			}
		} catch (Exception e)
		{
		}*/
		
		int retValue = fc.showSaveDialog(parent);
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
							
		saveToDisk(format, file);


	}
			
			
	private void saveToDisk(String format, File file)
	{
		AbstractDendroCollectionWriter writer = null;
		try{
			
			writer = TridasIO.getFileWriter(format);
			
			// Create a new converter based on a TridasProject
			NumericalNamingConvention nc = new NumericalNamingConvention(file.getName());
			writer.setNamingConvention(nc);

			writer.load(getContainerForFile());
			// Actually save file(s) to disk
			writer.saveAllToDisk(file.getParentFile().getAbsolutePath());
			
		}
		 catch (IncompleteTridasDataException e) {
				e.printStackTrace();
			} catch (ConversionWarningException e) {
		} 
		catch (Exception ex)
		{
			Alert.error("Invalid format", "Invalid format : "+format);	
			return;
		}
		
		s.clearModified();
		s.setMeta(Metadata.FILENAME, file.getName());
		
		if(parent instanceof EditorLite)
		{
			EditorLite editor = (EditorLite) parent;
			
			s.setMeta(Metadata.TITLE, editor.getMetadataPanel().txtTitle.getText());
			
		}
		
		s.fireSampleMetadataChanged();

	}
	
	private TridasTridas getContainerForFile()
	{
		if((f instanceof EditorLite))
		{
			
		}
		else
		{
			return null;
		}
		
		EditorLite ed = (EditorLite) f;
		TridasMeasurementSeries series = (TridasMeasurementSeries) ed.getSample().getSeries();
		TridasRadius radius = new TridasRadius();
		TridasSample sample = new TridasSample();
		TridasElement element = new TridasElement();
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
}
