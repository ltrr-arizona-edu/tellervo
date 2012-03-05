/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.tellervo.desktop.platform.Platform;


// default:
// - FileMenu
// - EditMenu (writeme?)
// - ***
// - WindowMenu (mac only)
// - HelpMenu
// you may (should!):
// - insert frame-specific menuitems in the FileMenu (e.g., Export)
// - insert frame-specific menus at *** (e.g., View, Manipulate)
// - insert frame-specific menuitems in the EditMenu (e.g., Undo)
// you should not:
// - leave out the file or edit menus entirely: this is the only way win32/linux
//   users can get to Exit or Preferences.
// - leave out the WindowMenu, or modify it: mac users expect this
// - leave out the HelpMenu, or modify it: everybody wants this
// for example, editor will extend this by:
// - having the constructor add custom menus between "edit" and "window"
// --- (manip, sum, graph, etc.)
// - override addFileMenu() to add a custom file menu (with Export...)
// - override addEditMenu() to add a custom edit menu (with undo/cut/insert/etc.)
@SuppressWarnings("serial")
public class TellervoMenuBar extends JMenuBar {

    private JFrame f; // does this need to be protected?

    public TellervoMenuBar(JFrame f) {
	this.f = f;

	addFirstMenus();
	addMiddleMenus();
	addLastMenus();
    }

    public void addFirstMenus() {
	addFileMenu();
	addEditMenu();
    }

    public void addMiddleMenus() {
	// ** subclasses: add your own menus in here
    }

    public void addLastMenus() {
	addWindowMenu();
	addHelpMenu();
    }

    public void addFileMenu() {
	add(new FileMenu(f));
    }

    public void addEditMenu() {
	// TODO: add "preferences..." if not mac;
	// WRITEME: an EditMenu class?
    }

    public void addWindowMenu() {
	if (Platform.isMac())
	    add(new WindowMenu(f));
    }

    public void addHelpMenu() {
	add(new HelpMenu());
    }
}
