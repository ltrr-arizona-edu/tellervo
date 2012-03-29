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
package org.tellervo.desktop.prefs.wrappers;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

/**
 * Wraps a JTextField that contains a string; updates pref when focus lost
 * 
 * @author lucasm
 */

public class TextComponentWrapper extends PrefWrapper<String> implements FocusListener {
	protected JTextComponent field;
	
	public TextComponentWrapper(JTextComponent field, String prefName, String defaultValue) {
		super(prefName, defaultValue, String.class);
		
		this.field = field;
		field.addFocusListener(this);
		field.setText(getValue() == null ? "" : getValue());
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		String text = field.getText();
		
		// set a null if we have a zero length string
		setValue(text.length() > 0 ? text : null);
	}
	
	/**
	 * Only allow numeric input here!
	 * 
	 * @param canHaveSign
	 */
	public void setNumbersOnly(final boolean canHaveSign) {		
		field.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char k = ke.getKeyChar();
	
				// always allow minuses
				if(Character.isDigit(k))
					return;
				
				// only allow a or at the beginning
				if(canHaveSign && k == KeyEvent.VK_MINUS) {
					if(field.getCaretPosition() != 0)
						ke.consume();
					return;
				}
				
				// only allow one period
				if(k == KeyEvent.VK_PERIOD) {
					String txt = field.getText();
					
					if(txt.indexOf(KeyEvent.VK_PERIOD) != -1)
						ke.consume();
					return;
				}
				
				// not here!
				ke.consume();
			}
		});
	}

}
