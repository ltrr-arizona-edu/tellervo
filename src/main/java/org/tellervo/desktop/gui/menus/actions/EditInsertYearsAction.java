package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.AskNumber;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class EditInsertYearsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public EditInsertYearsAction(AbstractEditor editor) {
        super("Insert multiple years", Builder.getIcon("insertyear.png", 22));
		putValue(SHORT_DESCRIPTION, "Insert multiple years");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

		this.editor = editor;
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			// first, get the number of years...
			int value = AskNumber.getNumber(
					editor, I18n.getText("menus.edit.insert_years"),
					I18n.getText("question.insertHowManyYears"), 2);
			
			String labels[] = {I18n.getText("general.blank"), I18n.getText("general.defaultValue") +" "+Sample.missingRingValue+"", I18n.getText("general.cancel")};
			
			int ret = JOptionPane.showOptionDialog(
					editor,
					I18n.getText("question.whatShouldInsertYearsBeSetTo"),
					I18n.getText("general.insert") + " " + value + " " + I18n.getText("general.years"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, labels,
					labels[0]);
			
			switch(ret) {
			case 0:
				editor.getSeriesDataMatrix().insertYears(0, value);
				break;
			case 1:
				editor.getSeriesDataMatrix().insertYears(new Integer(Sample.missingRingValue), value);
				break;
			case 2: // cancel
				break;
			}					
		} catch(UserCancelledException uce) {
			return;
		}
		
	}

	
	
}
