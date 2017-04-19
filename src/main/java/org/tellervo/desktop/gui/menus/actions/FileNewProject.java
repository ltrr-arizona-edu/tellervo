package org.tellervo.desktop.gui.menus.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.curation.ProjectBrowserDialog;
import org.tellervo.desktop.ui.Builder;

public class FileNewProject extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Window parent;
	
	public FileNewProject(Window parent) {
        super("New project", Builder.getIcon("project.png", 22));
		putValue(SHORT_DESCRIPTION, "Create a new project");
        this.parent = parent;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ProjectBrowserDialog browser = new ProjectBrowserDialog(true);
		browser.pack();
		browser.setLocationRelativeTo(parent);
		browser.setVisible(true);
		
	}

}
