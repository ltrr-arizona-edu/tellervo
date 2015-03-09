package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwindx.examples.util.ScreenShotAction;

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

import org.tellervo.desktop.gis.GISPanel;
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

public class FileExportMapAction extends ScreenShotAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public FileExportMapAction(GISPanel panel) {
		super(panel.getWwd());
		
        putValue(Action.SMALL_ICON, Builder.getIcon("captureimage.png", 22));
        putValue(Action.LARGE_ICON_KEY, Builder.getIcon("captureimage.png", 22));
        //putValue(Action.NAME, I18n.getText("menus.file.import"));
        putValue(SHORT_DESCRIPTION, "Export map as image");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.import")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.import"));
  
    }
	
}