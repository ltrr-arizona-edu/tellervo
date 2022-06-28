package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.admin.view.UserGroupAdminView;
import org.tellervo.desktop.ui.Builder;

public class AdminUsersAndGroupsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public AdminUsersAndGroupsAction() {
        super("Users and groups", Builder.getIcon("edit_group.png", 22));
		putValue(SHORT_DESCRIPTION, "Show Tellervo users and groups");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		UserGroupAdminView.main();		
	}

}
