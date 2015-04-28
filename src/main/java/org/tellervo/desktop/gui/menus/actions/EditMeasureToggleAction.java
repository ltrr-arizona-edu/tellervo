package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.ui.Builder;

public class EditMeasureToggleAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public EditMeasureToggleAction(AbstractEditor abstractEditor) {
       // super(I18n.getText("menus.edit.start_measuring"), Builder.getIcon("measure.png", 22));
        super("Start measuring [F5]", Builder.getIcon("measure.png", 22));
                
        this.editor = abstractEditor;
        putValue(SHORT_DESCRIPTION, "Start/stop measuring");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.edit.start_measuring")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.edit.start_measuring"));
  
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(editor!=null)
		{
			editor.toggleMeasuring();
		}
		
	}

}