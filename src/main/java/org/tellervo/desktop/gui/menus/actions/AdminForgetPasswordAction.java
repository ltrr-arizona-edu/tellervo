package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.admin.SetPasswordUI;
import org.tellervo.desktop.admin.view.PermissionByEntityDialog;
import org.tellervo.desktop.gui.menus.AdminMenu;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class AdminForgetPasswordAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public AdminForgetPasswordAction() {
        super("Forget password", Builder.getIcon("forgetpassword.png", 22));
		putValue(SHORT_DESCRIPTION, "Forget Password");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		AdminMenu.forgetPassword()		;
	}		
}
