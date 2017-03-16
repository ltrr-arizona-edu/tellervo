package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.seriesidentity.IdentifySeriesPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;

import com.rediscov.util.ICMSImporter;

public class FileICMSImportAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public FileICMSImportAction(AbstractEditor editor)
	{
		super("Import ICMS File");
		this.editor = editor;
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ICMS XML export file", "xml");
		
		File lastFolder = null;
		try{
			lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
		} catch (Exception e){}
		
		JFileChooser fc = new JFileChooser(lastFolder);
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		/*int returnVal = fc.showOpenDialog(editor);
			
		// Get details from user
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        
	    	File file = fc.getSelectedFile();
	    		        
	    	// Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getAbsolutePath());
	    	
	    	ICMSImporter importer = new ICMSImporter(file.getAbsolutePath());
			importer.doImport();
			
				
	    }
	    */
	    
    	ICMSImporter importer = new ICMSImporter("/home/pbrewer/Dropbox/export4.xml");
		importer.doImport();
		
	    
		

		
	}


}
