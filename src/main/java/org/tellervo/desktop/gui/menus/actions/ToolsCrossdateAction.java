package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.cross.CrossdateDialog;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class ToolsCrossdateAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public ToolsCrossdateAction(AbstractEditor editor) {
        super(I18n.getText("menus.tools.new_crossdate"), Builder.getIcon("crossdate.png", 22));
		putValue(SHORT_DESCRIPTION, I18n.getText("menus.tools.new_crossdate"));
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.tools.new_crossdate")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.tools.new_crossdate"));
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(editor.getSample()!=null)
		{
		
			Element secondary = new CachedElement(editor.getSample()); 
			new CrossdateDialog(ElementList.singletonList(secondary), secondary);
		}
		else
		{
			new CrossdateDialog();
		}
		
	}

}
