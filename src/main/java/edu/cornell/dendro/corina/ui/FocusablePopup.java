package edu.cornell.dendro.corina.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.SwingUtilities;

public class FocusablePopup {
	private Dialog popupDialog;
	
	private FocusablePopup(Dialog popupDialog) {
		this.popupDialog = popupDialog;
	}
	
	public static FocusablePopup getPopup(Component owner, Component contents, int x, int y) {
		Window ownerWindow = (owner == null) ? null : SwingUtilities.windowForComponent(owner);
		
		Dialog dialog;
		
		if(ownerWindow == null || ownerWindow instanceof Frame)
			dialog = new Dialog((Frame) ownerWindow);
		else if(ownerWindow instanceof Dialog)
			dialog = new Dialog((Dialog) ownerWindow);
		else
			throw new IllegalArgumentException("owner is not in a frame or dialog???");
		
		dialog.setUndecorated(true);
		dialog.setModal(false);
		dialog.setAlwaysOnTop(true);
		dialog.setLocation(x, y);
		dialog.setLayout(new BorderLayout());
		dialog.add(contents, BorderLayout.CENTER);
		dialog.pack();
		
		return new FocusablePopup(dialog);
	}
	
	public void show() {
		popupDialog.setVisible(true);
	}
	
	public void hide() {
		popupDialog.setVisible(false);
		popupDialog.dispose();
	}
}
