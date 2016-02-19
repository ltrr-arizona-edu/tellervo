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
import java.text.DecimalFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.jfree.util.Log;
import org.tellervo.desktop.prefs.Prefs.PrefKey;


public class ComboByIndexWrapper extends ItemWrapper<Integer> {
	
	String[] options;
	
	
	public ComboByIndexWrapper(JComboBox cbo, PrefKey key, Object defaultValue, String[]values) {
		super(key, defaultValue, Integer.class);
		initGeneric(cbo, values);
	}
			
	private void initGeneric(JComboBox cbo, String[] values)
	{
		// show a sample for each format thingy...
		options = values;
		int selectedIdx = getValue();
		
		cbo.setModel(new DefaultComboBoxModel(values));
		try{
			if(selectedIdx >= 0) cbo.setSelectedIndex(selectedIdx);
		} catch (IllegalArgumentException e)
		{
			System.out.println("Out of ");
		}
		
		cbo.addItemListener(this);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		int selectedIdx = ((JComboBox) e.getSource()).getSelectedIndex();
		
		if(selectedIdx >= 0)
			setValue(selectedIdx);
		else
			setValue(-1);
	}

}
