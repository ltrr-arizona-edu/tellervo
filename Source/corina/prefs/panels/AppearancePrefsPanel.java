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
import corina.prefs.components.ColorPrefComponent;
import corina.prefs.components.BoolPrefComponent;
import corina.prefs.components.FontPrefComponent;
import corina.gui.layouts.DialogLayout;
import corina.gui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

// TODO: javadoc

// TODO: write
// TODO: debug
// TODO: use

public class AppearancePrefsPanel extends JPanel {

    public AppearancePrefsPanel() {
        // layout
        setLayout(new DialogLayout());

        final JPanel me = this; // ugh.
        
        // foreground color
        {
            ColorPrefComponent p = new ColorPrefComponent("corina.edit.foreground");
            JPanel t = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            t.add(p);
            add(t, "Text color:");
        }

        // background color
        {
            ColorPrefComponent p = new ColorPrefComponent("corina.edit.background");
            JPanel t = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            t.add(p);
            add(t, "Background color:");
        }

        add(Box.createVerticalStrut(20));

        // font
        add(new FontPrefComponent("corina.edit.font"), "Font:");
        
        add(Box.createVerticalStrut(20));

        // draw gridlines?
        add(new BoolPrefComponent("Draw gridlines?", "corina.edit.gridlines"), "");
    }
}
