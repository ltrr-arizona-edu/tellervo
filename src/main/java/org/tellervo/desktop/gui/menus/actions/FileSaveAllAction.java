package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.view.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class FileSaveAllAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final FullEditor editor;
	
	/**
	 * Constructor for menus
	 * 
	 * @param ed
	 */
	public FileSaveAllAction(FullEditor ed) {
        //super(I18n.getText("menus.file.save"), Builder.getIcon("filesave.png", 22));
        super("Save all", Builder.getIcon("save_all.png", 22));
        editor=ed;
        putValue(SHORT_DESCRIPTION, "Save all current documents");

    }
	
	public FileSaveAllAction(FullEditor ed, Boolean b) {
        super("Save all", Builder.getIcon("save_all.png", 22));
        editor=ed;
        putValue(SHORT_DESCRIPTION, "Save all current documents");
    }
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		editor.saveAll();
	}
	
}