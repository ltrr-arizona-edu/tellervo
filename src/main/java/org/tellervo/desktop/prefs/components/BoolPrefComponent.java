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
package org.tellervo.desktop.prefs.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;



public class BoolPrefComponent extends JCheckBox implements ActionListener {

	private static final long serialVersionUID = 1L;
	private PrefKey prefKey;

	@Deprecated
    public BoolPrefComponent(String name, String pref) {
	super(name);

        this.prefKey = PrefKey.valueOf(pref);

        // set initial value
        if (App.prefs.getBooleanPref(pref, false)) // (null means false)
            setSelected(true);

        // listen for user clicks
	addActionListener(this);
    }
	
    public BoolPrefComponent(String name, PrefKey key) {
    	super(name);

            this.prefKey = key;

            // set initial value
            if (App.prefs.getBooleanPref(key, false)) // (null means false)
                setSelected(true);

            // listen for user clicks
    	addActionListener(this);
        }

    // list of widgets which this checkbox also dims/undims
    @SuppressWarnings("unchecked")
	private List controlees = new ArrayList();

    // tell the checkbox that it should dim this component when unchecked
    @SuppressWarnings("unchecked")
	public void controls(JComponent component) {
	controlees.add(component);

	if (!isSelected())
	    component.setEnabled(false);
    }

    // on check/uncheck...
    public void actionPerformed(ActionEvent e) {
	// enable/disable all the subsequent controls, as requested
        for (int i=0; i<controlees.size(); i++)
            ((JComponent) controlees.get(i)).setEnabled(isSelected());

	// set myself
        App.prefs.setPref(prefKey, String.valueOf(isSelected()));
    }
}
