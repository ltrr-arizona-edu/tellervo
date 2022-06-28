package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

public class FileSaveAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final JFrame f;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public FileSaveAction(JFrame frame) {
        //super(I18n.getText("menus.file.save"), Builder.getIcon("filesave.png", 22));
        super("Save", Builder.getIcon("filesave.png", 22));
        f=frame;
        putValue(SHORT_DESCRIPTION, "Save the current document");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.save")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.save"));
    }
	
	public FileSaveAction(JFrame frame, Boolean b) {
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

		if (doc.isSaved()) {
			if(doc instanceof FullEditor)
			{
				
				FullEditor editor = (FullEditor) doc;
				
				OpenRecent.sampleOpened(new SeriesDescriptor(editor.getSample()));
			}
		}
		
	}
	
}