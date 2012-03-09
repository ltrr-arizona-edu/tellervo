package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.menus.AdminMenu;
import org.tellervo.desktop.gui.menus.FileMenu;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

public class FileOpenAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public FileOpenAction() {
        super(I18n.getText("menus.file.open"), Builder.getIcon("fileopen.png", 22));
        putValue(SHORT_DESCRIPTION, "Open a series");
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.open")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.open"));

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		FileMenu.opendb();
	}
	
}