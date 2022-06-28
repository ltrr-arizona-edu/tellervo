/*******************************************************************************
 * Copyright (C) 2003 Ken Harris
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

package org.tellervo.desktop.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;


/**
    An "Edit" menu.

    <p>The default contents are:</p>
    <ul>
        <li>Can't Undo (control-Z)
        <li>Can't Redo (control-shift-Z)</li>
        <br/>
        <li>Cut (control-Z)
        <li>Copy (control-C)
        <li>Paste (control-V)</li>
        <br/>
        <li>Select All (control-A)</li>
        <br/>
        <li>Preferences... (non-Mac only)
    </ul>

    <p>All of these entries are dimmed, by default (except Preferences).
    If you want this functionality, you have to add it yourself.</p>

    <p>Note that even if you don't need any of the commands in this menu,
    you should still normally use it (for document windows, at least).
    Users expect it there, and it also provides a way to get at the
    application Preferences for Windows users.</p>

    <p>If you add a "Delete" or "Clear" menuitem, it should be between
    "Paste" and "Select All" &mdash; but not all applications even have
    this menuitem, and what it's actually called depends on the application.</p>

    <p>To use this class, extend init() to add a new section, but be sure
    to include all of the sections that init() already adds.  (Yes, this
    could be easier.)</p>
 
    <p>Tip: if you extend this class, and end up with the default Edit menu,
    check your access modifiers: all of the methods of this class (like init())
    are protected.</p>

    <h2>Left to do:</h2>
    <ul>
        <li>Javadoc
        <li>Move to tellervo.menus (or tellervo.gui.menus), along with other menus
        <li>Use this in Editor and Browser
        <li>Get rid of "Redo", unless a subclass explicitly adds it?
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
@SuppressWarnings("serial")
public class EditMenu extends JMenu {

	/** Make a new Edit menu. */
    public EditMenu(JFrame frame) {
        super(I18n.getText("menus.edit"));
                
        init();
    }

    protected void init() {
        //addUndoRedo();
        //addSeparator();

        addClipboard();
        addSeparator();

        addSelectAll();

        addPreferences();
    }

    protected void addUndoRedo() {
        addUndo();
        addRedo();
    }

    protected void addUndo() {
        //add(Builder.makeMenuItem("menus.edit.undo", false, "undo.png"));
    }

    protected void addRedo() {
        //add(Builder.makeMenuItem("menus.edit.redo", false, "redo.png"));
    }

    protected void addClipboard() {
        addCut();
        addCopy();
        addPaste();
    }

    protected void addCut() {
        add(Builder.makeMenuItem("menus.edit.cut", false, "editcut.png"));
    }

    protected void addCopy() {
        add(Builder.makeMenuItem("menus.edit.copy", false, "editcopy.png"));
    }

    protected void addPaste() {
        add(Builder.makeMenuItem("menus.edit.paste", false, "editpaste.png"));
    }

    protected void addSelectAll() {
        add(Builder.makeMenuItem("menus.edit.select_all", false));
    }
    
    protected void addPreferences() {
        if (!Platform.isMac()) {
            addSeparator();
            
            JMenuItem prefs = Builder.makeMenuItem("menus.preferences", true, "advancedsettings.png");
            prefs.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					App.showPreferencesDialog();
				}
            	
            });
            add(prefs);
                 
        }
        

    }
    


}
