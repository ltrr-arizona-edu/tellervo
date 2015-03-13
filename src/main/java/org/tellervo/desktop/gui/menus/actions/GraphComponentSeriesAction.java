package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class GraphComponentSeriesAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;

	
	public GraphComponentSeriesAction(AbstractEditor editor) {
        super(I18n.getText("menus.graph.components"));
        putValue(SHORT_DESCRIPTION, "Graph the component series");

        this.editor = editor;
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.graph.components")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.graph.components"));
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		new GraphWindow(editor.getSample().getElements());
		
	}
	
}