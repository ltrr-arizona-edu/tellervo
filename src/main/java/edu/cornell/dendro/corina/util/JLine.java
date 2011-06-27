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
package edu.cornell.dendro.corina.util;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Color;

/** A line, since JSeparator apparently can't be used (reliably, anyway)
outside of menus. */

@SuppressWarnings("serial")
public class JLine extends JPanel implements SwingConstants {
    /** Create a new horizontal line. */
    public JLine() {
        this(HORIZONTAL);
    }
    /** Create a new line, whose orientation may be HORIZONTAL or VERTICAL. */
    public JLine(int orientation) {
        setBorder(BorderFactory.createLineBorder(Color.gray));

        if (orientation == HORIZONTAL) {
            setMaximumSize(new Dimension(1000, THIN));
            setPreferredSize(new Dimension(100, THIN));
            setMinimumSize(new Dimension(THIN, THIN));
        } else if (orientation == VERTICAL) {
            setMaximumSize(new Dimension(THIN, 1000));
            setPreferredSize(new Dimension(THIN, 100));
            setMinimumSize(new Dimension(THIN, THIN));
        } else {
            throw new IllegalArgumentException("orientation must be" +
					       "VERTICAL or HORIZONTAL");
        }
    }

    // 1 doesn't work in 1.4?, 2 looks too thick.  maybe i'll need to write my own.
    private static final int THIN = 1;
}
