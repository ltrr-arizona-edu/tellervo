package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridasSeries;

public class TagSeries extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private ITridasSeries series;
	
	public TagSeries(ITridasSeries series) {
        super("Tag series", Builder.getIcon("zoomtoextent.png", 22));
		putValue(SHORT_DESCRIPTION, "Tag series");
        this.series = series;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		
		
	}

}
