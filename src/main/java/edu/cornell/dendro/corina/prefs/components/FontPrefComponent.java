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

package edu.cornell.dendro.corina.prefs.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.l2fprod.common.swing.JFontChooser;

import edu.cornell.dendro.corina.core.App;

/**
  A component for changing a font preference.
  
  <p>One limitation of this component is that it can only be used
  inside of top-level containers that extend Frame; in other words,
  it can't be used in applets.</p>

  <h2>Left to do</h2>
  <ul>
      <li>Use font name/size for preview-label text
      <li>Put button on new line?
      <li>I18n -- "Sample", "Change...", "Choose new font"
  </ul>

  @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
  @version $Id$
*/
public class FontPrefComponent implements ActionListener {
  private static final Container BOGUS_PARENT = new Container(); 
  private JLabel label = new JLabel("Sample");
  private JButton button;
  private String pref;
  private Component parent;
  /**
      Make a new FontPrefComponent which sets the specified preference.
      
      @param pref the preference key to change
  */
  public FontPrefComponent(String pref) {
    this.pref = pref;

    Font font = Font.decode(App.prefs.getPref(pref));
    label.setFont(font);
    label.setText(font.getName() + " " + font.getSize());

    button = new JButton("Change...");
    button.addActionListener(this);            
  }
  
  public void setParent(Component parent) {
    this.parent = parent;
  }
  
  public Component getParent() {
    return parent;
  }
  
  public JLabel getLabel() {
    return label;
  }
  
  public JButton getButton() {
    return button;
  }
  
  private Runnable showdialog = new Runnable() {
    public void run() {
      Component actualParent;
      if (parent != null) actualParent = parent;
      else actualParent = BOGUS_PARENT;
      //System.out.println(actualParent);
      Font font = JFontChooser.showDialog(actualParent, "Choose new font", label.getFont());

      if (font == null) return;

      label.setFont(font);
      label.setText(font.getName() + " " + font.getSize());
      label.repaint();
      // store pref
      App.prefs.setPref(pref, UIDefaultsComponent.stringifyFont(font));
    }
  };
  
  public void actionPerformed(ActionEvent ae) {
    SwingUtilities.invokeLater(showdialog);
  }
}