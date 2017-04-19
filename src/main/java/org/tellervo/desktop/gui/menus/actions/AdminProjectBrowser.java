package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.curation.ProjectBrowserDialog;
import org.tellervo.desktop.ui.Builder;

public class AdminProjectBrowser extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Window parent;
	
	public AdminProjectBrowser(Window parent) {
        super("Project browser", Builder.getIcon("project.png", 22));
		putValue(SHORT_DESCRIPTION, "Browse all projects");
        this.parent = parent;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ProjectBrowserDialog browser = new ProjectBrowserDialog(false);
		browser.pack();
		browser.setLocationRelativeTo(parent);
		browser.setVisible(true);
		
	}

}
