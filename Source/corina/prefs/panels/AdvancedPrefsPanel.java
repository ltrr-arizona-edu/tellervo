// 
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.prefs.panels;

import corina.prefs.Prefs;
import corina.prefs.components.FontPrefComponent;

import corina.gui.Layout;
import corina.gui.layouts.DialogLayout;
import corina.util.DocumentListener2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentEvent;

import java.awt.*;

// TODO: javadoc

// TODO: center this stuff

// TODO: i18n

/*
    [ ] override menubar font
        font: Lucida Grande 11
              (Change...)
        
    [ ] Override user name
        name: [             ]
        
    
    use which file chooser:
        (*) swing (slower)
        ( ) awt (faster, but no preview)
*/

public class AdvancedPrefsPanel extends JPanel {

    public AdvancedPrefsPanel() {
        // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridLayout(0, 1));
        
        // menubar font override
        JCheckBox menubar = new JCheckBox("Override menubar font");
        menubar.setAlignmentX(LEFT_ALIGNMENT);
        add(menubar);

        // TODO: make checkbox a bool-pref
        // TODO: make checkbox control font-pref-component (dim)
        // TODO: c.gui.menubar.font isn't the right pref!
        // TODO: figure out how to make the checkbox un-set the font pref
        // (idea: PrefComponent interface, getPref(), checkbox calls
        // getPref() on controlled components, sets them to null on dim)
        
        JComponent font = new FontPrefComponent("corina.menubar.font");
        add(font);
        
        add(Box.createVerticalStrut(2));

        // user name override
        // TODO: fix layout
        // TODO: change these to better names (usernameCheckbox, usernameField)
        final JCheckBox username = new JCheckBox("Override user name");
        final JTextField usernameField = new JTextField(20);

        // set initial state from prefs
        if (Prefs.getPref("corina.user.name") != null) {
            username.setSelected(true);
            usernameField.setText(Prefs.getPref("corina.user.name"));
        } else {
            username.setSelected(false);
            usernameField.setEnabled(false);
            usernameField.setText(System.getProperty("user.name"));
        }

        /*
            why use corina.user.name instead of just setting user.name directly?
            because if the user unchecks the checkbox, we need to be able to get
            the original value of user.name back, and once you set the system
            property user.name to something else, the original value is gone.
        */

        username.setAlignmentX(LEFT_ALIGNMENT);
        username.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // enable/disable text field
                usernameField.setEnabled(username.isSelected());
                
                // name to use: either the default, or the user's version
                String name;
                if (username.isSelected())
                    name = usernameField.getText();
                else
                    name = System.getProperty("user.name");

                // set pref
                Prefs.setPref("corina.user.name", name);
            }
        });
        
        usernameField.getDocument().addDocumentListener(new DocumentListener2() {
            public void update(DocumentEvent e) {
                Prefs.setPref("corina.user.name", usernameField.getText());
            }
        });
        
        JPanel nameFlow = Layout.flowLayoutL(new JLabel("Name: "),
                                            usernameField);

        add(username);
        add(indent(nameFlow));
        add(Box.createVerticalStrut(2));
        
        // file chooser
        {
            ButtonGroup group = new ButtonGroup();
            JRadioButton swing = new JRadioButton("Swing (slower)");
            JRadioButton awt = new JRadioButton("AWT (faster, but no preview)");
            group.add(swing);
            group.add(awt);
            JLabel label = new JLabel("Use which file chooser:");
            label.setAlignmentX(LEFT_ALIGNMENT);

            add(label);
            add(indent(swing));
            add(indent(awt));
        }

        add(Box.createVerticalGlue());
        
        // TODO: add spacer below bottom, just in case?
    }
    
    // indent a component to the right by 20 pixels
    private JPanel indent(JComponent component) {
        return Layout.flowLayoutL(Box.createHorizontalStrut(20),
                                    component);
    }
}
