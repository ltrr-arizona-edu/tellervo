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

package edu.cornell.dendro.corina.prefs.panels;

import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.prefs.components.ColorPrefComponent;
import edu.cornell.dendro.corina.prefs.components.BoolPrefComponent;
import edu.cornell.dendro.corina.prefs.components.FontPrefComponent;
import edu.cornell.dendro.corina.prefs.components.UIDefaultsComponent;

import java.awt.*;
import javax.swing.*;

// TODO: javadoc
// TODO: write
// TODO: debug
// TODO: use
public class AppearancePrefsPanel extends JComponent {
  private FontPrefComponent fpce, fpct;
  public AppearancePrefsPanel() {
    setLayout(new GridBagLayout());
    
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridwidth = GridBagConstraints.RELATIVE;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    JPanel jp = new JPanel(new FlowLayout());
    jp.add(makeEditorSettingsComponent());
    jp.add(makeTableSettingsComponent());
    add(jp, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 1;    
    gbc.fill = GridBagConstraints.BOTH;
    add(makeUIDefaultsComponent(), gbc);
  }
  
  private Component makeEditorSettingsComponent() {
	    JComponent gridGroup = new JPanel(new GridBagLayout());
	    gridGroup.setBorder(BorderFactory.createTitledBorder("Sample editor grid settings"));
	    gridGroup.setToolTipText("Grid settings: these settings apply to various Corina grid components");
	    
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
	    Container co = new Container();
	    fpce = new FontPrefComponent(Prefs.EDIT_FONT);
	    co.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
	    co.add(fpce.getLabel());
	    co.add(fpce.getButton());

	    c = co;

	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gridGroup.add(l, gbc);
	    gbc.gridx = 1;
	    gridGroup.add(c, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.gridwidth = 2;

	    gridGroup.add(new BoolPrefComponent("Draw gridlines?", Prefs.EDIT_GRIDLINES), gbc);
	    
	    return gridGroup;
  }
  
  private Component makeTableSettingsComponent() {
	    JComponent gridGroup = new JPanel(new GridBagLayout());
	    gridGroup.setBorder(BorderFactory.createTitledBorder("Other table settings"));
	    gridGroup.setToolTipText("Grid settings: these settings apply to most tables that contain only text");
	    
	    JLabel l = new JLabel("Text color:");
	    Component c = new ColorPrefComponent("corina.deftable.foreground");
	    l.setLabelFor(c);
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.insets = new Insets(2, 2, 2, 2);
	    
	    gridGroup.add(l, gbc);
	    gbc.gridx = 1;
	    gridGroup.add(c, gbc);
	    
	    l = new JLabel("Background color:");
	    c = new ColorPrefComponent("corina.deftable.background");
	    
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gridGroup.add(l, gbc);
	    gbc.gridx = 1;
	    gridGroup.add(c, gbc);
	    
	    l = new JLabel("Font:");
	    Container co = new Container();
	    fpct = new FontPrefComponent("corina.deftable.font");
	    co.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
	    co.add(fpct.getLabel());
	    co.add(fpct.getButton());

	    c = co;

	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gridGroup.add(l, gbc);
	    gbc.gridx = 1;
	    gridGroup.add(c, gbc);
	    
	    return gridGroup;
}
  
  private Component makeUIDefaultsComponent() {
    Component c = new UIDefaultsComponent();
    
    ((JComponent) c).setBorder(BorderFactory.createTitledBorder("UI fonts and colors"));
    ((JComponent) c).setToolTipText("UI fonts and colors: settings modified here will apply to all universally throughout Corina");
    
    return c;
  }
  
  @Override
public void addNotify() {
    fpce.setParent(getTopLevelAncestor());
    fpct.setParent(getTopLevelAncestor());
    super.addNotify();
  }
}
