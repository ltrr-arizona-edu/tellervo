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
package org.tellervo.desktop.ui;

import java.awt.Window;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.tellervo.desktop.platform.Platform;


public class Alert {

    // TODO: automatic text wrapping for dialog text?
    // TODO: use curly-quotes (single and double) on mac?  on everywhere they're available?
    // "tightness" = (tight) alpha numeric punctuation whitespace (loose)
    // -- then curl in the direction of the "tighter" char -- keep vertical if they're the same

    // extracted error-dialog code
    public static void error(String title, String text) {
	JOptionPane.showMessageDialog(null, // ??  no parent -- is this ok?
				      text,
				      maybeTitle(title),
				      JOptionPane.ERROR_MESSAGE,
				      treeIcon);
    }
    
    public static void error(Window parent, String title, String text) {
    	JOptionPane.showMessageDialog(parent, 
    				      text,
    				      maybeTitle(title),
    				      JOptionPane.ERROR_MESSAGE,
    				      treeIcon);
        }

    // extracted error-dialog code
    public static void message(String title, String text) {
	JOptionPane.showMessageDialog(null, // ??  no parent -- is this ok?
				      text,
				      maybeTitle(title),
				      JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    public static void message(Window parent, String title, String text) {
    	JOptionPane.showMessageDialog(parent, 
    				      text,
    				      maybeTitle(title),
    				      JOptionPane.INFORMATION_MESSAGE);
        }
    
    /*
      uses of Alert.error():
      -- custom text (x1)
      -- error loading sample (x3)
      ---- file not found (always check for this!)
      -- error saving sample (x4)
      -- (...list others here: there are 242 calls to showMessageDialog() in corina...)
      -- error printing

      can i use this?  i think so...
      ---- Alert.errorLoading(exception, filename)?
      ---- Alert.errorSaving(exception, filename)?
      ---- errorPrinting(pe)?
      -- ALSO: let me use removeFolders here, so calling Alert.error*() can use any old filename
      -- (where else is it needed?  move to util?)
    */

    // there was an error loading |filename|, and |ioe| is to blame.
    // look at |ioe|, and figure out something intelligent to say.
    public static void errorLoading(String filename, IOException ioe) {
	if (ioe instanceof FileNotFoundException) {
	    Alert.error("File not found",
			"The file \"" + filename + "\" could not be\n" +
			"loaded, because it doesn't exist.  It may have\n" +
			"been moved or deleted");
	    // TODO: call removeFolders on filename, but say "in folder ..."?

	    // TODO: this is dumb.  where is it?  search sub-folders, super-folder for it.
	    // try different cases.  look for files with similar names (same name, different
	    // extension), and offer to use that, instead.
	    // -- (this is probably not possible here, because it's already been thrown.
	    // -- maybe i need a lookForFile() method?
	}

	// TODO: put other cases here
    }

    // BETTER: explain why showing a title at all on win32/linux is a good thing -- window lists?
    // -- (no, dialogs don't show up in win32 window lists)
    // SO: in the future, perhaps maybeTitle() will go away, and the |title| arg of error(), too
    private static String maybeTitle(String title) {
	return (Platform.isMac() ? "" : title);
    }

    // icon for dialogs
    private final static Icon treeIcon = Builder.getIcon("corina-application.png", 64); // WAS: Tree-64x64.png
 
}
