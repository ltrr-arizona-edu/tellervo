/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import com.l2fprod.common.swing.JFontChooser;

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

		Font font = chooser.showFontDialog(null, "Choose font");

		setValue(font);
			
		updateButton(button);
	}
}
