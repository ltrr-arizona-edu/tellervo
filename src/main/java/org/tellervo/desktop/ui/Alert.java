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
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.ui.MessageDialog.DialogType;


public class Alert {

    public static void error(String title, String text) {
    	MessageDialog dialog = new MessageDialog(App.mainWindow, title, text, DialogType.ERROR);
		dialog.setVisible(true);
    }
    
    public static void error(Window parent, String title, String text) {
    	MessageDialog dialog = new MessageDialog(parent, title, text, DialogType.ERROR);
		dialog.setVisible(true);
    }

    /**
     * @wbp.parser.entryPoint
     */
    @Deprecated
    public static void message(String title, String text) {
    	MessageDialog dialog = new MessageDialog(title, text, DialogType.INFO);
		dialog.setVisible(true);	
    }
    
    public static void message(Window parent, String title, String text) {
    	MessageDialog dialog = new MessageDialog(parent, title, text, DialogType.INFO);
		dialog.setVisible(true);
    }
    

    // there was an error loading |filename|, and |ioe| is to blame.
    // look at |ioe|, and figure out something intelligent to say.
    public static void errorLoading(String filename, IOException ioe) {
		if (ioe instanceof FileNotFoundException) {
			
			MessageDialog dialog = new MessageDialog("File not found",
					"The file \"" + filename + "\" could not be\n" +
					"loaded, because it doesn't exist.  It may have\n" +
					"been moved or deleted", DialogType.ERROR);
			dialog.setVisible(true);	
		
		}

		// TODO: put other cases here
    }



}
