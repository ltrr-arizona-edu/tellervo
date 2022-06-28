package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gis2.DBMapSearchDialog;
import org.tellervo.desktop.gis2.WWJPanel;
import org.tellervo.desktop.ui.Builder;

public class MapDatabaseLayerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	protected WWJPanel wwjPanel;
	
	public MapDatabaseLayerAction(FullEditor editor) {
        super("Database layer", Builder.getIcon("dblayer.png", 22));
		putValue(SHORT_DESCRIPTION, "Add a map layer by searching database");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {

		DBMapSearchDialog dialog = new DBMapSearchDialog(editor);
		dialog.setVisible(true);
	}
		

}



