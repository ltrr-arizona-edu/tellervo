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

package corina.prefs.components;

import corina.prefs.Prefs;
import corina.gui.UserCancelledException;

import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
    A component for changing a font preference.
    
    <p>One limitation of this component is that it can only be used
    inside of top-level containers that extend Frame; in other words,
    it can't be used in applets.</p>

    <h2>Left to do</h2>
    <ul>
        <li>If value of pref is null, bad things happen (bug!) -
            see both getPref() calls
        <li>Fix up variable names to be clearer
        <li>Use font name/size for preview-label text
        <li>Put button on new line?
        <li>I18n -- "Sample", "Change...", "Choose new font"
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class FontPrefComponent extends JPanel {

    /**
        Make a new FontPrefComponent which sets the specified preference.
        
        @param pref the preference key to change
    */
    public FontPrefComponent(String pref) {
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        final JLabel p = new JLabel("Sample");
        Font font = Font.decode(Prefs.getPref(pref));
        p.setFont(font);
        p.setText(font.getName() + " " + font.getSize());

        final String glue = pref;
        JButton b = new JButton("Change...");
        b.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    // get new value
                    String oldValue = Prefs.getPref(glue);
                    Frame owner = (Frame) getTopLevelAncestor();
                    String newValue = FontChooser.showDialog(owner,
                                                        "Choose new font",
                                                        oldValue);

                    // get the font
                    Font font = Font.decode(newValue);

                    // update sample text, font
                    p.setFont(font);
                    p.setText(font.getName() + " " + font.getSize());

                    // store pref
                    Prefs.setPref(glue, newValue);
                } catch (UserCancelledException uce) {
                    // do nothing
                }
            }
        });

        flow.add(p);
        flow.add(Box.createHorizontalStrut(8));
        flow.add(b);
    }
}
