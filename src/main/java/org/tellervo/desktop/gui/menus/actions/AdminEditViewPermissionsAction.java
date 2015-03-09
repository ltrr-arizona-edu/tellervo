package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.admin.view.PermissionByEntityDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class AdminEditViewPermissionsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public AdminEditViewPermissionsAction() {
        super("Edit/View permissions", Builder.getIcon("trafficlight.png", 22));
		putValue(SHORT_DESCRIPTION, "Show Tellervo users and groups");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		ITridas returned = TridasEntityPickerDialog.pickEntity(null, "Pick Sample", TridasSample.class, EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT);
		
		if(returned==null) return;
		
		PermissionByEntityDialog.showDialog(returned);
		}

}
