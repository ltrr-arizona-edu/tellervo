package corina.gui.menus;

import corina.gui.menus.FileMenu;
import corina.gui.menus.EditMenu;
import corina.gui.menus.HelpMenu;
import corina.util.Platform;

import javax.swing.JMenuBar;
import javax.swing.JFrame;

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
public class CorinaMenuBar extends JMenuBar {

    private JFrame f; // does this need to be protected?

    public CorinaMenuBar(JFrame f) {
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
	if (Platform.isMac)
	    add(new WindowMenu(f));
    }

    public void addHelpMenu() {
	add(new HelpMenu());
    }
}
