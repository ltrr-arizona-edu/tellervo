package org.tellervo.desktop.gui.widgets;

import javax.swing.Action;
import javax.swing.JButton;

public class TitlelessButton extends JButton {

	public TitlelessButton(Action action) {
		super(action);
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
