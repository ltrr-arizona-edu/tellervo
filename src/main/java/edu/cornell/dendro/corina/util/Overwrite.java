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

package edu.cornell.dendro.corina.util;

import java.io.File;

import javax.swing.JOptionPane;

import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.platform.Platform;

/**
   A helper function for asking "do you really want to overwrite that
   file?".

   <p>To use, simply add a call to overwrite().  If the user cancels,
   a UserCancelledException is thrown; otherwise, it does nothing.</p>

<pre>
    try {
       String filename = FileDialog.showDialog("save");
       Overwrite.overwrite(filename);
       save(filename);
    } catch (UserCancelledException uce) {
       // do nothing
    } catch (IOException ioe) {
       // ...
    }
</pre>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Overwrite {
    // don't instantiate me
    private Overwrite() { }

    // REFACTOR: move this to FileDialog?

    // FIXME: need i18n

    /**
       Ask if the user really wants to overwrite a file.

       <p>If the file doesn't exists, does nothing.  If it does
       already exist, it shows a dialog which informs the user that
       this file exists, and asks if Corina should overwrite it.</p>

       <p>If the file already exists, and the user chooses "Cancel",
       this method throws UserCancelledException; otherwise, it does
       nothing.</p>

       @param filename the name of the file
       @exception UserCancelledException if the file exists, and the
       user doesn't want to overwrite it
    */
    public static void overwrite(String filename) throws UserCancelledException {
	// does it exist?
	boolean exists = new File(filename).exists();

	// if not, it's safe.
	if (!exists)
	    return;

	// TODO: removeFolders() from filename when displaying
	// -- or merely make this an option?

	// TODO: this doesn't use the icon that Alert(?) does
	// -- do i want that icon?

	// it does exist.  ask the user what to do.

	String question = "A file called \"" + filename + "\"\n" +
	                  "already exists; overwrite it with this data?";

	// TODO: (isMac?"":...) construct exists elsewhere (Alert?) -- refactor
	String title = (Platform.isMac() ? "" : "Already Exists");

	// good, explicit commands
	Object choices[] = new Object[] { "Overwrite", "Cancel" };

        int x = JOptionPane.showOptionDialog(null, // parentComponent
                                             question,
                                             title,
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null, // icon: none
                                             choices,
                                             null); // default: none

	boolean overwrite = (x == 0); // (overwrite => true)

	// did the user cancel?
	if (!overwrite)
	    throw new UserCancelledException();
    }
}
