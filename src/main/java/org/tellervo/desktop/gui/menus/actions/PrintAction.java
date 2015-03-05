package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.print.SeriesReport;
import org.tellervo.desktop.ui.Builder;

public class PrintAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private AbstractEditor ed;
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public PrintAction(AbstractEditor ed) {
        //super(I18n.getText("menus.file.print"), Builder.getIcon("printer.png", 22));
        super("&Print [accel p]", Builder.getIcon("printer.png", 22));
        this.ed=ed;
        
        putValue(SHORT_DESCRIPTION, "Print this document");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.print")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.print"));
  
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SeriesReport.printReport(ed.getSample());
		
	}

}