package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.tellervo.desktop.gis.AddGISDataDialog;
import org.tellervo.desktop.gis.GISFrame;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

import com.dmurph.mvc.MVCEvent;

public class AddLayersAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private GISFrame parent;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public AddLayersAction(GISFrame parent) {
        super(I18n.getText("menus.view.addlayers"), Builder.getIcon("addlayers.png", 22));
        this.parent=parent;
        putValue(SHORT_DESCRIPTION, "Add new data layer");
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.view.addlayers")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.view.addlayers"));
    }
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		AddGISDataDialog dialog = new AddGISDataDialog(parent);
		dialog.setVisible(true);
		
	}
	
}