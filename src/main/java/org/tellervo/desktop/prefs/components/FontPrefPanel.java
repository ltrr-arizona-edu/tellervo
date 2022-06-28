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

import java.awt.FlowLayout;
import javax.swing.JComponent;

/**
 * A convenience container that contains both FontPrefComponent components.
 */
@SuppressWarnings("serial")
public class FontPrefPanel extends JComponent {
  private FontPrefComponent components;

  public FontPrefPanel(String pref) {
    this(new FontPrefComponent(pref));
  }
  public FontPrefPanel(FontPrefComponent components) {
    this.components = components;
    setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
    
    add(components.getLabel());
    add(components.getButton());
  }
  
  @Override
public void addNotify() {
    components.setParent(getTopLevelAncestor());
    super.addNotify();
  }
}
