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
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.graph;

import corina.Sample;
import corina.editor.Editor;

import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;

public class PopupMenu extends JPopupMenu {

    JMenuItem titleItem, rangeItem, open, plot;

    public PopupMenu() {
        // dummy entries: the name of the sample, and its range
        titleItem = new JMenuItem();
        titleItem.setEnabled(false);
        rangeItem = new JMenuItem();
        rangeItem.setEnabled(false);

        // open
        JMenuItem open = new JMenuItem("Open"); // is "Edit" better than "Open"?
        open.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                new Editor(_s);
            }
        });

        // plot
        JMenuItem plot = new JMenuItem("Plot");
        plot.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                new GraphFrame(_s);
            }
        });

        // WAS: "Redate..." menuitem

        add(titleItem);
        add(rangeItem);
        addSeparator();
        add(open);
        add(plot);
    }

    private Sample _s;

    public void setSample(Sample s) {
        _s = s;

        // update title, range
        titleItem.setText((String) (_s.meta.containsKey("title") ? _s.meta.get("title") : _s.meta.get("filename"))); // REFACTOR: move this into Sample.getTitle()?
        rangeItem.setText("    " + _s.range.toStringWithRange());
    }
}
