/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.sources;

import java.awt.*;
import javax.swing.*;

/**
   A panel which lists the sources the user is working with.

 <h2>Left to do:</h2>
 <ul>
    <li>events when source changes
    <li>let me change source from app?
    <li>keep track (prefs) what sources there are
    <li>add/remove sources
    <li>drag-n-drop, if sources support it
    <li>Javadoc
  </ul>
*/
@SuppressWarnings("serial")
public class SourcesPanel extends JPanel {

    public SourcesPanel() {
        Source s[] = new Source[] {
            new FolderSource(),
            new FavoritesSource("Oak Forest Masters"),
            new FavoritesSource("Juniper Masters"),
            new FavoritesSource("Ancient Masters"),
        };

        JList l = new JList(s); // TODO: exactly one!
        l.setCellRenderer(new SourceRenderer());
        JScrollPane p = new JScrollPane(l); // TODO: vert only!

        setLayout(new BorderLayout());
        add(p);
    }

    public static void main(String args[]) {
        JFrame f = new JFrame("Sources");
	f.getContentPane().add(new SourcesPanel());
	f.pack();
	f.setVisible(true);
    }

    private static class SourceRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
						      int index,
						      boolean isSelected, boolean cellHasFocus) {
	    JLabel c = (JLabel) super.getListCellRendererComponent(list, value,
								   index,
								   isSelected, cellHasFocus);
	    Source s = (Source) value;
	    c.setText(s.getName());
	    c.setIcon(s.getIcon());
	    return c;
	}
    }

}
