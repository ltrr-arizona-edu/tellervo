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

package corina.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class CountRenderer extends JProgressBar implements TableCellRenderer {
    public CountRenderer(int max) {
	// range is 0..max
	super(0, max);

	// on win32, this forces native pixel-granularity
	// progressbars -- otherwise they'd be chunky
	setStringPainted(true);
	setString("");
    }
    public Component getTableCellRendererComponent(JTable table,
						   Object value,
						   boolean isSelected, boolean hasFocus,
						   int row, int column) {
	setValue(((Integer) value).intValue());
	return this;
    }
}
