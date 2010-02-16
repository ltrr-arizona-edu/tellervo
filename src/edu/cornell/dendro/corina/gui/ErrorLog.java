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

package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;

/**
    An error log.

    <h2>Left to do:</h2>
    <ul>
        <li>Put date before each section, not after the first line (check time?)
        <li>I18n: "No Errors", "The following ...", "Error Log"
        <li>Add "Print" button

        <li>Update when new exceptions are thrown?
        <li>Put a little more spacing around the text area
        <li>Figure out how to focus the dialog, when it's re-shown
        <li>Add "Clear" button?

        <li>Consolidate ErrorLog/Bug duality?
    </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class ErrorLog extends JDialog {
  private static ErrorLog singleton = null;

  private ErrorLog() {
    setTitle("Log");
    setIconImage(Builder.getApplicationIcon());

    LogViewer center = new LogViewer();

    // WRITEME: to add printing, make a Printer that just calls
    // lines.add(line) for each |line| read from w.toString(),
    // and also call the java/awt printing method.

    center.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    getContentPane().add(center, BorderLayout.CENTER);
    pack();
    setSize(600, 500);
  }

  public static synchronized void showLogViewer() {
    
	if (singleton == null) {
      singleton = new ErrorLog();
    }
    Center.center(singleton);
    singleton.show();
  }
}