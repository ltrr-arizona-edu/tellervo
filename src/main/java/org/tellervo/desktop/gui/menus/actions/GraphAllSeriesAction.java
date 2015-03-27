package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class GraphAllSeriesAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;

	
	public GraphAllSeriesAction(AbstractEditor editor) {
        super(I18n.getText("menus.graph.everything"), Builder.getIcon("graph.png", 22));
        putValue(SHORT_DESCRIPTION, "Graph all series");

        this.editor = editor;
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.graph.everything")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.graph.everything"));
        
		/*if(editor.getSample().getElements() != null && editor.getSample().getElements().size() > 0)
		{
			this
		}*/

        
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		GraphWindow graphWindow = new GraphWindow(editor.getSamples().get(0));

		
		for (int i=1; i < editor.getSamples().size(); i++){
			
			Sample s = editor.getSamples().get(i);
			graphWindow.add(s);
		}
		
		
	}
	
}