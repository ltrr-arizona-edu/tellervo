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

package corina.prefs;

import corina.prefs.components.ColorPrefComponent;
import corina.prefs.components.FormattingPrefComponent;
import corina.prefs.components.BoolPrefComponent;
import corina.prefs.components.FontPrefComponent;
import corina.prefs.panels.AppearancePrefsPanel;
import corina.prefs.panels.DataPrefsPanel;
import corina.prefs.panels.GraphPrefsPanel;
import corina.prefs.panels.CrossdatingPrefsPanel;
import corina.prefs.panels.AdvancedPrefsPanel;

import corina.gui.HasPreferences; // future: not needed
import corina.gui.Bug; // ???
import corina.ui.Builder;
import corina.util.Center;
import corina.util.DocumentListener2;

import java.util.List;

import java.awt.BorderLayout;
import java.awt.Frame; // obsolete: used only for HasPrefs
//import java.awt.event.*; // !!!
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;

/**
    A dialog for setting user preferences.

    <p>A user front-end for Prefs.getPref()/setPref().  To show,
    use PrefsDialog.showPreferences().</p>

    <h2>Left to do:</h2>
    <ul>
        <li>extract StringPrefComponent?
        <li>fix the build: it's dying on javahelp (ok, not quite related, but...)
        <li>get rid of HasPreferences entirely; use listeners
        <li>i18n
        <li>need non-jlfgr icon
        <li>(unit test?)
        <li>make me a jdialog (so OS X doesn't clear the menubar?) (why haven't i done this?)
            -- or document why it has to be a jframe
        <li>policy for selecting a tab?  use mac os x convention -- does this mean close-op
            should be HIDE?  that would be easiest (fastest?), but also take more memory.
            but not much.
        <li>add a "reset to defaults" button at the bottom?
        <li>open-recent menu should be a prefs listener, too
    </ul>
    <p>Goal: even with javadoc, it should be under 200 lines; maybe under 150</p>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class PrefsDialog extends JFrame {
    // static reference: there can be only one! -- gets null on close, how?
    private static PrefsDialog me = null;

    // REMOVE ME: obsolete, since refreshFromPrefs() is going away
    public static void updateAll() {
        // call refreshFromPreferences on every Frame that HasPreferences
        Frame f[] = Frame.getFrames();
        for (int i=0; i<f.length; i++)
            if (f[i] instanceof HasPreferences)
                try {
                    ((HasPreferences) f[i]).refreshFromPreferences();
                } catch (Exception e) {
                    Bug.bug(e);
                }

        // save all
	Prefs.save();
    }

    /** Show the preferences dialog.  If there is already a Corina
	preferences dialog, this restores it and brings it to the
	front.  This prevents multiple preferences dialogs from
	appearing, which would serve only to confuse the user. */
    public static void showPreferences() {
	try {
	    if (me == null) {
		me = new PrefsDialog();
	    } else { // FIXME: THIS CODE IS NEVER USED (...why?)
		me.setVisible(true); // despite DISPOSE_ON_CLOSE, it's just getting hidden?
		me.setState(NORMAL);
		me.toFront();
	    }
	} catch (RuntimeException re) {
	    Bug.bug(re);
	}
    }

    // show a specific tab
    // TODO: document me!
    // in the future, this should be passed a name like "corina.prefs.panels.AppearancePrefPanel".
    // i think right now you'd pass it something like "advanced", but that won't work without the template.
    public static void showPreferences(String tab) {
        // show dialog, as normal
        showPreferences();

        // select the tab
        // WRITEME -- look for tab, use me.tabs.setSelectedIndex(i)
/* pseudocode:
        for (int i=0; i<me.tabs.getNumberOfTabs(); i++) {
            if (me.tabs.getTabAt(i).getClass().contains(tab))
                me.tabs.setSelectedIndex(i);
                return;
            }
        }
*/

        // note: if we're given a bogus tab name, it just falls through,
        // and the first tab remains selected, which is correct behavior.
    }

    private JTabbedPane tabs;

    // private, because nobody outside of showPreferences() should ever construct me
    private PrefsDialog() {
        setTitle("Preferences");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // dispose?  why?
        setResizable(false);

        // icon!
        Image image = Builder.getImage("Preferences16.gif");
        if (image != null) {
	        setIconImage(image);
        }

        // use my own panel, so i can add a border
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setContentPane(p);

        // fill up the tabbed pane
        tabs = new JTabbedPane();

        // add tabs
        tabs.addTab("Appearance", new AppearancePrefsPanel());
        tabs.addTab("Data", new DataPrefsPanel());
        tabs.addTab("Crossdating", new CrossdatingPrefsPanel());
        tabs.addTab("Graphs", new GraphPrefsPanel());
        tabs.addTab("Advanced", new AdvancedPrefsPanel());
        p.add(tabs);

        // pack, center on screen, and show
        pack();
        setSize(getWidth() * 2, getHeight()); // FIXME: this is really arbitrary!
	Center.center(this);
        show();
    }
}
