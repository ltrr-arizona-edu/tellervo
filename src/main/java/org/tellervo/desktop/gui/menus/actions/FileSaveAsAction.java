package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

public class FileSaveAsAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final JFrame f;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public FileSaveAsAction(JFrame frame) {
        //super(I18n.getText("menus.file.save"), Builder.getIcon("filesave.png", 22));
        super("Save as", Builder.getIcon("filesave.png", 22));
        f=frame;
        putValue(SHORT_DESCRIPTION, "Save the current document with a new name");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.save")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.save"));
    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// get doc
		
		if(f instanceof LiteEditor)
		{
			LiteEditor editor  = (LiteEditor) f;
			
			// save
			try {
				editor.saveAs();
			} catch (Exception e) {
				Alert.error(editor, "Error saving", "Error saving file"+e.getLocalizedMessage());
				return;
			}

			// add to the recently opened files list if the user actually saved
			// also, the user can try to save a document they didn't do anything to. argh.
			if (editor.isSaved() && editor.getFilename() != null) {
				if(editor.getSavedDocument() instanceof Sample)
					OpenRecent.sampleOpened(new SeriesDescriptor((Sample) editor.getSavedDocument()));
				else
					OpenRecent.fileOpened(editor.getFilename());
			}
			
		}
		

		
		
		

		
	}
	
}