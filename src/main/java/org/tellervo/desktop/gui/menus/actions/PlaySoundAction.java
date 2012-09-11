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

import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.PrintableDocument;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.print.SeriesReport;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

public class PlaySoundAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final SystemSound snd;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public PlaySoundAction(SystemSound snd) {
        super("Play", Builder.getIcon("play.png", 16));
                
        this.snd = snd;
        putValue(SHORT_DESCRIPTION, "Play sound");
        
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(snd!=null)
		{
			SoundUtil.playSystemSound(snd);
		}

		
	}

}