package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

import com.dmurph.mvc.MVCEvent;

public class ExportDataAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final String argMVCKey;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public ExportDataAction(String argMVCKey) {
        super(I18n.getText("menus.file.export"), Builder.getIcon("fileexport.png", 22));
        this.argMVCKey=argMVCKey;
        putValue(SHORT_DESCRIPTION, "Export data");
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.export")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.export"));
    }
	
	/**
	 * Constructor for toolbars
	 * 
	 * @param frame
	 */
	public ExportDataAction(String argMVCKey, Boolean b) {
        super("", Builder.getIcon("fileexport.png", 22));
        this.argMVCKey=argMVCKey;
        putValue(SHORT_DESCRIPTION, "Export data");
    }
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		MVCEvent event = new MVCEvent(argMVCKey);
		event.dispatch();
		
	}
	
}