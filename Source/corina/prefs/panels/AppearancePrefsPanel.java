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
import corina.prefs.components.UIDefaultsComponent;

import java.awt.*;
import javax.swing.*;

// TODO: javadoc
// TODO: write
// TODO: debug
// TODO: use
public class AppearancePrefsPanel extends Container {

  public AppearancePrefsPanel() {
    setLayout(new GridBagLayout());
    
    JComponent gridGroup = new JPanel(new GridBagLayout());
    gridGroup.setBorder(BorderFactory.createTitledBorder("Grid settings"));
    gridGroup.setToolTipText("Grid settings");
    
    JLabel l = new JLabel("Text color:");
    Component c = new ColorPrefComponent(Prefs.EDIT_FOREGROUND);
    l.setLabelFor(c);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(2, 2, 2, 2);
    
    gridGroup.add(l, gbc);
    gbc.gridx = 1;
    gridGroup.add(c, gbc);
    
    l = new JLabel("Background color:");
    c = new ColorPrefComponent(Prefs.EDIT_BACKGROUND);
    
    gbc.gridx = 0;
    gbc.gridy = 1;
    gridGroup.add(l, gbc);
    gbc.gridx = 1;
    gridGroup.add(c, gbc);
    
    l = new JLabel("Font:");
    c = new FontPrefComponent(Prefs.EDIT_FONT);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gridGroup.add(l, gbc);
    gbc.gridx = 1;
    gridGroup.add(c, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;

    gridGroup.add(new BoolPrefComponent("Draw gridlines?", Prefs.EDIT_GRIDLINES), gbc);
    
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    
    add(gridGroup, gbc);
    
    gbc.gridy = 1;
    gbc.weighty = 1;
    
    gbc.fill = GridBagConstraints.BOTH;
    
    c = new UIDefaultsComponent();
    ((JComponent) c).setBorder(BorderFactory.createTitledBorder("UI fonts and colors"));
    add(c, gbc);
  }
}
