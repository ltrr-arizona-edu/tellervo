package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.ui.Builder;

public class GraphCurrentSeriesAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;

	
	public GraphCurrentSeriesAction(AbstractEditor editor) {
       // super(I18n.getText("menus.graph.activeSeries"), Builder.getIcon("graph.png", 22));
        super("&Graph active series [accel G]", Builder.getIcon("graph.png", 22));
        putValue(SHORT_DESCRIPTION, "Graph the current series");

        this.editor = editor;
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.graph.activeSeries")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.graph.activeSeries"));
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		new GraphWindow(editor.getSample());
		
	}
	
}