package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.PopulateEditorDialog;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.gui.menus.FileMenu;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class EditInitDataGridAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public EditInitDataGridAction(AbstractEditor editor) {
        //super(I18n.getText("menus.edit.populateditor"), Builder.getIcon("inittable.png", 22));
        super("Initialize data grid", Builder.getIcon("inittable.png", 22));
        
        this.editor = editor;
        
        //putValue(SHORT_DESCRIPTION, I18n.getText("menus.edit.populateditor"));
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.edit.populateditor")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.edit.populateditor"));

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		PopulateEditorDialog dialog = new PopulateEditorDialog(editor);
		dialog.setVisible(true);
	}
	
}