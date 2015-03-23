package org.tellervo.desktop.gui.widgets;

import javax.swing.Action;
import javax.swing.JButton;

public class TitlelessButton extends JButton {

	private static final long serialVersionUID = 1L;

	public TitlelessButton(Action action) {
		super(action);
		this.setFocusable(false);
	}
	
	@Override
	public void setText(String text) {
		super.setText(null);
	}
	
	@Override
	public String getText() {
		return null;
	}
	
}
