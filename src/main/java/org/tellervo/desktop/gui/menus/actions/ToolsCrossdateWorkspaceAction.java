package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import org.tellervo.desktop.cross.CrossdateDialog;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class ToolsCrossdateWorkspaceAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static final boolean Sample = false;
	private AbstractEditor editor;
	
	public ToolsCrossdateWorkspaceAction(AbstractEditor editor) {
        super(I18n.getText("Crossdate all series in workspace"), Builder.getIcon("crossdate.png", 22));
		putValue(SHORT_DESCRIPTION, I18n.getText("menus.tools.new_crossdate"));
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.tools.new_crossdate")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.tools.new_crossdate"));
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ElementList listOfElements = new ElementList();
		if(editor.getSample()!=null)
		{
			for(Sample s : editor.getSamples())
			{
				 Element secondary = new CachedElement(s);
				 listOfElements.add(secondary);
			}
						
			new CrossdateDialog(listOfElements, listOfElements.get(0));
		}
		else
		{
			new CrossdateDialog();
		}
		
	}

}
