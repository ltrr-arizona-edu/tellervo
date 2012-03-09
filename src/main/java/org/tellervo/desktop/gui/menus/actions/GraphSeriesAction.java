package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class GraphSeriesAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final Sample sample;

	
	public GraphSeriesAction(Sample sample) {
        super(I18n.getText("menus.graph.activeSeries"), Builder.getIcon("graph.png", 22));
        putValue(SHORT_DESCRIPTION, "Graph the current series");

        this.sample = sample;
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.graph.activeSeries")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.graph.activeSeries"));
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		new GraphWindow(sample);
		
	}
	
}