package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.PrintableDocument;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.print.SeriesReport;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

public class MeasureToggleAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public MeasureToggleAction(AbstractEditor abstractEditor) {
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