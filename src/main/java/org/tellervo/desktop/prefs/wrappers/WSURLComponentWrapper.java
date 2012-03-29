package org.tellervo.desktop.prefs.wrappers;

import java.awt.event.FocusEvent;

import javax.swing.text.JTextComponent;

public class WSURLComponentWrapper extends TextComponentWrapper {

	public WSURLComponentWrapper(JTextComponent field, String prefName,
			String defaultValue) {
		super(field, prefName, defaultValue);

	}
	
	public void focusLost(FocusEvent e) {
		String text = field.getText();
		
		if(text.endsWith("/tellervo"))
		{
			text = text+"/";
			field.setText(text);
		}

		// set a null if we have a zero length string
		setValue(text.length() > 0 ? text : null);
	}

}
