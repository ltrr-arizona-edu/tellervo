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

import java.awt.event.ItemEvent;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

import org.tellervo.desktop.prefs.Prefs.PrefKey;


// this one is nice and simple! :)

public class CheckBoxWrapper extends ItemWrapper<Boolean> {
	public CheckBoxWrapper(JCheckBox cb, String prefName, Boolean defaultValue) {
		super(prefName, defaultValue, Boolean.class);
		
		cb.setSelected(getValue());
		cb.addItemListener(this);
	}
	
	public CheckBoxWrapper(JCheckBox cb, PrefKey key, Boolean defaultValue) {
		super(key.getValue(), defaultValue, Boolean.class);
		
		cb.setSelected(getValue());
		cb.addItemListener(this);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		setValue(((AbstractButton)e.getSource()).isSelected());
	}
}
