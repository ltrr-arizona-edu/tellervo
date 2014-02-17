package org.tellervo.desktop.gui.menus;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.editor.EditorLite;
import org.tellervo.desktop.io.view.ImportDataOnly;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.model.TricycleModelLocator;

public class EditorLiteFileMenu extends FileMenu {

	private static final Logger log = LoggerFactory.getLogger(EditorLiteFileMenu.class);

	private static final long serialVersionUID = 1L;
	
	private Sample s;
	private JFrame editor;

	public EditorLiteFileMenu(JFrame f)
	{
		super(f);
		this.editor =f;
		this.s = null;
	}
	
	public EditorLiteFileMenu(Editor e, Sample s){
		super(e);
		this.s = s;
		this.editor = e;
		
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
		
		
		fileimportdataonly = Builder.makeMenuItem("menus.file.open2", true, "fileopen.png");
		fileimportdataonly.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				openLegacyFile(editor);
				
			}
		
		});
	
		add(fileimportdataonly);
		addSeparator();

	}
	
	public static void openLegacyFile(Window parent)
	{
		// custom jfilechooser
		File file = null;
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
			if(App.prefs.getPref(PrefKey.IMPORT_FORMAT, null)!=null)
			{
				if(App.prefs.getPref(PrefKey.IMPORT_FORMAT, null).equals(filter.getFormatName()))
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
		
		int retValue = fc.showOpenDialog(parent);
		TricycleModelLocator.getInstance().setLastDirectory(fc.getCurrentDirectory());
		if (retValue == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			String formatDesc = fc.getFileFilter().getDescription();
			// Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
			try{
				//format = formatDesc.substring(0, formatDesc.indexOf("(")).trim();
				format = ((DendroFileFilter)fc.getFileFilter()).getFormatName();
				App.prefs.setPref(PrefKey.IMPORT_FORMAT, format);
			} catch (Exception e){}
		}
		if (file == null) {
			return;
		}
				
        ImportDataOnly importDialog = new ImportDataOnly(parent, file, format);
        importDialog.openEditorLites();
	    


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
		save.setIcon(Builder.getIcon("filesave.png", 22));
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				((EditorLite)editor).save();
				
			}
			
		});
		
		add(save);

		
		
		
		
		 fileOfflineSaveAs = Builder.makeMenuItem("menus.file.saveas");
		 fileOfflineSaveAs.setIcon(Builder.getIcon("saveas.png", 22));
		 
			if(f instanceof EditorLite)
			{
				fileOfflineSaveAs.setEnabled(true);
				
				fileOfflineSaveAs.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						((EditorLite)f).saveAs();	
					}
				});
			}
			
			add(fileOfflineSaveAs);

			addSeparator();
		
	}
	
	
	

}
