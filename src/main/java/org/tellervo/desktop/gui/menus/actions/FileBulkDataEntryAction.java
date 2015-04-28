package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

import com.dmurph.mvc.MVCEvent;

public class FileBulkDataEntryAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final String argMVCKey;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public FileBulkDataEntryAction(String argMVCKey) {
       // super(I18n.getText("menus.file.bulkimport"), Builder.getIcon("bulkDataEntry.png", 22));
        super("Bulk data entry", Builder.getIcon("bulkDataEntry.png", 22));
        
        this.argMVCKey=argMVCKey;
        putValue(SHORT_DESCRIPTION, "Bulk data entry");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.export")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.export"));
    }
	
	
	/**
	 * Constructor for toolbars
	 * 
	 * @param frame
	 */
	public FileBulkDataEntryAction(String argMVCKey, Boolean b) {
        super("", Builder.getIcon("bulkDataEntry.png", 22));
        this.argMVCKey=argMVCKey;
        putValue(SHORT_DESCRIPTION, "Bulk data entry");
    }
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		MVCEvent event = new MVCEvent(argMVCKey);
		event.dispatch();
		
	}
	
}