package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.io.TridasDoc;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;

public class GraphComponentSeriesAction extends AbstractAction{
	private final static Logger log = LoggerFactory.getLogger(GraphComponentSeriesAction.class);

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
		
		Sample sample = editor.getSample();
		
		try {
			sample.getLoader().loadBasic();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ElementList elements = sample.getElements();
				
		log.debug("Number of elements to graph: "+elements.size());
		
		for(Element element : elements)
		{
			try {
				element.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		new GraphWindow(elements);
		
	}
	
}