package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.io.control.OpenExportEvent;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class FileExportDataAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor ;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public FileExportDataAction(AbstractEditor editor) {
        super(I18n.getText("menus.file.export"), Builder.getIcon("fileexport.png", 22));
        this.editor = editor;
        putValue(SHORT_DESCRIPTION, "Export data");
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.export")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.export"));
    }
		
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(editor.getSample()!=null)
		{
			OpenExportEvent event = new OpenExportEvent(editor.getSamples(), editor.getSample());
			event.dispatch();
		}
		else
		{
			OpenExportEvent event = new OpenExportEvent();
			event.dispatch();
		}
		
		
	}
	
}