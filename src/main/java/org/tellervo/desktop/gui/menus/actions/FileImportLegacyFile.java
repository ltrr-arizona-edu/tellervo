package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.seriesidentity.IdentifySeriesPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;

public class FileImportLegacyFile extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractDendroFileReader reader;
	private AbstractEditor editor;
	
	public FileImportLegacyFile(AbstractDendroFileReader reader, AbstractEditor editor)
	{
		super(reader.getFormat().getShortName());
		this.reader = reader;
		this.editor = editor;
	}
	
	
	public AbstractDendroFileReader getReader()
	{
		return reader;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		// Set up file chooser and filters
		DendroFileFilter filter = reader.getDendroFileFilter();
		File lastFolder = null;
		try{
			lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
		} catch (Exception e){}
		
		JFileChooser fc = new JFileChooser(lastFolder);
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(true);
		int returnVal = fc.showOpenDialog(editor);
			
		// Get details from user
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        
	    	File[] files = fc.getSelectedFiles();
	    		        
			// Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, files[0].getPath());
			
	    	IdentifySeriesPanel.show(files, reader.getFormat());

		    
	    } else {
	    	return;
	    }

		
	}


}
