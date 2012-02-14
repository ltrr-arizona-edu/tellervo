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

import org.tellervo.desktop.prefs.Prefs.PrefKey;


public class FormatWrapper extends ItemWrapper<String> {
	
	String[] formats;
	
	@Deprecated
	public FormatWrapper(JComboBox cbo, String prefName, Object defaultValue) {
		super(prefName, defaultValue, String.class);
		initFormats(cbo);
	}
	
	@Deprecated
	public FormatWrapper(JComboBox cbo, String prefName, Object defaultValue, String[]values) {
		super(prefName, defaultValue, String.class);
		initGeneric(cbo, values);
	}
	
	public FormatWrapper(JComboBox cbo, PrefKey key, Object defaultValue, String[]values) {
		super(key.getValue(), defaultValue, String.class);
		initGeneric(cbo, values);
	}
	
	public FormatWrapper(JComboBox cbo, PrefKey key, Object defaultValue) {
		super(key.getValue(), defaultValue, String.class);
		initFormats(cbo);
	}
	
	

	private void initFormats(JComboBox cbo)
	{
		// show a sample for each format thingy...
		formats = new String[FORMAT_STRINGS.length];
		int selectedIdx = -1;
		
		for(int i = 0; i < FORMAT_STRINGS.length; i++) {
			formats[i] = new DecimalFormat(FORMAT_STRINGS[i]).format(SAMPLE_NUMBER);
			if(FORMAT_STRINGS[i].equals(getValue()))
				selectedIdx = i;
		}
		
		cbo.setModel(new DefaultComboBoxModel(formats));
		if(selectedIdx >= 0)
			cbo.setSelectedIndex(selectedIdx);
		
		cbo.addItemListener(this);
	}
	
	private void initGeneric(JComboBox cbo, String[] values)
	{
		
		// show a sample for each format thingy...
		formats = values;
		int selectedIdx = -1;
		
		for(int i = 0; i < values.length; i++) {
			if(formats[i].equals(getValue()))
				selectedIdx = i;
		}
		
		cbo.setModel(new DefaultComboBoxModel(values));
		if(selectedIdx >= 0)
			cbo.setSelectedIndex(selectedIdx);
		
		cbo.addItemListener(this);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		int selectedIdx = ((JComboBox) e.getSource()).getSelectedIndex();
		
		if(selectedIdx >= 0)
			setValue(formats[selectedIdx]);
		else
			setValue(null);
	}

    private final static String FORMAT_STRINGS[] = new String[] {
        "0.0", "0.00", "0.000", "0.0000", "0.00000",
        "0%", "0.0%", "0.00%", "0.000%",
    };

    private final static float SAMPLE_NUMBER = 0.49152f;
}
