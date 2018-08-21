package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.labelgen.LabelGenWizard;
import org.tellervo.desktop.ui.Builder;

public class AdminLabelGenAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public AdminLabelGenAction() {
        super("Label wizard", Builder.getIcon("barcode.png", 22));
		putValue(SHORT_DESCRIPTION, "Label wizard");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		LabelGenWizard wizard = new LabelGenWizard(null);
		
		if(wizard.wasCancelled()) return;
		
		wizard.openLabelsPDF();
	}		
	

}
