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

public class SaveAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final JFrame f;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public SaveAction(JFrame frame) {
        //super(I18n.getText("menus.file.save"), Builder.getIcon("filesave.png", 22));
        super("&Save [accel S]", Builder.getIcon("filesave.png", 22));
        f=frame;
        putValue(SHORT_DESCRIPTION, "Save the current document");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.save")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.save"));
    }
	
	public SaveAction(JFrame frame, Boolean b) {
        super("", Builder.getIcon("filesave.png", 22));
        f=frame;
        putValue(SHORT_DESCRIPTION, "Save the current document");
    }
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// get doc
		SaveableDocument doc = (SaveableDocument) f;

		// save
		doc.save();

		// add to the recently opened files list if the user actually saved
		// also, the user can try to save a document they didn't do anything to. argh.
		if (doc.isSaved() && doc.getFilename() != null) {
			if(doc.getSavedDocument() instanceof Sample)
				OpenRecent.sampleOpened(new SeriesDescriptor((Sample) doc.getSavedDocument()));
			else
				OpenRecent.fileOpened(doc.getFilename());
		}
		
	}
	
}