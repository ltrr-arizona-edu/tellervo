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

import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

public class RadioButtonWrapper extends ActionWrapper<String> {

	public RadioButtonWrapper(JRadioButton buttons[], String prefName, Object defaultValue) {
		super(prefName, defaultValue, String.class);

		String selectedValue = getValue();
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i].getActionCommand().equalsIgnoreCase(selectedValue))
				buttons[i].setSelected(true);
			
			buttons[i].addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setValue(e.getActionCommand());
	}
}
