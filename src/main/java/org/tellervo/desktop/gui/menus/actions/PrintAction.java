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

public class PrintAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private Sample sample;
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public PrintAction(Sample sample) {
        //super(I18n.getText("menus.file.print"), Builder.getIcon("printer.png", 22));
        super("&Print [accel p]", Builder.getIcon("printer.png", 22));
        this.sample=sample;
        
        putValue(SHORT_DESCRIPTION, "Print this document");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.print")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.print"));
  
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SeriesReport.printReport(sample);
		
	}

}