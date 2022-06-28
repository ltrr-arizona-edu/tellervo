package org.tellervo.desktop.gui.menus.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.admin.SetPasswordUI;
import org.tellervo.desktop.admin.view.PermissionByEntityDialog;
import org.tellervo.desktop.curation.BoxCuration;
import org.tellervo.desktop.curation.LoanDialog;
import org.tellervo.desktop.curation.SampleCuration;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.labels.ui.LabelPrintingDialog;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class AdminCurationMenuNewLoanAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	AbstractEditor editor;
	
	public AdminCurationMenuNewLoanAction(AbstractEditor editor) {
        super("New Loan", Builder.getIcon("box.png", 22));
		putValue(SHORT_DESCRIPTION, "New Loan...");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));
		this.editor = editor;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		LoanDialog.showNewLoanDialog(editor);
		}		
}
