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

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;

import org.tellervo.desktop.core.App;


/*
    TODO: gpl header, javadoc
*/

// a prefs component for number formatting: 0.0, 0.00, 0.000, etc.
// FIXME: extend jcombobox, instead of jpanel?
@SuppressWarnings("serial")
public class FormattingPrefComponent extends Container {
    private static String FORMAT_STRINGS[] = new String[] {
        "0.0", "0.00", "0.000", "0.0000", "0.00000",
        "0%", "0.0%", "0.00%", "0.000%",
    };

    private static float SAMPLE_NUMBER = 0.49152f;

    // example: "tellervo.cross.tscore.format", "0.00", "T-score format:"
    // defaultValue may be null, in which case it falls back to "0.00" if needed.
    public FormattingPrefComponent(String key, String defaultValue) {
      setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        // make an array, |items|, that holds the strings i'll actually show
        int n = FORMAT_STRINGS.length;
        String items[] = new String[n];
        for (int i=0; i<n; i++) {
            items[i] = new DecimalFormat(FORMAT_STRINGS[i]).format(SAMPLE_NUMBER);
        }
        
        final JComboBox popup = new JComboBox(items);
        popup.setMaximumRowCount(n);
            // add(new JLabel(label)); // REMOVE!
        add(popup);
        // HACK:
        //setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // set default value
        setInitialValue(popup, key, defaultValue);

        final String glue = key;

        popup.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // set pref
                int i = popup.getSelectedIndex();
                String format = FORMAT_STRINGS[i];
                App.prefs.setPref(glue, format);
                    
                /* DEBUG
                System.out.println("properties[" + glue + "] = " + System.getProperty(glue));
                System.out.println("prefs[" + glue + "] = " + Prefs.getPref(glue));
                */
            }
        });
    }

    private void setInitialValue(JComboBox popup, String key, String defaultValue) {
        int n = FORMAT_STRINGS.length;

        // try getPref()
        String prefValue = App.prefs.getPref(key);
        if (prefValue != null) {
            
            // backwards compatibility hack: the old default for trend formats
            // was "00.0%", which isn't in this list.  so let's just silently
            // squeeze it into "0.0%", which is.
            if (prefValue.equals("00.0%"))
                prefValue = "0.0%";
                
            for (int i=0; i<n; i++)
                if (prefValue.equals(FORMAT_STRINGS[i])) {
                    popup.setSelectedIndex(i);
                    return;
                }
        }

        // if that fails, try defaultValue
        if (defaultValue != null) {
            for (int i=0; i<n; i++)
                if (defaultValue.equals(FORMAT_STRINGS[i])) {
                    popup.setSelectedIndex(i);
                    return;
                }
        }

        // if that fails, use "0.00"
        String fallbackValue = "0.00";
        for (int i=0; i<n; i++)
            if (fallbackValue.equals(FORMAT_STRINGS[i])) {
                popup.setSelectedIndex(i);
                return;
            }

        // if fallbackValue isn't in FORMAT_STRINGS, it'll just
        // stay on the first value, 0.0 -- which isn't the end of
        // the world.
    }
}
