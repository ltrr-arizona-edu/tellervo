/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.print;


import java.util.Date;

import java.text.DateFormat;
import java.text.MessageFormat;

import org.tellervo.desktop.ui.I18n;

/**
   A "by-line", showing who printed a document, and when.  Default
   text is similar to "Printed by [user] at [date], [time]".  It's
   gotten from the i18n key "printed_by"; the string {0} is replaced
   by the user's name, {1} is replaced by the date, and {2} is
   replaced by the time.

   <h2>Left to do</h2>
   <ul>
     <li>timeString includes seconds, but shouldn't (nobody cares about secs!)
     <li>"unknown user" isn't localized
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class ByLine extends TextLine {

    /**
       Make a new by-line.
    */
    public ByLine() {
        super(makeByLine(), 10);
    }

    // make a "printed by ... at ..." string
    private static String makeByLine() {
        Date date = new Date();
        String dateString = DateFormat.getDateInstance().format(date);
        String timeString = DateFormat.getTimeInstance().format(date);

	Object args[] = new Object[] {
	    System.getProperty("user.name", "(unknown user)"),
	    dateString,
	    timeString,
	};

        String byline = MessageFormat.format(I18n.getText("printed_by"), args);
        return byline;
    }
}
