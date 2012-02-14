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

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;

import org.tellervo.desktop.prefs.Prefs.PrefKey;


public class DoubleSpinnerWrapper extends ChangeWrapper<Double> {
	public DoubleSpinnerWrapper(JSpinner spinner, String prefName, Double defaultValue) {
		super(prefName, defaultValue, Double.class);
		
		spinner.setValue(getValue());
		spinner.addChangeListener(this);
	}

	public DoubleSpinnerWrapper(JSpinner spinner, PrefKey key, Double defaultValue) {
		super(key.getValue(), defaultValue, Double.class);
		
		spinner.setValue(getValue());
		spinner.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		setValue((Double) ((JSpinner)e.getSource()).getValue());
	}

}
