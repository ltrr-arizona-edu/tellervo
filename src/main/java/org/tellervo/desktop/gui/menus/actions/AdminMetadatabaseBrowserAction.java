package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.menus.AdminMenu;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

public class AdminMetadatabaseBrowserAction extends AbstractAction{
	private final static Logger log = LoggerFactory.getLogger(AdminMetadatabaseBrowserAction.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public AdminMetadatabaseBrowserAction() {
        //super(I18n.getText("menus.admin.metadatabrowser"), Builder.getIcon("database.png", 22));
        super("&Metadata browser [accel M]", Builder.getIcon("database.png", 22));
        putValue(SHORT_DESCRIPTION, "Browse metadatabase");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.admin.metadatabrowser")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.admin.metadatabrowser"));

    }
			
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		AdminMenu.metadataBrowser();
	}
	
}