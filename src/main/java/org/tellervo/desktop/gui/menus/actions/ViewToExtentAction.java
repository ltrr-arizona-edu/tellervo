package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

public class ViewToExtentAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private FullEditor parent;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public ViewToExtentAction(FullEditor parent) {
        //super(I18n.getText("menus.view.addlayers"), Builder.getIcon("addlayers.png", 22));
        super("Zoom to extent", Builder.getIcon("addlayers.png", 22));
        this.parent=parent;
        putValue(SHORT_DESCRIPTION, "Zoom to extent");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.view.addlayers")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.view.addlayers"));
    }
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		parent.zoomToExtent();
		
	}
	
}