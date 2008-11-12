package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import say.swing.JFontChooser;

public class FontButtonWrapper extends ActionWrapper<Font> {
	public FontButtonWrapper(JButton button, String prefName, Font defaultFont) {
		super(prefName, defaultFont, Font.class);
		
		updateButton(button);
		button.addActionListener(this);
	}
	
	private void updateButton(JButton button) {
		Font f = getValue();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(f.getFamily());
		sb.append(' ');
		sb.append(f.getSize());
		
		if(f.isBold()) {
			sb.append(' ');
			sb.append("bold");
		}
		
		if(f.isItalic()) {
			sb.append(' ');
			sb.append("italic");
		}
		
		button.setText(sb.toString());
		button.setFont(f);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		JFontChooser chooser = new JFontChooser();
		chooser.setSelectedFont(getValue());
		int ret = chooser.showDialog(button);
		
		if(ret == JFontChooser.OK_OPTION) {
			Font font = chooser.getSelectedFont();
			setValue(font);
			
			updateButton(button);
		}
	}
}
