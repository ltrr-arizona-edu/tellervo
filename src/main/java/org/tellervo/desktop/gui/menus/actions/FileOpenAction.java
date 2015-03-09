package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.view.FullEditor;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.io.view.ImportDataOnly;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.model.TricycleModelLocator;

public class FileOpenAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	private Window parent;
	
	public FileOpenAction(Window parent) {
        //super(I18n.getText("menus.file.open"), Builder.getIcon("fileopen.png", 22));
        super("Open", Builder.getIcon("fileopen.png", 22));
		putValue(SHORT_DESCRIPTION, "Open a series");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.open")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.open"));
        this.parent = parent;

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
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
		else
		{
			opendb(false);
		}
	}
	
	
	public static void opendb(boolean multi) {
		DBBrowser browser = new DBBrowser(App.mainWindow, true, multi);
		
		browser.setVisible(true);
		
		if(browser.getReturnStatus() == DBBrowser.RET_OK) {
			ElementList toOpen = browser.getSelectedElements();
			
			for(Element e : toOpen) {
				// load it
				Sample s;
				try {
					s = e.load();
				} catch (IOException ioe) {
					Alert.error(browser, I18n.getText("error.loadingSample"),
							I18n.getText("error.cantOpenFile") +":" + ioe.getMessage());
					continue;
				}

				OpenRecent.sampleOpened(new SeriesDescriptor(s));
				
				// open it
				new FullEditor(s);
			}
		}
	}
	
}