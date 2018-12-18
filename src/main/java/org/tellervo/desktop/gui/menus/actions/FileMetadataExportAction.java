package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.seriesidentity.IdentifySeriesPanel;
import org.tellervo.desktop.metadataexport.MetadataExportDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;

import com.rediscov.util.ICMSExporter;
import com.rediscov.util.ICMSImporter;

public class FileMetadataExportAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public FileMetadataExportAction()
	{
		super("Spreadsheet export");
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		
		MetadataExportDialog dialog = new MetadataExportDialog();
		dialog.setVisible(true);
	    
		

		
	}


}
