package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.PopulateEditorDialog;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.gui.menus.FileMenu;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class InitDataGridAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private Window parent;
	private SeriesDataMatrix dataView;
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public InitDataGridAction(Window parent, SeriesDataMatrix dataView) {
        super(I18n.getText("menus.edit.populateditor"), Builder.getIcon("inittable.png", 22));
        
        this.parent = parent;
        this.dataView = dataView;
        
        putValue(SHORT_DESCRIPTION, I18n.getText("menus.edit.populateditor"));
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.edit.populateditor")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.edit.populateditor"));

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		PopulateEditorDialog dialog = new PopulateEditorDialog(parent, dataView);
		dialog.setVisible(true);
	}
	
}